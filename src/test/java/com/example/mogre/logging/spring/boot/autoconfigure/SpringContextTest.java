package com.example.mogre.logging.spring.boot.autoconfigure;

import com.mortengredal.logging.autoconfigure.LoggingAutoConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LoggingAutoConfiguration.class)
public class SpringContextTest {

    @Test
    public void springContextStrapped_shouldNotThrowExceptions() {
    }
}
