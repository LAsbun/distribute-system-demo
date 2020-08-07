package github.lasbun.lock.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class JedisLockTestTest {

    private final static Integer EXPERIED_TIME = 30;

    // 最大等待时间10s
    private final static Integer MAX_WAIT_TIME = 10000;

    private final static String OK = "OK";

    private final static SetParams param = new SetParams().ex(EXPERIED_TIME).nx();

    @Test
    public void testJedis() {
        Jedis jedis = JedisUtils.getJedis();
        String set = jedis.set("xx", "ds");
        System.out.println(set);
    }

    @Test
    public void testJedisSet() {

        ExecutorService executorService = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));

        AtomicInteger atomicInteger = new AtomicInteger(1);

        for (int i = 0; i < 1000; i++) {
            executorService.submit(() -> {
                Jedis jedis = JedisUtils.getJedis();
                try {
                    int o = atomicInteger.incrementAndGet();
                    log.info("[{}] set key:{}", o, o);
                    String set = jedis.set(String.valueOf(o), String.valueOf(o), param);
                    log.info("[{}] set result:{}", o, set);
                } catch (Exception e) {
                    log.error("x", e);
                } finally {
                    jedis.close();
                }
            });

        }
//        executorService.shutdown();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        jedis.close();
    }


    @Test
    public void testUnlock() {
        JedisLock jedisLock = new JedisLock();
        jedisLock.lock("c6f7d7fd-b12c-471e-9b1b-98cb5f6172a3");
        jedisLock.unlock("c6f7d7fd-b12c-471e-9b1b-98cb5f6172a3");
        System.out.println("xx");
    }
}
