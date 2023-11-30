package scau.yhq.interesting.test.mysql.transaction.service.impl;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import scau.yhq.interesting.test.mysql.transaction.Application;
import scau.yhq.interesting.test.mysql.transaction.service.ITestTransactionService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class ITestTransactionServiceImplTest {

    @Resource
    ITestTransactionService iTestTransactionService;

    @Test
    void testRepeatableReadIsolation() {
        iTestTransactionService.testRepeatableReadIsolation();;
    }
}