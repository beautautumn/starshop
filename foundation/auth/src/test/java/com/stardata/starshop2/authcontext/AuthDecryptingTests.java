package com.stardata.starshop2.authcontext;

import com.stardata.starshop2.authcontext.domain.MobileNumberDecryptingService;
import com.stardata.starshop2.authcontext.domain.WxLoginWithTokenService;
import com.stardata.starshop2.authcontext.domain.user.User;
import com.stardata.starshop2.authcontext.domain.user.WxAuthInfo;
import com.stardata.starshop2.authcontext.north.local.AuthAppService;
import com.stardata.starshop2.authcontext.pl.MobileNumberResponse;
import com.stardata.starshop2.authcontext.pl.WxEncryptedUserInfo;
import com.stardata.starshop2.authcontext.south.port.UserRepository;
import com.stardata.starshop2.sharedcontext.domain.LongIdentity;
import com.stardata.starshop2.sharedcontext.domain.MobileNumber;
import com.stardata.starshop2.sharedcontext.domain.SessionUser;
import com.stardata.starshop2.sharedcontext.exception.ApplicationValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/25 15:00
 */
@SpringBootTest
public class AuthDecryptingTests {

    /**
     * 任务级测试：解密微信手机号 ——1. 解密微信手机号；（组合任务，领域服务）
     * 按照先聚合再端口、先原子再组合、从内向外的分解步骤。
     * 设计原子任务级测试用例包括：
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
    void should_get_mobile_phone_correctly_given_exists_userid_and_correct_encrypted_data_iv_by_domain_service() {
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

        String encryptedData = "NkSHcnyy8jJZwvsTBpb8Kw7jKIdYz1UnVqJ9Gf2NGFAn3DEeObh7W2Vh5hlgvs5zvtfNsV/IW+oHxEtzN4DY1E/tsGWnhiZYN+pYQun/8gNPrUNsjlR8saZIYoTGNcKpchNp+QEPzP/JFtGqIH+Etpk11RVRWepNJdYNzG3aNLUVmYQRqgCom7kYRrbM7g7GDu/jnqzvMn65ps48GawfQA==";
        String iv = "S2r8F/Gr0aUEs7BoerD5ZQ==";

        // when: 解密手机号
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
    void should_throw_exception_given_not_exists_user_and_correct_encrypted_data_iv_by_domain_service() {
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

        String encryptedData = "NkSHcnyy8jJZwvsTBpb8Kw7jKIdYz1UnVqJ9Gf2NGFAn3DEeObh7W2Vh5hlgvs5zvtfNsV/IW+oHxEtzN4DY1E/tsGWnhiZYN+pYQun/8gNPrUNsjlR8saZIYoTGNcKpchNp+QEPzP/JFtGqIH+Etpk11RVRWepNJdYNzG3aNLUVmYQRqgCom7kYRrbM7g7GDu/jnqzvMn65ps48GawfQA==";
        String iv = "S2r8F/Gr0aUEs7BoerD5ZQ==";

        try {
            // when: 解密手机号
            MobileNumber mobileNumber = mobileNumberDecryptingService.decryptWxMobileNumber(userId, encryptedData, iv);

            // 下面这些不应该被执行到
            assertEquals(1, 0);
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
    void should_throw_exception_given_exists_user_and_incorrect_encrypted_data_iv_by_domain_service() {
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

        String encryptedData = "NkSHcnyy88jJZwvsTBpb8Kw7jKIdYz1UnVqJ9Gf2NGFAn3DEeObh7W2Vh5hlgvs5zvtfNsV/IW+oHxEtzN4DY1E/tsGWnhiZYN+pYQun/8gNPrUNsjlR8saZIYoTGNcKpchNp+QEPzP/JFtGqIH+Etpk11RVRWepNJdYNzG3aNLUVmYQRqgCom7kYRrbM7g7GDu/jnqzvMn65ps48GawfQA==";
        String iv = "S2r8F/Gr0aUEs7BoerD5ZQ==";

        try {
            // when: 解密手机号
            MobileNumber mobileNumber = mobileNumberDecryptingService.decryptWxMobileNumber(userId, encryptedData, iv);

            // 下面这些不应该被执行到
            assertEquals(1, 0);
        }
        catch (ApplicationValidationException e) {
            assertNotNull(e);
            assertEquals(e.getErrCode(), ApplicationValidationException.INVALID_REQUEST_DATA);
        }
    }

    /**
     * 服务级测试：解密微信手机号；（组合任务，应用服务）
     * 2. 测试解密微信手机号应用服务，包括的测试用例有：
     * 2.1. 正常的已有用户、加密数据encryptedData、iv，成功解密手机号，并更新用户对象手机号
     */

    @Autowired
    AuthAppService authAppService;

    //2.1. 正常的已有用户、加密数据encryptedData、iv，成功解密手机号，并更新用户对象手机号
    @Test
    @Transactional
    @Rollback(true)
    void should_get_mobile_phone_correctly_given_exists_userid_and_correct_encrypted_data_iv() {
        // given: 正常的已有用户、加密数据encryptedData、iv
        User existsUser = User.of("深清秋", 1)
                .avatarUrl("https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKq2CRmib1mpu4hOFYtcIHgAmS7DicCEfYkUHoPmPQn74BXH5GerjoMOxIqib7iafNNBw2ZAicBj6gZGUQ/132")
                .country("中国")
                .province("江苏")
                .city("南京")
                .language("zh_CN");
        existsUser.refreshLoginToken("EfUbtTDsTz/S3lSvdkx2jg==");
        userRepository.add(existsUser);
        Long userId = existsUser.getId().value();

        SessionUser sessionUser = SessionUser.from(userId);

        String encryptedData = "NkSHcnyy8jJZwvsTBpb8Kw7jKIdYz1UnVqJ9Gf2NGFAn3DEeObh7W2Vh5hlgvs5zvtfNsV/IW+oHxEtzN4DY1E/tsGWnhiZYN+pYQun/8gNPrUNsjlR8saZIYoTGNcKpchNp+QEPzP/JFtGqIH+Etpk11RVRWepNJdYNzG3aNLUVmYQRqgCom7kYRrbM7g7GDu/jnqzvMn65ps48GawfQA==";
        String iv = "S2r8F/Gr0aUEs7BoerD5ZQ==";
        WxEncryptedUserInfo wxEncryptedUserInfo = new WxEncryptedUserInfo();
        wxEncryptedUserInfo.setEncryptedData(encryptedData);
        wxEncryptedUserInfo.setIv(iv);

        // when: 解密手机号
        MobileNumberResponse response = authAppService.decryptWxMobileNumber(sessionUser, wxEncryptedUserInfo);

        User loadedUser = userRepository.instanceOf(LongIdentity.from(userId));

        // then: 判定解密手机号是否正确
        assertNotNull(response.getMobileNumber());
        assertEquals(response.getMobileNumber(), "18652012976");
        assertEquals(loadedUser.getMobileNumber().value(), "18652012976");

    }

}
