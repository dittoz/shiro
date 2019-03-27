package com.zhouyee.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;
/**
 * 散列算法 md5 sha
 * md5算法可以被穷举算法破解(一个个遍历hash串找到原始密码)
 * 解决，在原始密码上加salt（通常是随机数）
 * @author admin
 *
 */
public class ShanLieCodeTest {
	@Test
	public void test(){
		//第一个参数是原始密码，第二个参数是盐，第三个参数是hash次数，hash两次相当于md5(md5)
		//可以散列多次,安全性高
		Md5Hash md5 = new Md5Hash("zhouyi", "1993<>?", 1);
		System.out.println(md5.toString());
		//第一个参数是散列的算法
		SimpleHash simpleHash = new SimpleHash("md5", "zhouyi", "1993<>?", 1);
		System.out.println(simpleHash.toString());
		
	}
}
