package github.LAsbun.lock.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by lasbun
 */

public class JedisUtils {

    private static Jedis jedis;

    // 单例获取jedis连接
    // 一般生产环境都是使用连接池
    public static Jedis getJedis() {
//        if (null == jedis) {
//            synchronized (JedisUtils.class) {
//                if (null == jedis) {
//                    jedis = new Jedis("127.0.0.1");
//                }
//            }
//        }
//        return jedis;
        return new Jedis("127.0.0.1");
    }
}
