package com.wang.peng.demo.hystrix;

import junit.framework.TestCase;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;

/**
 * @author wangpeng
 * @date 2023/04/07
 */
public class CommandHelloFailureTest extends TestCase {

    public void test() {
        String name = "Austin Ellis";
        CommandHelloFailure commandHelloFailure = new CommandHelloFailure(name);
        Assert.assertThat(commandHelloFailure.execute(), IsEqual.equalTo("Hello Failure " + name + "!"));
    }
}