package com.morningbees.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.cache.RedisCacheConfiguration
import java.time.Duration

@Configuration
@EnableCaching
class RedisConfig : CachingConfigurerSupport() {
    @Value("\${spring.redis.host}")
    private val redisHost: String? = null

    @Value("\${spring.redis.port}")
    private val redisPort: Int = 0

    @Value("\${spring.redis.password}")
    private val redisPassword: String? = null

    @Bean
    fun lettuceConnectionFactory(): RedisConnectionFactory {


        return LettuceConnectionFactory(redisHost!!, redisPort)
    }

    @Bean("redisTemplateWithLettuce")
    fun redisTemplateWithLettuce(

    ): RedisTemplate<String, Any> {

        val template = RedisTemplate<String, Any>()
        template.setDefaultSerializer(GenericJackson2JsonRedisSerializer())
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = GenericJackson2JsonRedisSerializer()
        template.hashKeySerializer = StringRedisSerializer()
        template.hashValueSerializer = GenericJackson2JsonRedisSerializer()

        template.setConnectionFactory(lettuceConnectionFactory())
        template.setEnableTransactionSupport(true)


        return template
    }

    @Bean
    override fun cacheManager(): CacheManager? {
        val builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(lettuceConnectionFactory())
        val configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofHours(1L))
        builder.cacheDefaults(configuration)
        return builder.build()
    }
}