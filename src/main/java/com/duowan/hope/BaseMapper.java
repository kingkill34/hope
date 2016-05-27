package com.duowan.hope;

import java.util.List;

public interface BaseMapper<T> {

	T getH(Params params);

	List<T> getListH(Params params);

	Integer insertH(T t);

	Integer insertByTnsH(Params params);

	Integer updateH(Params params);

	Integer deleteH(Params params);

	Integer countH(Params params);

	Integer countDistinctH(Params params);

	Integer batchInsertH(List<T> list);

	Integer batchInsertHByTnsH(Params params);

}
