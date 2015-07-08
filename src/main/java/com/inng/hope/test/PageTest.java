package com.inng.hope.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageTest {

	public static void main(String[] args) {
		List page = new ArrayList();
		//for(int i = 1;i<=10;i++){
		//	page.add(i);
		//}
		int totalPage = 4;
		
		int currentPage = 3;
		if(currentPage-2>0){
			for(int i =2;i>0;i--){
				page.add(currentPage-i);
			}
			page.add(currentPage);
		}else{
			
		}
		
	}
}
