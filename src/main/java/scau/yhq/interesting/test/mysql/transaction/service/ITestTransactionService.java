package scau.yhq.interesting.test.mysql.transaction.service;

public interface ITestTransactionService {

    /**
     * test the repeatable read isolation in mysql transaction
     */
    void testRepeatableReadIsolation();
}
