package com.duowan.hope.test.dao;

import com.duowan.hope.mybatis.annotation.Table;
import com.duowan.hope.test.entity.TestKeyInt;

@Table(TestKeyInt.class)
public interface TestKeyIntDao extends BaseDao<TestKeyInt> {

}
