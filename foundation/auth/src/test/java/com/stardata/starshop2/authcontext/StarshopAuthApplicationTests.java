package com.stardata.starshop2.authcontext;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StarshopAuthApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());



    /**
     * 任务级测试：
     */

    /**
     * 服务级测试：
     * 1. 测试微信登录，包括的测试案例有：
     * 1.1 正确的微信code，且未过期，该用户为新用户，要求返回72小时内有效的新token
     * 1.2 正确的微信code，且未过期，该用户为老用户，要求返回72小时内有效的新token
     * 1.3 正确的微信code，且未过期，但用户信息完整性被破坏，要求返回业务失败
     * 1.4 正确的微信code，但已过期，要求返回业务失败
     * 1.5 错误的微信code，要求返回无效请求告警
     */

    @Test
    void should_login_by_new_user_valid_code_given_new_token_with_valid_period() {
        // given: 准备好输入数据

        // when: 执行被测试方法调用

        // then: 检查调用结果是否正确
    }


    /**
     * 服务级测试：
     * 2. 测试获取微信手机号，包括的测试案例有：
     */
}
