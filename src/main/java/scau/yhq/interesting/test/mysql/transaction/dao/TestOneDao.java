package scau.yhq.interesting.test.mysql.transaction.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestOneDao {
    List<String> selectAll();

    void insert(@Param("temp") String temp);
}
