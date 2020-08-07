package github.LAsbun.lock.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by lasbun
 */

public class JedisUtils {

    private static JedisPool jedisPool;

    // 单例获取jedis连接
    // 一般生产环境都是使用连接池
    public static Jedis getJedis() {
        if (null == jedisPool) {
            synchronized (JedisUtils.class) {
                if (null == jedisPool) {
                    jedisPool = new JedisPool("127.0.0.1");
                }
            }
        }
        return jedisPool.getResource();
    }
}
