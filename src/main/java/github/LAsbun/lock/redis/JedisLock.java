package github.LAsbun.lock.redis;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

/**
 * Created by lasbun
 */
@Slf4j
public class JedisLock {

    private final static String LOCK_KEY = "lock_key";

    //seconds
    private final static Integer EXPERIED_TIME = 30;

    // 最大等待时间10s
    private final static Integer MAX_WAIT_TIME = 10000;

    private final static String OK = "OK";

    private final static SetParams param = new SetParams().ex(EXPERIED_TIME).nx();

    private final Jedis jedis;


    public JedisLock() {
        jedis = JedisUtils.getJedis();
    }

    public boolean lock(String id) {
        long start = System.currentTimeMillis();
        for (; ; ) {
        log.info("[{}] start set", id);
        // todo 这里多线程会卡死 奇怪了
            String set = jedis.set(LOCK_KEY, id, param);
            log.info("[{}] set:{}", id, set);
            if (OK.equals(set)) {
                return true;
            }

            long l = System.currentTimeMillis() - start;
            if (l > MAX_WAIT_TIME) {
                return false;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean unlock(String id) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1]" +
                " then return redis.call('del', KEYS[1]) " +
                " else return 0 end";

        Object eval = jedis.eval(script, Collections.singletonList(LOCK_KEY),
                Collections.singletonList(id));

        boolean equals = eval.equals(1L);
        log.info("[{}] unlock result:{}", id, equals);
        return equals;
    }

    public void close() {
        jedis.close();
    }
}
