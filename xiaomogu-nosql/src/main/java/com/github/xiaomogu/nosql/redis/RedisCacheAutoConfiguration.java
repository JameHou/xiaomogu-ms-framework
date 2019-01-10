package com.github.xiaomogu.nosql.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xiaomogu.commons.utils.EnvironmentUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @date 2019/1/9 17:18
 */
@Slf4j
@EnableCaching
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnClass({CacheProperties.Redis.class, RedisCacheConfiguration.class})
public class RedisCacheAutoConfiguration implements EnvironmentAware {

    private final CacheProperties cacheProperties;

    private ConfigurableEnvironment environment;

    public RedisCacheAutoConfiguration(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> String
                .join("", target.getClass().getName(), method.getName(),
                        Arrays.stream(params).map(Object::toString).collect(Collectors.joining("")));
    }


    @Bean
    public RedisCacheManager getRedisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        CustomRedisCacheWriter customRedisCacheWriter = new CustomRedisCacheWriter(
                RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory));

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromCacheWriter(customRedisCacheWriter).cacheDefaults(determineConfiguration());

        List<String> cacheNames = this.cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            Map<String, RedisCacheConfiguration> map = Maps.newHashMap();
            cacheNames.forEach(name -> {
                RedisCacheConfiguration redisCacheConfiguration = determineConfiguration();
                redisCacheConfiguration = redisCacheConfiguration.entryTtl(EnvironmentUtil
                        .resolverSetting(Duration.class, "spring.cache.redis." + name + ".ttl", environment));
                map.put(name, redisCacheConfiguration);
            });
            builder.withInitialCacheConfigurations(map);
        }
        return builder.build();
    }

    private RedisCacheConfiguration determineConfiguration() {
        CacheProperties.Redis redisProperties = this.cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        jackson2JsonRedisSerializer.setObjectMapper(om);
        config = config
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        config = config
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));


        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }
}
