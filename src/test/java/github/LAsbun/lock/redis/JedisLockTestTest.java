package github.LAsbun.lock.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.*;

public class JedisLockTestTest {

    @Test
    public void testJedis() {
        Jedis jedis = JedisUtils.getJedis();
        String set = jedis.set("xx", "ds");
        System.out.println(set);
    }

    @Test
    public void testUnlock() {
        JedisLock jedisLock = new JedisLock();
        jedisLock.lock("c6f7d7fd-b12c-471e-9b1b-98cb5f6172a3");
        jedisLock.unlock("c6f7d7fd-b12c-471e-9b1b-98cb5f6172a3");
        System.out.println("xx");
    }
}
