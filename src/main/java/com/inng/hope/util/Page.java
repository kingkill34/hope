package com.inng.hope.util;

import java.util.ArrayList;
import java.util.List;

public class Page {

	private int choosePage;

	private int pageSize;

	private int total;

	private int pageTotal;

	private int navigateTotal;

	private List<Integer> showNums;

	private Integer lastPage;

	private Integer nextPage;

	public Page() {

	}

	/**
	 * 
	 * @param choosePage
	 *            当前页
	 * @param pageSize
	 *            每一页的记录数
	 * @param total
	 *            总共的记录数
	 * @param navigateTotal
	 *            导航页的记录数
	 * @throws NumberInputException
	 * @throws OddEvenException
	 */
	public Page(int choosePage, int pageSize, int total, int navigateTotal)
			throws Exception {
		this.choosePage = choosePage;
		this.pageSize = pageSize;
		this.total = total;
		int result = total / pageSize;
		if (total % pageSize > 0) {
			result += 1;
		}
		this.pageTotal = result;
		this.navigateTotal = navigateTotal;

		this.lastPage = choosePage == 1 ? 0 : choosePage - 1;
		this.nextPage = pageTotal == choosePage?0:choosePage+1;
		
		this.pageTotal = pageTotal<this.navigateTotal?pageTotal:navigateTotal;

		if (this.choosePage <= 0) {
			throw new Exception("参数错误： choosePage 必须为大于0的整数,而输入的为" + choosePage);
		}
		if (this.pageSize <= 0) {
			throw new Exception("参数错误： pageSize 必须为大于0的整数,而输入的为" + pageSize);
		}
		if (this.total <= 0) {
			throw new Exception("参数错误： total 必须为大于0的整数,而输入的为" + total);
		}
		if (this.navigateTotal <= 0) {
			throw new Exception("参数错误： navigateTotal 必须为大于0的整数,而输入的为"
					+ navigateTotal);
		}
		if (this.navigateTotal % 2 == 0) {
			throw new Exception("参数错误： navigateTotal 只能为奇数,而输入的为"
					+ navigateTotal);
		}
		
	}

	public int getChoosePage() {
		return choosePage;
	}

	public void setChoosePage(int choosePage) {
		this.choosePage = choosePage;
		this.lastPage = choosePage == 1 ? 0 : choosePage - 1;
		this.nextPage = pageTotal == choosePage?0:choosePage+1;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public int getNavigateTotal() {
		return navigateTotal;
	}

	public void setNavigateTotal(int navigateTotal) {
		this.navigateTotal = navigateTotal;
	}

	public List<Integer> getShowNums() {
		showNums = new ArrayList<Integer>();
		if (choosePage <= (navigateTotal + 1) / 2) {
			for (int i = 1; i <= navigateTotal; i++) {
				showNums.add(i);
			}
		} else if (choosePage >= (pageTotal - navigateTotal / 2 + 1)) {
			if (pageTotal - navigateTotal + 1 < 1) {

			}
			for (int i = pageTotal - navigateTotal + 1; i <= pageTotal; i++) {
				showNums.add(i);
			}
		} else {
			for (int i = choosePage - navigateTotal / 2; i <= choosePage
					+ navigateTotal / 2; i++) {
				showNums.add(i);
			}
		}
		return showNums;
	}

	public Integer getLastPage() {
		return lastPage;
	}

	public void setLastPage(Integer lastPage) {
		this.lastPage = lastPage;
	}

	public Integer getNextPage() {
		return nextPage;
	}

	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}
	
	
}
