package com.morningbees

import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.flywaydb.test.junit5.annotation.FlywayTestExtension;


@ExtendWith(SpringExtension::class)
@SpringBootTest
@FlywayTestExtension
@FlywayTest
open class MorningbeesApplicationTests {

}
