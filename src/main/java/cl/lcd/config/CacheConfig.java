package cl.lcd.config;

import cl.lcd.model.LocationResponse;
import cl.lcd.model.LocationResponseWrapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.EhcacheManager;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@Profile("!nodb")
public class CacheConfig {
//    @Bean
//    public CacheManager cachemanager() {
//        org.ehcache.CacheManager ehCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
//                .withCache("locations",
//                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
//                                String.class, LocationResponse.class,
//                                ResourcePoolsBuilder.heap(500)
//                        )
//                                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(10)))
//                ).build(true);
//
//        return new EhCacheCacheManager(ehCacheManager);
//    }

    @Bean
    public CacheManager cacheManagerSetup() {
//        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("locations", "flightOffer");
//        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
//                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .maximumSize(1000));
//        return caffeineCacheManager;
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager() {
            @Override
            protected @NonNull Cache<Object, Object> createNativeCaffeineCache(@NonNull String name) {
                if("locations".equals(name)) {
                    return Caffeine.newBuilder()
                            .expireAfterWrite(10, TimeUnit.MINUTES)
                            .maximumSize(1000)
                            .build();
                } else if("flightOffers".equals(name)) {
                    return Caffeine.newBuilder()
                            .expireAfterWrite(5, TimeUnit.MINUTES)
                            .maximumSize(100)
                            .build();
                } else if("activities".equals(name)) {
                    return Caffeine.newBuilder()
                        .expireAfterWrite(12, TimeUnit.HOURS)
                        .maximumSize(100)
                        .build();
            }

                return Caffeine.newBuilder()
                        .expireAfterAccess(10, TimeUnit.MINUTES)
                        .maximumSize(1000)
                        .build();
            }
        };

        caffeineCacheManager.setCacheNames(List.of("locations", "flightOffers", "activities"));

        return caffeineCacheManager;
    }

//  cache manager auto managed by spring boot
//    @Bean
//    public RedisCacheConfiguration redisCacheConfiguration() throws JsonProcessingException {
//        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
//                .allowIfSubType("cl.lcd.model")
//                .allowIfSubType("java.util")
//                .build();
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.activateDefaultTyping(
////                LaissezFaireSubTypeValidator.instance,
////                mapper.getPolymorphicTypeValidator(),
//                ptv,
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY
//        );
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(mapper, Object.class);
//
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(60))
//                .disableCachingNullValues()
//                .serializeValuesWith(
//                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
//                );
//    }
//
//    @Bean
//    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(RedisCacheConfiguration redisCacheConfiguration) {
//        return (builder -> builder
//                .withCacheConfiguration("locations",
//                        redisCacheConfiguration.entryTtl(Duration.ofMinutes(10)))
//                .withCacheConfiguration("flightOffers",
//                        redisCacheConfiguration.entryTtl(Duration.ofMinutes(6)))
//        );
//    }

//    manual cache maneger
//    @Bean
//    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
//        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
//                .allowIfSubType("cl.lcd.model")
//                .allowIfSubType("java.util")
//                .build();
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.activateDefaultTyping(
//                ptv,
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY
//        );
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(mapper, Object.class);
//
//        RedisCacheConfiguration redisDefaultConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10))
//                .disableCachingNullValues()
//                .serializeValuesWith(
////                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
//                        RedisSerializationContext.SerializationPair.fromSerializer(
//                                new GenericJackson2JsonRedisSerializer()
//                        )
//                );
//
//        Map<String, RedisCacheConfiguration> customConfigs = new HashMap<>();
//        customConfigs.put("locations", redisDefaultConfig.entryTtl(Duration.ofMinutes(60)));
//        customConfigs.put("flightOffers", redisDefaultConfig.entryTtl(Duration.ofMinutes(5)));
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(redisDefaultConfig)
//                .withInitialCacheConfigurations(customConfigs)
//                .build();
//    }
}
