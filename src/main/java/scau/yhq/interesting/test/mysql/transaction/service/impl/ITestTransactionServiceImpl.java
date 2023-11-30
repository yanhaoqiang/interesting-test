package scau.yhq.interesting.test.mysql.transaction.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import scau.yhq.interesting.test.mysql.transaction.dao.CommonDao;
import scau.yhq.interesting.test.mysql.transaction.dao.TestOneDao;
import scau.yhq.interesting.test.mysql.transaction.dao.TestTwoDao;
import scau.yhq.interesting.test.mysql.transaction.service.ITestTransactionService;

@Slf4j
@Service
public class ITestTransactionServiceImpl implements ITestTransactionService {

    @Resource
    private CommonDao commonDao;

    @Resource
    private TestOneDao testOneDao;

    @Resource
    private TestTwoDao testTwoDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void testRepeatableReadIsolation() {
        log.info("test");

        log.info("all test_one:{}", testOneDao.selectAll());
        log.info("all test_two:{}", testTwoDao.selectAll());
        log.info("all tables:{}", commonDao.getAllTable());

        testOneDao.insert("1");
        testTwoDao.insert("1");

        log.info("--------");
        log.info("all tables:{}", commonDao.getAllTable());
        log.info("all test_one:{}", testOneDao.selectAll());
        log.info("all test_two:{}", testTwoDao.selectAll());
    }
}
