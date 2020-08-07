package github.lasbun.lock.redis;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * Created by lasbun
 */
@Slf4j
public class JedisLockTest {
    private static int inventory = 1001;
    private static final int NUM = 1000;
    private static final JedisLock jedisLock = new JedisLock();

    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        CountDownLatch countDownLatch = new CountDownLatch(NUM);
        for (int i = 0; i < NUM; i++) {
            executorService.submit(() -> {
                String uuid = UUID.randomUUID().toString();

                try {
                    boolean lock = jedisLock.lock(uuid);
                    log.info("当前线程是:" + Thread.currentThread().getName() + " lock result " + lock);
                    log.info("[{}] 当前inventory start:{}", Thread.currentThread().getName(), inventory);
                    inventory--;
                    log.info("[{}] 当前inventory end:{}", Thread.currentThread().getName(), inventory);
                    countDownLatch.countDown();
                    jedisLock.unlock(uuid);
                    log.info("[{}] end", uuid);
                } catch (Exception e) {

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
