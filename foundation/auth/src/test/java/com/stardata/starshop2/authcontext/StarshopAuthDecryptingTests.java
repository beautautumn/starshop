package com.stardata.starshop2.authcontext;

import com.stardata.starshop2.authcontext.domain.MobileNumberDecryptingService;
import com.stardata.starshop2.authcontext.domain.WxLoginWithTokenService;
import com.stardata.starshop2.authcontext.domain.user.User;
import com.stardata.starshop2.authcontext.domain.user.WxAuthInfo;
import com.stardata.starshop2.authcontext.south.port.UserRepository;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.MobileNumber;
import com.stardata.starshop2.sharedcontext.exception.ApplicationValidationException;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/25 15:00
 */
@SpringBootTest
public class StarshopAuthDecryptingTests {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 任务级测试：解密微信手机号 ——1. 解密微信手机号；（组合任务，领域服务）
     * 按照先聚合再端口、先原子再组合、从内向外的原则。
     * 设计原子任务级测试案例包括：
     * 1.1. 正常的已有用户、加密数据encryptedData、iv，成功解密手机号，并更新用户对象手机号
     * 1.2. 系统中不存在该用户，解密报业务异常 ApplicationValidationException
     * 1.3. 加密数据encryptedData、iv内容不合格，解密报业务异常 ApplicationValidationException
     */


    @Autowired
    WxLoginWithTokenService loginWithTokenService;

    @Autowired
    MobileNumberDecryptingService mobileNumberDecryptingService;

    @Autowired
    UserRepository userRepository;

    @Resource
    EntityManager entityManager;


    // 1.1. 正常的已有用户、加密数据encryptedData、iv，成功解密手机号，并更新用户对象手机号
    @Test
    @Transactional
    @Rollback(true)
    void should_get_mobile_phone_correctly_given_exists_userid_and_correct_encrypted_data_iv() {
        // given: 正常的已有用户、加密数据encryptedData、iv
        String code = "011NXJ000zQ8VN1iJZ000DWjjS2NXJ0e";
        String rawData = "{\"nickName\":\"深清秋\",\"gender\":0,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"avatarUrl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKq2CRmib1mpu4hOFYtcIHgAmS7DicCEfYkUHoPmPQn74BXH5GerjoMOxIqib7iafNNBw2ZAicBj6gZGUQ/132\"}";
        String signature = "3901b7322c7920ef4a5077eeba54b0d633585eef";
        WxAuthInfo wxAuthInfo = new WxAuthInfo(rawData, signature);
        User requestUser = User.of("深清秋", 1)
                .avatarUrl("https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKq2CRmib1mpu4hOFYtcIHgAmS7DicCEfYkUHoPmPQn74BXH5GerjoMOxIqib7iafNNBw2ZAicBj6gZGUQ/132")
                .country("中国")
                .province("江苏")
                .city("南京")
                .language("zh_CN");

        User user = loginWithTokenService.loginWithToken(code, wxAuthInfo, requestUser);
        userRepository.add(user);
        entityManager.flush();
        LongIdentity userId = user.getId();

        // when: 解密手机号
        String encryptedData = "NkSHcnyy8jJZwvsTBpb8Kw7jKIdYz1UnVqJ9Gf2NGFAn3DEeObh7W2Vh5hlgvs5zvtfNsV/IW+oHxEtzN4DY1E/tsGWnhiZYN+pYQun/8gNPrUNsjlR8saZIYoTGNcKpchNp+QEPzP/JFtGqIH+Etpk11RVRWepNJdYNzG3aNLUVmYQRqgCom7kYRrbM7g7GDu/jnqzvMn65ps48GawfQA==";
        String iv = "S2r8F/Gr0aUEs7BoerD5ZQ==";
        MobileNumber mobileNumber = mobileNumberDecryptingService.decryptWxMobileNumber(userId, encryptedData, iv);
        userRepository.update(user);
        entityManager.flush();

        User loadedUser = userRepository.instanceOf(userId);

        // then: 判定解密手机号是否正确
        assertNotNull(mobileNumber);
        assertEquals(mobileNumber.value(), "18652012976");
        assertEquals(loadedUser.getMobileNumber().value(), "18652012976");

    }

    //1.2. 系统中不存在该用户，解密报业务异常 ApplicationValidationException
    @Test
    @Transactional
    @Rollback(true)
    void should_throw_exception_given_not_exists_user_and_correct_encrypted_data_iv() {
        // given: 正确的加密数据encryptedData、iv，不存在的userId
        String code = "011NXJ000zQ8VN1iJZ000DWjjS2NXJ0e";
        String rawData = "{\"nickName\":\"深清秋\",\"gender\":0,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"avatarUrl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKq2CRmib1mpu4hOFYtcIHgAmS7DicCEfYkUHoPmPQn74BXH5GerjoMOxIqib7iafNNBw2ZAicBj6gZGUQ/132\"}";
        String signature = "3901b7322c7920ef4a5077eeba54b0d633585eef";
        WxAuthInfo wxAuthInfo = new WxAuthInfo(rawData, signature);
        User requestUser = User.of("深清秋", 1)
                .avatarUrl("https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKq2CRmib1mpu4hOFYtcIHgAmS7DicCEfYkUHoPmPQn74BXH5GerjoMOxIqib7iafNNBw2ZAicBj6gZGUQ/132")
                .country("中国")
                .province("江苏")
                .city("南京")
                .language("zh_CN");

        User user = loginWithTokenService.loginWithToken(code, wxAuthInfo, requestUser);
        userRepository.add(user);
        entityManager.flush();
        LongIdentity userId = LongIdentity.snowflakeId();

        try {
            // when: 解密手机号
            String encryptedData = "NkSHcnyy8jJZwvsTBpb8Kw7jKIdYz1UnVqJ9Gf2NGFAn3DEeObh7W2Vh5hlgvs5zvtfNsV/IW+oHxEtzN4DY1E/tsGWnhiZYN+pYQun/8gNPrUNsjlR8saZIYoTGNcKpchNp+QEPzP/JFtGqIH+Etpk11RVRWepNJdYNzG3aNLUVmYQRqgCom7kYRrbM7g7GDu/jnqzvMn65ps48GawfQA==";
            String iv = "S2r8F/Gr0aUEs7BoerD5ZQ==";
            MobileNumber mobileNumber = mobileNumberDecryptingService.decryptWxMobileNumber(userId, encryptedData, iv);

            // 下面这些不应该被执行到
            userRepository.update(user);
            entityManager.flush();

            User loadedUser = userRepository.instanceOf(userId);

            // then: 判定解密手机号是否正确
            assertNotNull(mobileNumber);
            assertEquals(mobileNumber.value(), "18652012976");
            assertEquals(loadedUser.getMobileNumber().value(), "18652012976");
        }
        catch (ApplicationValidationException e) {
            assertNotNull(e);
            assertEquals(e.getErrCode(), ApplicationValidationException.INVALID_REQUEST_ENTITY);
        }
    }

    // 1.3. 加密数据encryptedData、iv内容不合格，解密报业务异常 ApplicationValidationException
    @Test
    @Transactional
    @Rollback(true)
    void should_throw_exception_given_exists_user_and_incorrect_encrypted_data_iv() {
        // given: 正常的已有用户，不正确的加密数据encryptedData、iv
        String code = "011NXJ000zQ8VN1iJZ000DWjjS2NXJ0e";
        String rawData = "{\"nickName\":\"深清秋\",\"gender\":0,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"\",\"avatarUrl\":\"https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKq2CRmib1mpu4hOFYtcIHgAmS7DicCEfYkUHoPmPQn74BXH5GerjoMOxIqib7iafNNBw2ZAicBj6gZGUQ/132\"}";
        String signature = "3901b7322c7920ef4a5077eeba54b0d633585eef";
        WxAuthInfo wxAuthInfo = new WxAuthInfo(rawData, signature);
        User requestUser = User.of("深清秋", 1)
                .avatarUrl("https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKq2CRmib1mpu4hOFYtcIHgAmS7DicCEfYkUHoPmPQn74BXH5GerjoMOxIqib7iafNNBw2ZAicBj6gZGUQ/132")
                .country("中国")
                .province("江苏")
                .city("南京")
                .language("zh_CN");

        User user = loginWithTokenService.loginWithToken(code, wxAuthInfo, requestUser);
        userRepository.add(user);
        entityManager.flush();
        LongIdentity userId = user.getId();

        try {
            // when: 解密手机号
            String encryptedData = "NkSHcnyy88jJZwvsTBpb8Kw7jKIdYz1UnVqJ9Gf2NGFAn3DEeObh7W2Vh5hlgvs5zvtfNsV/IW+oHxEtzN4DY1E/tsGWnhiZYN+pYQun/8gNPrUNsjlR8saZIYoTGNcKpchNp+QEPzP/JFtGqIH+Etpk11RVRWepNJdYNzG3aNLUVmYQRqgCom7kYRrbM7g7GDu/jnqzvMn65ps48GawfQA==";
            String iv = "S2r8F/Gr0aUEs7BoerD5ZQ==";
            MobileNumber mobileNumber = mobileNumberDecryptingService.decryptWxMobileNumber(userId, encryptedData, iv);

            // 下面这些不应该被执行到
            userRepository.update(user);
            entityManager.flush();

            User loadedUser = userRepository.instanceOf(userId);

            // then: 判定解密手机号是否正确
            assertNotNull(mobileNumber);
            assertEquals(mobileNumber.value(), "18652012976");
            assertEquals(loadedUser.getMobileNumber().value(), "18652012976");
        }
        catch (ApplicationValidationException e) {
            assertNotNull(e);
            assertEquals(e.getErrCode(), ApplicationValidationException.INVALID_REQUEST_DATA);
        }
    }

}
