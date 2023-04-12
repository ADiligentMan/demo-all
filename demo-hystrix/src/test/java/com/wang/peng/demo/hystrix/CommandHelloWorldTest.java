package com.wang.peng.demo.hystrix;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author wangpeng
 * @date 2023/04/06
 */
@Slf4j
public class CommandHelloWorldTest {
    @Test
    public void run() {
        log.info("Curr Thread {}", Thread.currentThread().getName());
        String result = new CommandHelloWorld("Bob").execute();
        assertThat(result, equalTo("Hello Bob!"));
    }
}