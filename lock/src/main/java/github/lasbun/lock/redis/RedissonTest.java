package github.lasbun.lock.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
public class RedissonTest {

    private static int inventory = 1001;
    private static final int NUM = 1000;

    private final static RLock rlock = getRlock();

    public static RLock getRlock() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient.getLock("lock_key_redission");
    }

    public static void main(String[] args) {


        ExecutorService executorService = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        CountDownLatch countDownLatch = new CountDownLatch(NUM);
        for (int i = 0; i < NUM; i++) {
            executorService.submit(() -> {
                String uuid = UUID.randomUUID().toString();

                try {
                    rlock.lock(10, TimeUnit.SECONDS);
                    log.info("当前线程是:" + Thread.currentThread().getName() + " lock result ");
                    log.info("[{}] 当前inventory start:{}", Thread.currentThread().getName(), inventory);
                    inventory--;
                    log.info("[{}] 当前inventory end:{}", Thread.currentThread().getName(), inventory);
                    countDownLatch.countDown();
                    log.info("[{}] end", uuid);
                    rlock.unlock();
                } catch (Exception e) {
                    log.error("xx", e);
                }
            });
        }

        executorService.shutdown();
        try {
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("当前存量 " + inventory);
    }
}
