package com.tabber.tabby.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfiguration {
    @Bean
    public RedisCacheManager redisCacheManager(JedisConnectionFactory jedisConnectionFactory) {
        RedisCacheManager cm = RedisCacheManager.builder(jedisConnectionFactory)
                .cacheDefaults(cacheConfiguration()).build();
        cm.afterPropertiesSet();
        return cm;
    }
    private static RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(86400))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
    }
}
