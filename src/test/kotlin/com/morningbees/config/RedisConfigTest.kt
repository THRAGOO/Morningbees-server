package com.morningbees.config

import com.morningbees.SpringMockMvcTestSupport
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.*
import java.util.concurrent.TimeUnit


class RedisConfigTest : SpringMockMvcTestSupport() {

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, Any>

    @Test
    fun `캐시에 Value를 저장하고 읽는다`() {
        redisTemplate.opsForValue().set("test","hey",10, TimeUnit.SECONDS)
        val value = redisTemplate.opsForValue().get("test")

        Assertions.assertEquals("hey", value)
    }

    @Test
    fun `캐시에 저장한 Value가 특정 시간 이후에 Expire 된다`() {
        redisTemplate.opsForValue().set("test1","hey",1, TimeUnit.SECONDS)

        val old_value = redisTemplate.opsForValue().get("test1")
        Assertions.assertEquals("hey", old_value)

        Thread.sleep(1100)

        val new_value = redisTemplate.opsForValue().get("test1")
        Assertions.assertNull(new_value)
    }
}