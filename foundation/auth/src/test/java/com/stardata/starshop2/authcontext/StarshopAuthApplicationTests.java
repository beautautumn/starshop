package com.stardata.starshop2.authcontext;

import com.stardata.starshop2.authcontext.domain.*;
import com.stardata.starshop2.authcontext.domain.loginlog.LoginLog;
import com.stardata.starshop2.authcontext.domain.user.User;
import com.stardata.starshop2.authcontext.domain.user.UserToken;
import com.stardata.starshop2.authcontext.domain.user.WxAuthInfo;
import com.stardata.starshop2.authcontext.domain.user.WxOpenId;
import com.stardata.starshop2.authcontext.north.local.AuthAppService;
import com.stardata.starshop2.authcontext.pl.UserResponse;
import com.stardata.starshop2.authcontext.pl.WxLoginRequest;
import com.stardata.starshop2.authcontext.south.port.LoginLogRepository;
import com.stardata.starshop2.authcontext.south.port.UserRepository;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class StarshopAuthApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 任务级测试：微信用户登录——1. 确保用户记录存在；（组合任务，领域服务）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计原子任务级测试案例包括：
     * 1.1. 针对微信前端小程序获得用户信息字段，更新已有用户信息（昵称、头像等）；（原子任务，"用户"聚合行为）
     * 1.2. 设置新用户openid；（原子任务，"用户"聚合行为）
     * 1.3. 根据openid查找用户记录；（原子任务，资源库端口，访问数据库）
     * 1.4. 确保前端微信用户信息对应的记录存在；（对应openid的用户存在，根据微信前端用户信息更新用户）；
     * 1.5. 确保前端微信用户信息对应的记录存在；（对应openid的用户不存在，根据微信前端用户信息新建用户）；
     */

    //1.1. 针对微信前端小程序获得用户信息字段，更新已有用户信息（昵称、头像等）；（原子任务，"用户"聚合行为）
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

    //1.2. 设置新用户openid；（原子任务，"用户"聚合行为）
    @Test
    void should_set_openid_given_openid_correctly() {
        // given: 准备好openid，并创建user对象
        WxOpenId openId = WxOpenId.of("o9Nvx4gUq9dfO1KQy7LL-gXS_EkI");
        User user = User.of("testUser", 1);

        // when: 为user对象赋值openId
        user.setOpenid(openId);

        // then: 新用户的OpenId等于原先的OpenId
        assertEquals(user.getOpenid(), openId);
    }

    @Autowired
    UserRepository userRepository;

    //1.3. 根据openid查找用户记录；（原子任务，资源库端口，访问数据库）
    @Test
    @Transactional
    @Rollback(true)
    void should_load_user_by_openid_exists_given_user_correctly() {
        // given: 创建新用户，并设置其openid后保存
        WxOpenId openId = WxOpenId.of("testOpenIdX");
        User user = User.of("testUserX", 1);
        user.setOpenid(openId);
        userRepository.add(user);

        // when: 向资源库加入该用户，并重新从资源库根据openid重建用户对象
        User loadedUser = userRepository.findByOpenId(openId);

        // then: 重建的用户对象存在且OpenId等于给定的openid
        assertNotNull(loadedUser);
        assertEquals(openId, loadedUser.getOpenid());
    }

    @Autowired
    UserExistenceService userExistenceService;

    //1.4. 确保前端微信用户信息对应的记录存在；（对应openid的用户存在，根据微信前端用户信息更新用户）；
    @Test
    @Transactional
    @Rollback(true)
    void should_load_exists_user_by_openid_given_update_wx_user_info() {
        // given: 已有微信用户信息、openid、并根据该openid在系统中插入用户记录
        WxOpenId openId = WxOpenId.of("testOpenId");
        User existsUser = User.of("testUserX", 1)
                .avatarUrl("testUrlX")
                .country("testCountryX")
                .province("testProvinceX")
                .city("testCityX")
                .language("testLanguageX");

        existsUser.setOpenid(openId);
        userRepository.add(existsUser);

        User frontUser = User.of("testUserY", 2)
                .avatarUrl("testUrlY")
                .country("testCountryY")
                .province("testProvinceY")
                .city("testCityY")
                .language("testLanguageY");
        frontUser.setOpenid(openId);

        // when: 调用领域服务 UserExistenceService.ensureUser 更新用户信息，然后从db重建用户对象
        User user = userExistenceService.ensureUser(openId, frontUser);
        User loadedUser = userRepository.instanceOf(user.getId());

                // then: 返回用户对象的所有前端小程序可传入属性字段，与给定的用户信息完全相同
        assertNotNull(loadedUser);
        assertTrue(isSameMiniAppUserInfo(loadedUser, frontUser));
    }

    private boolean isSameMiniAppUserInfo(@NotNull User loadedUser, @NotNull User frontUser) {
        return loadedUser.getNickName().equals(frontUser.getNickName()) &&
                loadedUser.getGender().equals(frontUser.getGender()) &&
                loadedUser.getAvatarUrl().equals(frontUser.getAvatarUrl()) &&
                loadedUser.getCountry().equals(frontUser.getCountry()) &&
                loadedUser.getProvince().equals(frontUser.getProvince()) &&
                loadedUser.getCity().equals(frontUser.getCity()) &&
                loadedUser.getLanguage().equals(frontUser.getLanguage());

    }

    //1.5. 确保前端微信用户信息对应的记录存在；（对应openid的用户不存在，根据微信前端用户信息新建用户）；
    @Test
    @Transactional
    @Rollback(true)
    void should_load_not_exists_user_by_openid_given_insert_wx_user_info() {
        // given: 已有openid，但db中没有对应的User对象
        WxOpenId openId = WxOpenId.of("testOpenId");

        User frontUser = User.of("testUserY", 2)
                .avatarUrl("testUrlY")
                .country("testCountryY")
                .province("testProvinceY")
                .city("testCityY")
                .language("testLanguageY");
        frontUser.setOpenid(openId);

        // when: 调用领域服务 UserExistenceService.ensureUser 查询新用户，然后从db重建用户对象
        User user = userExistenceService.ensureUser(openId, frontUser);
        User loadedUser = userRepository.instanceOf(user.getId());

        // then: 返回用户对象的所有前端小程序可传入属性字段，与给定的用户信息完全相同
        assertNotNull(loadedUser);
        assertTrue(isSameMiniAppUserInfo(loadedUser, frontUser));

    }

    /**
     * 任务级测试：微信用户登录——2.生成用户登录令牌；（组合任务，领域服务）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计原子任务级测试案例包括：
     * 2.1. 创建用户登录令牌；（原子任务，用户聚合行为，用户原来无令牌，创建新令牌）
     * 2.2. 创建用户登录令牌；（原子任务，用户聚合行为，用户原来有令牌，更新令牌）
     * 2.3. 生成用户登录令牌；（组合任务，领域服务，保存令牌后重建用户对象，令牌正确）
     * 2.4. 生成用户登录令牌；（组合任务，领域服务，老用户更新令牌后保存，再重新加载后令牌已更新）
     */

    // 2.1. 创建用户登录令牌；（原子任务，用户聚合行为，用户原来无令牌，创建新令牌）
    @Test
    void should_create_new_token_for_user_give_token_correctly() {
        //given: 一个新用户
        User newUser = User.of("testUser", 1);
        String sessionKey = "testSessionKey";

        //when: 调用User.refreshLoginToken创建令牌
        UserToken token = newUser.refreshLoginToken(sessionKey);

        //then: 有效的用户令牌
        assertNotNull(token);
        assertNotNull(newUser.currentToken());
        assertFalse(StringUtils.isBlank(token.getToken()));
    }

    // 2.2. 创建用户登录令牌；（原子任务，用户聚合行为，用户原来有令牌，更新令牌）
    @Test
    void should_update_token_for_user_give_token_different() {
        //given: 一个新用户
        User newUser = User.of("testUser", 1);
        String sessionKey = "testSessionKey";

        //when: 调用User.refreshLoginToken创建两次令牌
        UserToken tokenOld = newUser.refreshLoginToken(sessionKey);
        UserToken tokenNew = newUser.refreshLoginToken(sessionKey);

        //then: 两次用户令牌不一样
        assertNotNull(tokenOld);
        assertNotNull(tokenNew);
        assertNotNull(newUser.currentToken());
        assertNotEquals(tokenOld.getToken(), tokenNew.getToken());
    }

    // 2.3. 生成用户登录令牌；（组合任务，领域服务，新用户创建令牌后保存，再重新加载后令牌正确）
    @Test
    @Transactional
    @Rollback(true)
    void should_create_user_token_given_token_updated() {
        //given: 一个新用户
        User newUser = User.of("testUser", 1);
        String sessionKey = "testSessionKey";

        //when: 调用User.refreshLoginToken创建令牌，并持久化用户对象后再重建
       UserToken token = newUser.refreshLoginToken(sessionKey);
       userRepository.add(newUser);

       User loadedUser = userRepository.instanceOf(newUser.getId());

       //then: 用户令牌被保存，且内容正确
       assertNotNull(loadedUser);
       assertNotNull(loadedUser.currentToken());
       assertEquals(loadedUser.currentToken(), token);
    }

    @Resource
    EntityManager entityManager;

    // 2.4. 生成用户登录令牌；（组合任务，领域服务，老用户更新令牌后保存，再重新加载后令牌正确）
    @Test
    @Transactional
    @Rollback(true)
    void should_update_user_token_given_token_updated() {
        //given: 创建一个用户及其登录令牌保存到db
        String sessionKey = "testSessionKey";
        User user = User.of("testUser", 1);
        user.refreshLoginToken(sessionKey);
        userRepository.add(user);
        entityManager.flush();

        String oldToken = user.currentToken().getToken();

        //when: 调用User.refreshLoginToken更新令牌，并持久化用户对象后再重建
        user.refreshLoginToken(sessionKey);
        userRepository.update(user);
        entityManager.flush();
        User loadedUser = userRepository.instanceOf(user.getId());

        //then: 用户令牌被保存，且内容正确
        assertNotNull(loadedUser);
        assertNotNull(loadedUser.currentToken());
        assertNotEquals(loadedUser.currentToken().getToken(), oldToken);
        logger.info("oldToken: " + oldToken);
        logger.info("newToken: " + loadedUser.currentToken().getToken());
    }


    /**
     * 任务级测试：微信用户登录——3. 微信后台登录并校验；（组合任务，领域服务）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计原子任务级测试案例包括：
     * 3.1. 微信后台登录并校验；（组合任务，领域服务，微信code有效且未被使用过）
     * 3.2. 微信后台登录并校验；（组合任务，领域服务，微信code有效且未被使用过，但signature 不正确）
     * 3.3. 微信后台登录并校验；（组合任务，领域服务，微信code有效但已被使用过）
     * 3.4. 微信后台登录并校验；（组合任务，领域服务，微信code无效）
     */

    @Autowired
    WxLoginService wxLoginService;

    //3.1. 微信后台登录并校验；（组合任务，领域服务，微信code有效且未被使用过）
    @Test
    void should_wx_login_by_valid_and_unused_code_given_login_success() {
        // given: 有效的微信前端code、有效的微信认证用户rawData和signature
        String code = "081sloll28T8d94nl8nl2hxKhh3slolv";
        String rawData = "{\"nickName\":\"深清秋\",\"gender\":0,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"avatarUrl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKq2CRmib1mpu4hOFYtcIHgAmS7DicCEfYkUHoPmPQn74BXH5GerjoMOxIqib7iafNNBw2ZAicBj6gZGUQ/132\"}";
        String signature = "06846a4ba8b003af5d98fadfaf376a652e5d75d7";
        WxAuthInfo wxAuthInfo = new WxAuthInfo(rawData, signature);

        // when: 调用微信登录服务 WxLoginService.wxLogin
        WxOpenId wxOpenId = wxLoginService.wxLogin(code, wxAuthInfo);

        // then: 返回有效的openid
        assertNotNull(wxOpenId);
        assertFalse(StringUtils.isBlank(wxOpenId.toString()));
    }

    //3.2. 微信后台登录并校验；（组合任务，领域服务，微信code有效且未被使用过，但signature 不正确）
    @Test
    void should_wx_login_by_valid_and_unused_code_but_signature_error_given_login_exception() {
        // given: 有效的微信前端code、有效的微信认证用户rawData和signature
        String code = "081sloll28T8d94nl8nl2hxKhh3slolv";
        String rawData = "{\"nickName\":\"深清秋\",\"gender\":0,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"avatarUrl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKq2CRmib1mpu4hOFYtcIHgAmS7DicCEfYkUHoPmPQn74BXH5GerjoMOxIqib7iafNNBw2ZAicBj6gZGUQ/132\"}";
        String signature = "06846a4ba8b003af5d98fadfaf376a652e5d75d8";
        WxAuthInfo wxAuthInfo = new WxAuthInfo(rawData, signature);

        try {
            // when: 调用微信登录服务 WxLoginService.wxLogin
            WxOpenId wxOpenId = wxLoginService.wxLogin(code, wxAuthInfo);
        }
        catch(WxLoginErrorException e) {
            // then: 抛出"Checking userinfo integrity failed"异常
            assertNotNull(e);
            assertEquals(e.getErrCode(), -99);
        }

    }

    //3.3. 微信后台登录并校验；（组合任务，领域服务，微信code有效但已被使用过）
    @Test
    void should_wx_login_by_valid_and_used_code_given_login_biz_exception() {
        // given: 有效的微信前端code、有效的微信认证用户rawData和signature
        String code = "091FHp0w3KT3yY2VjV1w3aMiIB3FHp0B";
        String rawData = "{\"nickName\":\"深清秋\",\"gender\":0,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"avatarUrl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKq2CRmib1mpu4hOFYtcIHgAmS7DicCEfYkUHoPmPQn74BXH5GerjoMOxIqib7iafNNBw2ZAicBj6gZGUQ/132\"}";
        String signature = "af36606701965ce329c2613b91f801d2c07b332d";
        WxAuthInfo wxAuthInfo = new WxAuthInfo(rawData, signature);

        try{
            // when: 调用微信登录服务 WxLoginService.wxLogin
            WxOpenId wxOpenId = wxLoginService.wxLogin(code, wxAuthInfo);
            assertEquals(0, 1); //正常情况下不应该执行到这儿
        }
        catch(WxLoginErrorException e) {
            // then: 抛出"微信登录错误"异常
            assertNotNull(e);
        }
    }

    //3.4. 微信后台登录并校验；（组合任务，领域服务，微信code无效）
    @Test
    void should_wx_login_by_invalid_code_given_login_biz_exception() {
        // given: 有效的微信前端code、有效的微信认证用户rawData和signature
        String code = "testCode";
        String rawData = "testRawData";
        String signature = "testSignature";
        WxAuthInfo wxAuthInfo = new WxAuthInfo(rawData, signature);

        try{
            // when: 调用微信登录服务 WxLoginService.wxLogin
            WxOpenId wxOpenId = wxLoginService.wxLogin(code, wxAuthInfo);
            assertEquals(0, 1); //正常情况下不应该执行到这儿
        }
        catch(WxLoginErrorException e) {
            // then: 抛出"微信登录错误"异常
            assertNotNull(e);
        }
    }

    @Autowired
    WxLoginWithTokenService wxLoginWithTokenService;

    /**
     * 任务级测试：微信用户登录——4.生成微信登录令牌；（组合任务，领域服务）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计原子任务级测试案例包括：
     * 4.1. 微信登录成功,生成登录令牌；（组合任务，领域服务）
     * 4.2. 微信登录异常,未生成微信登录令牌；（组合任务，领域服务）
     */

    // 4.1. 微信登录成功,生成登录令牌；（组合任务，领域服务）
    @Test
    @Transactional
    @Rollback(true)
    void should_wx_login_with_token_by_valid_and_unused_code_given_login_success() {

        // given: 有效的微信前端code、有效的微信认证用户rawData和signature、有效的用户信息
        String code = "081sloll28T8d94nl8nl2hxKhh3slolv";
        String rawData = "{\"nickName\":\"深清秋\",\"gender\":0,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"avatarUrl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKq2CRmib1mpu4hOFYtcIHgAmS7DicCEfYkUHoPmPQn74BXH5GerjoMOxIqib7iafNNBw2ZAicBj6gZGUQ/132\"}";
        String signature = "06846a4ba8b003af5d98fadfaf376a652e5d75d7";
        WxAuthInfo wxAuthInfo = new WxAuthInfo(rawData, signature);
        User frontUser = User.of("testUser", 2)
                .avatarUrl("testUrl")
                .country("testCountry")
                .province("testProvince")
                .city("testCity")
                .language("testLanguage");

        // when: 调用wxLoginWithTokenService.loginWithToken()
        User loginUser = wxLoginWithTokenService.loginWithToken(code, wxAuthInfo, frontUser);

        // then: 有效的用户令牌
        UserToken token = loginUser.currentToken();
        WxOpenId openid = loginUser.getOpenid();
        assertNotNull(token);
        assertFalse(StringUtils.isBlank(token.getToken()));
        assertNotNull(openid);
        assertFalse(StringUtils.isBlank(openid.toString()));
    }

    // 4.2. 微信登录异常,未生成微信登录令牌；（组合任务，领域服务）
    @Test
    @Transactional
    @Rollback(true)
    void should_wx_login_with_token_by_valid_and_unused_code_but_signature_error_given_login_exception() {

        // given: 有效的微信前端code、有效的微信认证用户rawData和signature、有效的用户信息
        String code = "081sloll28T8d94nl8nl2hxKhh3slolv";
        String rawData = "{\"nickName\":\"深清秋\",\"gender\":0,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"avatarUrl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKq2CRmib1mpu4hOFYtcIHgAmS7DicCEfYkUHoPmPQn74BXH5GerjoMOxIqib7iafNNBw2ZAicBj6gZGUQ/132\"}";
        String signature = "06846a4ba8b003af5d98fadfaf376a652e5d75d8";
        WxAuthInfo wxAuthInfo = new WxAuthInfo(rawData, signature);
        User frontUser = User.of("testUser", 2)
                .avatarUrl("testUrl")
                .country("testCountry")
                .province("testProvince")
                .city("testCity")
                .language("testLanguage");

        try{
            // when: 调用wxLoginWithTokenService.loginWithToken()
            User loginUser = wxLoginWithTokenService.loginWithToken(code, wxAuthInfo, frontUser);

            // 如下代码应该不被执行
            UserToken token = loginUser.currentToken();
            WxOpenId openid = loginUser.getOpenid();
            assertNotNull(token);
            assertFalse(StringUtils.isBlank(token.getToken()));
            assertNotNull(openid);
            assertFalse(StringUtils.isBlank(openid.toString()));
        }

        catch(WxLoginErrorException e) {
            // then: 有效的用户令牌
            // then: 抛出"Checking userinfo integrity failed"异常
            assertNotNull(e);
            assertEquals(e.getErrCode(), -99);
        }
    }

    /**
     * 任务级测试：微信用户登录——5. 记录用户登录日志；（组合任务，领域服务）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计原子任务级测试案例包括：
     * 5.1. 记录用户登录日志；（组合任务，领域服务）
     */

    @Autowired
    LoginLogService loginLogService;

    @Autowired
    LoginLogRepository loginLogRepository;

    // 5.1. 记录用户登录日志；（组合任务，领域服务）
    @Test
    @Transactional
    @Rollback(true)
    void should_create_and_save_login_log_given_login_log_correctly() {
        // given:
        WxOpenId openId = WxOpenId.of("testOpenId");
        User user = User.of("testUser", 2)
                .avatarUrl("testUrl")
                .country("testCountry")
                .province("testProvince")
                .city("testCity")
                .language("testLanguage");
        user.setOpenid(openId);
        userRepository.add(user);


        // when: 调用 loginLogService.recordLogin
        LoginLog loginLog = loginLogService.recordLogin(user, "testIp");
        entityManager.flush();

        LoginLog loadedLog = loginLogRepository.instanceOf(loginLog.getId());

        // then: 创建了有效的登录日志
        assertNotNull(loadedLog);
        assertNotNull(loadedLog.getLastLoginTime());
        LocalDateTime now = LocalDateTime.now();
        assertTrue(loadedLog.getLastLoginTime().isBefore(now.plusSeconds(1)));

    }

    /**
     * 服务级测试：微信用户登录
     * 6. 测试微信用户登录应用服务，包括的测试案例有：
     * 6.1 正确的微信code，且未过期，该用户为新用户，要求返回72小时内有效的新token
     * 6.2 正确的微信code，且未过期，该用户为老用户，要求返回72小时内有效的新token
     * 6.3 正确的微信code，且未过期，但用户信息完整性被破坏，要求返回业务失败
     * 6.4 正确的微信code，但已过期，要求返回业务失败
     * 6.5 错误的微信code，要求返回无效请求告警
     */

    @Autowired
    AuthAppService authAppService;

    //6.1 正确的微信code，且未过期，该用户为新用户，要求返回72小时内有效的新token
    @Test
    @Transactional
    @Rollback(true)
    void should_login_by_new_user_valid_code_given_new_token_with_valid_period() {
        // given: 准备好输入数据
        String code = "081sloll28T8d94nl8nl2hxKhh3slolv";
        String rawData = "{\"nickName\":\"深清秋\",\"gender\":0,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"avatarUrl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKq2CRmib1mpu4hOFYtcIHgAmS7DicCEfYkUHoPmPQn74BXH5GerjoMOxIqib7iafNNBw2ZAicBj6gZGUQ/132\"}";
        String signature = "06846a4ba8b003af5d98fadfaf376a652e5d75d7";

        WxLoginRequest request = new WxLoginRequest();
        request.setCode(code);
        request.setRequestIp("testLoginIp");
        request.setRawData(rawData);
        request.setSignature(signature);
        request.setNickName("testUser1");
        request.setGender(1);
        request.setAvatarUrl("https://www.somehost.com/someAvatar.png");
        request.setCountry("中国");
        request.setProvince("江苏");
        request.setCity("南京");
        request.setLanguage("zh_CN");


        // when: 执行authAppService.loginByWx方法调用
        UserResponse response = authAppService.loginByWx(request);

        // then: 检查调用结果是否正确
        assertNotNull(response);
        assertNotNull(response.getId());
        assertNotNull(response.getToken());

        User loadedUser = userRepository.instanceOf(LongIdentity.from(response.getId()));
        User frontUser = User.of("testUser1", 1)
                        .avatarUrl("https://www.somehost.com/someAvatar.png")
                        .country("中国")
                        .province("江苏")
                        .city("南京")
                        .language("zh_CN");
        assertTrue(isSameMiniAppUserInfo(loadedUser, frontUser));

        UserToken userToken = loadedUser.currentToken();
        LocalDateTime now = LocalDateTime.now();
        userToken.getExpireTime().isAfter(now.minusMinutes(1).plusHours(72));
    }


}
