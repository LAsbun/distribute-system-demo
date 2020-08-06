package github.LAsbun.lock;

/**
 * Created by lasbun
 */

import github.LAsbun.lock.redis.JedisLock;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class LocalLockTest {

    private static int inventory = 1001;
    private static final int NUM = 1000;
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        CountDownLatch countDownLatch = new CountDownLatch(NUM);
        for (int i = 0; i < NUM; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前线程是:" + Thread.currentThread().getName() + " lock result" + lock);
//                    log.info("[{}] 当前inventory start:{}", Thread.currentThread().getName(), inventory);
                    inventory--;
//                    log.info("[{}] 当前inventory end:{}", Thread.currentThread().getName(), inventory);
                    countDownLatch.countDown();
                }
            });
        }

        executorService.shutdown();
        try {
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("当前存量 " + inventory);
    }

}
