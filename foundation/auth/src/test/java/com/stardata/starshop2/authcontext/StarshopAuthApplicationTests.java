package com.stardata.starshop2.authcontext;

import com.stardata.starshop2.authcontext.domain.user.User;
import com.stardata.starshop2.authcontext.domain.user.WxOpenId;
import com.stardata.starshop2.authcontext.south.port.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StarshopAuthApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;


    /**
     * 任务级测试：微信用户登录——确保用户记录存在；（组合任务，领域服务）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计原子任务级测试案例包括：
     * 1. 针对微信前端小程序获得用户信息字段，更新已有用户信息（昵称、头像等）；（原子任务，"用户"聚合行为）
     * 2. 设置新用户openid；（原子任务，"用户"聚合行为）
     * 3. 根据openid查找用户记录；（原子任务，资源库端口，访问数据库）
     * 4. 确保用户记录存在；（组合任务，领域服务）
     */

    //1. 针对微信前端小程序获得用户信息字段，更新已有用户信息（昵称、头像等）；（原子任务，"用户"聚合行为）
    @Test
    void should_copy_user1_wxinfo_to_user2_given_equal_values_except_id() {
        // given: 准备好user1对象并给除ID之外所有属性赋值、以及空的user2对象
        User user1 = User.of("testUser1", 1)
                .avatarUrl("https://www.somehost.com/someAvatar.png")
                .country("中国")
                .province("江苏")
                .city("南京")
                .language("zh_CN");
        User user2 = User.of("testUser2", 2);

        // when: 调用user2的copyInfoFrom方法
        user2.copyMiniAppInfoFrom(user1);

        // then: 检查user1和user2的相应属性是否相等
        assertEquals(user1.getNickName(), user2.getNickName());
        assertEquals(user1.getGender(), user2.getGender());
        assertEquals(user1.getAvatarUrl(), user2.getAvatarUrl());
        assertEquals(user1.getCountry(), user2.getCountry());
        assertEquals(user1.getProvince(), user2.getProvince());
        assertEquals(user1.getCity(), user2.getCity());
        assertEquals(user1.getLanguage(), user2.getLanguage());
    }

    //2. 设置新用户openid；（原子任务，"用户"聚合行为）
    @Test
    void should_set_openid_given_openid_correctly() {
        // given: 准备好openid，并创建user对象
        WxOpenId openId = WxOpenId.of("o9Nvx4gUq9dfO1KQy7LL-gXS_EkI");
        User user = User.of("testUser", 1);

        // when: 为user对象赋值openId
        user.setOpenId(openId);

        // then: 新用户的OpenId等于原先的OpenId
        assertEquals(user.getOpenId(), openId);
    }

    //3. 根据openid查找用户记录；（原子任务，资源库端口，访问数据库）
    @Test
    void should_load_user_by_openid_exists_given_user_correctly() {
        // given: 准备好已有用户的openid，以及资源库适配器
        WxOpenId openId = WxOpenId.of("o9Nvx4gUq9dfO1KQy7LL-gXS_EkI");

        // when: 从资源库重建用户对象
        User user = userRepository.findByOpenId(openId);

        // then: 用户对象存在且OpenId等于给定的openid
        assertNotNull(user);
        assertEquals(openId, user.getOpenId());
    }

    /**
     * 服务级测试：微信用户登录
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
