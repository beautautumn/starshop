package com.stardata.starshop2.sharedcontext;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StarshopCoreApplicationTests {

    @Test
    @Transactional
    public void test1() {}
}
