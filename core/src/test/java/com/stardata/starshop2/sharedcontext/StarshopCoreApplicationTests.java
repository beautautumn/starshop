package com.stardata.starshop2.sharedcontext;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
class StarshopCoreApplicationTests {

    @Test
    @Transactional
    public void test1() {}
}
