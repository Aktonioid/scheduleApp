package com.sched.sched;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Tag("UnitTest")
@ExtendWith(SpringExtension.class)
@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = SchedApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
  // locations = "resources",
  locations = {"classpath:application-integrationtest.properties"}
  )
public class UserRepoTests {
    
}
