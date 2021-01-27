package com.tabber.tabby.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.logging.Logger;

@Component
public class RedisServiceManager {

    private static final Logger logger = Logger.getLogger(RedisServiceManager.class.getName());

    @Autowired
    private JedisConnectionFactory connFactory;

    private JedisConnection jedisConnection() {
        return (JedisConnection) connFactory.getConnection();
    }
    public String getValueForKey(String key){
        try (Jedis redis = jedisConnection().getJedis()) {
            return redis.get(key);
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING,
                    "An error occured while getting value for the key {}. Please check if redis is up", key);
            return null;
        }
    }
    public List<String> getAllKeys(String... keys) {
        try (Jedis redis = jedisConnection().getJedis()) {
            return redis.mget(keys);
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING,
                    "An error occured while getting value for the key {}. Please check if redis is up", keys);
            return null;
        }
    }
    public long delKey(String key) {
        try (Jedis redis = jedisConnection().getJedis()) {
            return redis.del(key);
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING,
                    "An error occured while getting value for the key {}. Please check if redis is up", key);
            return -1;
        }
    }
    /**
     * Method to create mapping for key, value with expiry. If exists, it overwrites it
     *
     * @param key
     * @param value
     * @param expiryInSecs
     * @return Status code reply
     */
    public String setWithExpiry(String key, String value, int expiryInSecs) {
        try (Jedis redis = jedisConnection().getJedis()) {
            return redis.setex(key, expiryInSecs, value);
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING,
                    "An error occured while getting value for the key {}. Please check if redis is up", key);
            return null;
        }
    }
    public long increment(String key, int expiry) {
        try (Jedis redis = jedisConnection().getJedis()) {
            long count = redis.incr(key);
            if (count == 1) {
                redis.expire(key, expiry);
            }
            return count;
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING,
                    "An error occured while getting value for the key {}. Please check if redis is up", key);
            return -1;
        }
    }
    public Integer flushAll() {
        try (Jedis redis = jedisConnection().getJedis()) {
            redis.flushAll();
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING,
                    "An error occured while flushing. Please check if redis is up");
            return -1;
        }
        return 1;
    }
    public List<byte[]> pop(int timeout, byte[]... keys){
        try (Jedis redis = jedisConnection().getJedis()) {
            return redis.brpop(timeout,keys);
        }catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING,
                    "An error occured while getting value for the key {}. Please check if redis is up", keys);
            return null;
        }
    }
    public Long rPush(String key, String  value){
        try (Jedis redis = jedisConnection().getJedis()) {
            return redis.rpush(key,value);
        }catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING,
                    "An error occured while getting value for the key {}. Please check if redis is up", key);
            return null;
        }
    }

    public Integer delKeys(String... keys) {
        try (Jedis redis = jedisConnection().getJedis()) {
            redis.del(keys);
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING,
                    "An error occured while getting value for the key {}. Please check if redis is up", keys);
            return -1;
        }
        return 1;
    }
}
