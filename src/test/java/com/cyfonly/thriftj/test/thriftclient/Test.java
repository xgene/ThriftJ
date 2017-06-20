package com.cyfonly.thriftj.test.thriftclient;

public class Test {

	public static void main(String[] args) {

		int all =0;
		for (int i = 1; i <=4000; i++) {
			all += (((i % 2 == 0)?1:-1) * i);
		}
		System.out.println(all);
	}

}
