package com.duowan.hope.mybatis.page;

import java.util.ArrayList;
import java.util.List;

public class HopePage<E> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer pageSize;

	private Integer pageNo;

	private Integer totalCount;

	private Integer totalPage;

	public List<E> getData() {
		return this;
	}

	public void setData(List<E> data) {
		this.addAll(data);
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

}
