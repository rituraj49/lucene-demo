package cl.lcd.config;

import cl.lcd.model.LocationResponse;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.EhcacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
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
    public CacheManager cacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("locations", "flightOffer");
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000));
        return caffeineCacheManager;
    }
}
