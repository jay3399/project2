package Jay.BoardP;


import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    private @Value("${spring.redis.host}") String redisHost;
    private @Value("${spring.redis.port}") int redisPort;
    private @Value("${spring.redis.timeout}") Long timeout;



    @Bean
    public RedisConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }


    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory());
        return redisTemplate;
    }

        @Bean
    public CacheManager cacheManager() {
//        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
//            .RedisCacheManagerBuilder
//            .fromConnectionFactory(connectionFactory());
//        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
//            .entryTtl(Duration.ofHours(timeout));
//        builder.cacheDefaults(configuration);
//        return builder.build();

        RedisCacheConfiguration configuration = RedisCacheConfiguration
            .defaultCacheConfig()
            .disableCachingNullValues()
            .entryTtl(Duration.ofDays(1))
            .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(
                SerializationPair.fromSerializer(new JdkSerializationRedisSerializer()));

        return RedisCacheManagerBuilder
            .fromConnectionFactory(connectionFactory())
            .cacheDefaults(configuration)
            .build();


    }


    @Bean
    public CacheManager cacheManagerV3() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//            .entryTtl(Duration.ofSeconds(30))
            .serializeKeysWith(RedisSerializationContext
                .SerializationPair
                .fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext
                .SerializationPair
                .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        Map<String, RedisCacheConfiguration> cacheConfiguration = new HashMap<>();
//        cacheConfiguration.put( "board", redisCacheConfiguration.entryTtl(Duration.ofSeconds(30)));

        cacheConfiguration.put("board", redisCacheConfiguration.entryTtl(Duration.ofSeconds(180)));
        cacheConfiguration.put("comment", redisCacheConfiguration.entryTtl(Duration.ofSeconds(180)));
        cacheConfiguration.put("file", redisCacheConfiguration.entryTtl(Duration.ofSeconds(180)));





        return RedisCacheManager
            .RedisCacheManagerBuilder
            .fromConnectionFactory(connectionFactory())
            .cacheDefaults(redisCacheConfiguration)
            .withInitialCacheConfigurations(cacheConfiguration)
            .build();
    }


}



//    @Bean
//    public CacheManager userCacheManager() {
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
//            .entryTtl(Duration.ofMinutes(3L));
//
//        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory()).cacheDefaults(redisCacheConfiguration).build();
//    }
//
//
//
//    @Bean
//    public CacheManager userCacheManager() {
//        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory());
//        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
//            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())) // Value Serializer 변경
//            .entryTtl(Duration.ofMinutes(30)); // 캐시 수명 30분
//        builder.cacheDefaults(configuration);
//        return builder.build();
//    }

//
//    @Bean
//    public RedisTemplate<?, ?> redisTemplateV2() {
//        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        redisTemplate.setConnectionFactory(connectionFactory());
//        return redisTemplate;
//    }

//
////    @Bean
//    public JedisConnectionFactory connectionFactory() {
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//        jedisConnectionFactory.setHostName(redisHost);
//        jedisConnectionFactory.setPort(redisPort);
//        jedisConnectionFactory.setUsePool(true);
//        return jedisConnectionFactory;
//    }

//    @Bean
//    public LettuceConnectionFactory lettuceConnectionFactory() {
//        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
//            .commandTimeout(Duration.ofMinutes(1))
//            .shutdownTimeout(Duration.ZERO)
//            .build();
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
//        return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
//    }


//    @Bean
//    public RedisTemplate<?, ?> redisTemplate() {
//        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(connectionFactory() );
//        return redisTemplate;
//    }

