package org.wallet.dap.cache.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.DigestUtils;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.cache.RedisCache;
import org.wallet.dap.cache.RedisCacheTemplate;
import org.wallet.dap.cache.RedisScriptExecutor;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zengfucheng
 **/
@Slf4j
@Configuration
@EnableCaching
@EnableConfigurationProperties(DefaultRedisProperties.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ComponentScan(basePackages = "org.wallet.dap.cache")
public class RedisCacheConfig {

    @Bean
    public KeyGenerator keyGenerator() {
        return (o, method, objects) -> {
            StringBuilder key = new StringBuilder();
            key.append(o.getClass().getSimpleName());
            key.append(".");
            key.append(method.getName());
            key.append("[");
            for (Object obj : objects) {
                key.append(obj.toString());
            }
            key.append("]");

            return DigestUtils.md5DigestAsHex(key.toString().getBytes());
        };
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager cacheManager = new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                // 默认缓存过期时间 2个小时
                this.getRedisCacheConfigurationWithTtl(7200)
        );
        cacheManager.afterPropertiesSet();
        return cacheManager;
    }

    /**
     *  设置 Redis @Cacheable 注解默认过期时间
     *  设置@Cacheable 序列化方式
     * @return Redis配置
     */
    @Bean
    public RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds){
        // 打开自动装载类型
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        // 将类型写入JSON字符串，属性为@type
        fastJsonRedisSerializer.getFastJsonConfig().setSerializerFeatures(SerializerFeature.WriteClassName);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer));
        // Cacheable注解默认超时时间
        return configuration.entryTtl(Duration.ofSeconds(seconds));
    }

    /**
     * 获取集群Redis 连接工厂
     * @param redisProperties redis配置项
     * @return Redis 连接工厂
     */
    @Bean
    @ConditionalOnProperty(value = "spring.redis.cluster.nodes")
    LettuceConnectionFactory clusterLettucePoolingConnectionFactory(DefaultRedisProperties redisProperties) {
        Map<String, Object> source = new HashMap<>(2);

        source.put("spring.redis.cluster.nodes", String.join(",", redisProperties.getCluster().getNodes()));
        source.put("spring.redis.cluster.max-redirects", redisProperties.getCluster().getMaxRedirects());

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));

        return getLettucePoolingConnectionFactory(redisProperties, redisClusterConfiguration);
    }

    /**
     * 获取单机版Redis 连接工厂
     * @param redisProperties redis配置项
     * @return Redis 连接工厂
     */
    @Bean
    @ConditionalOnProperty(value = "spring.redis.host")
    LettuceConnectionFactory lettucePoolingConnectionFactory(DefaultRedisProperties redisProperties) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));

        return getLettucePoolingConnectionFactory(redisProperties, redisStandaloneConfiguration);
    }

    /**
     * 获取Lettuce Redis连接池
     * @param redisProperties Redis 配置项
     * @param redisConfiguration Redis 配置（单机或集群）
     * @return
     */
    private LettuceConnectionFactory getLettucePoolingConnectionFactory(DefaultRedisProperties redisProperties, RedisConfiguration redisConfiguration) {
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder lettucePoolingClientConfigurationBuilder = LettucePoolingClientConfiguration.builder();
        lettucePoolingClientConfigurationBuilder.poolConfig(getPoolConfig(redisProperties.getLettuce().getPool()));

        return new LettuceConnectionFactory(redisConfiguration,
                lettucePoolingClientConfigurationBuilder.build());
    }

    private GenericObjectPoolConfig<?> getPoolConfig(DefaultRedisProperties.Pool properties) {
        GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(properties.getMaxActive());
        config.setMaxIdle(properties.getMaxIdle());
        config.setMinIdle(properties.getMinIdle());
        if (properties.getTimeBetweenEvictionRuns() != null) {
            config.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRuns().toMillis());
        }
        if (properties.getMaxWait() != null) {
            config.setMaxWaitMillis(properties.getMaxWait().toMillis());
        }
        return config;
    }

    /**
     * 获取集群RedisTemplate
     * @param factory Redis连接工厂
     * @return Redis 模版
     */
    @Bean
    @ConditionalOnProperty(value = "spring.redis.cluster.nodes")
    RedisTemplate<String, Object> clusterRedisTemplate(LettuceConnectionFactory factory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        RedisSerializer serializer = new StringRedisSerializer();

        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);

        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        log.info("spring.redis.cluster.factory: {}", factory.getClass().getName());
        log.info("spring.redis.cluster.nodes: {}", JSON.toJSONString(factory.getClusterConfiguration().getClusterNodes()
                .stream().map(node -> node.getHost() + ":" + node.getPort()).collect(Collectors.toList())));

        return redisTemplate;
    }

    /**
     * 获取单机RedisTemplate
     * @param factory Redis连接工厂
     * @return Redis 模版
     */
    @Bean
    @ConditionalOnProperty(value = "spring.redis.host")
    RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        RedisSerializer serializer = new StringRedisSerializer();

        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);

        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);

        log.info("spring.redis.factory: {}", factory.getClass().getName());
        log.info("spring.redis.host: {}", factory.getHostName());
        log.info("spring.redis.port: {}", factory.getPort());
        log.info("spring.redis.database: {}", factory.getDatabase());

        return redisTemplate;
    }

    /**
     * 注入公用Cache类
     * @param redisTemplate RedisTemplate
     * @return 公用Cache类
     */
    @Bean
    Cache redisCache(RedisTemplate<String, Object> redisTemplate){
        RedisCache cache = new RedisCache();
        cache.setRedisTemplate(redisTemplate);
        return cache;
    }

    /**
     * Lua脚本工具类注入RedisTemplate
     * @param redisTemplate
     * @return
     */
    @Bean
    RedisScriptExecutor redisScriptExecutor(RedisTemplate<String, Object> redisTemplate){
        return new RedisScriptExecutor(redisTemplate);
    }

    /**
     * 注入RedisCacheTemplate，以便静态方式调用Redis
     * @param cache 公用Cache
     * @return RedisCacheTemplate
     */
    @Bean
    RedisCacheTemplate redisCacheTemplate(Cache cache){
        return new RedisCacheTemplate(cache);
    }
}
