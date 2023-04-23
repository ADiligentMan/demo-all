package com.wang.peng.demo.hystrix;

import junit.framework.TestCase;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.junit.Test;

import java.sql.SQLOutput;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author wangpeng
 * @date 2023/04/17
 */
public class RemoteServiceTestCommandTest {
    @Test(expected = HystrixRuntimeException.class)
    public void testTimeOut()
            throws InterruptedException {

        HystrixCommand.Setter config = HystrixCommand
                .Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupTest5"));

        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        commandProperties.withExecutionTimeoutInMilliseconds(5_000);
        config.andCommandPropertiesDefaults(commandProperties);

        new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(15_000)).execute();
    }

    @Test
    public void testThreadPoolLimit() throws InterruptedException {
        HystrixCommand.Setter config = HystrixCommand
                .Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupThreadPool"));

        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        commandProperties.withExecutionTimeoutInMilliseconds(10_000);
        config.andCommandPropertiesDefaults(commandProperties);
        config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                .withMaxQueueSize(10)
                .withCoreSize(3)
                .withQueueSizeRejectionThreshold(10));

        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
                equalTo("Success"));
    }

    @Test
    public void givenCircuitBreakerSetup_whenRemoteSvcCmdExecuted_thenReturnSuccess()
            throws InterruptedException {

        HystrixCommand.Setter config = HystrixCommand
                .Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupCircuitBreaker"));

        HystrixCommandProperties.Setter properties = HystrixCommandProperties.Setter();
        properties.withExecutionTimeoutInMilliseconds(300);
        properties.withCircuitBreakerSleepWindowInMilliseconds(4000);
        properties.withExecutionIsolationStrategy
                (HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
        properties.withCircuitBreakerEnabled(true);
        // 熔断条件之一: 过去10s请求次数阈值, 默认为20
        properties.withCircuitBreakerRequestVolumeThreshold(3);
        // 熔断条件之二: 过去10s的请求，错误百分比(包括错误和延迟的请求)必须超过阈值, 默认为50
        properties.withCircuitBreakerErrorThresholdPercentage(20);

        config.andCommandPropertiesDefaults(properties);
        config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                .withCoreSize(1));

        assertThat(this.invokeRemoteService(config, 400), equalTo(null));
        assertThat(this.invokeRemoteService(config, 400), equalTo(null));
        assertThat(this.invokeRemoteService(config, 400), equalTo(null));
        assertThat(this.invokeRemoteService(config, 400), equalTo(null));
        assertThat(this.invokeRemoteService(config, 400), equalTo(null));
        Thread.sleep(10_000);

        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(100)).execute(),
                equalTo("Success"));

        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(100)).execute(),
                equalTo("Success"));

        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(100)).execute(),
                equalTo("Success"));
    }

    public String invokeRemoteService(HystrixCommand.Setter config, int timeout)
            throws InterruptedException {

        String response = null;

        try {
            response = new RemoteServiceTestCommand(config,
                    new RemoteServiceTestSimulator(timeout)).execute();
        } catch (HystrixRuntimeException ex) {
            System.out.println("ex = " + ex);
        }

        return response;
    }
}