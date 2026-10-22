/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.codec;

import com.jeesite.common.codec.SM3Utils;

/**
 * 国密 SM3 加密工具类，散列加密，不可逆加密
 * @author ThinkGem
 * @version 2026-10-22
 */
public class SM3UtilsTest {

	public static final int HASH_ITERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	private static final String S = "Hello word! 你好，中文！";

	public static void main(String[] args) {

		String s = S;
		System.out.println("原文：" + s);

		String data = SM3Utils.sm3(s);
		System.out.println("密文：" + data);

		String salt = SM3Utils.genSaltString(SALT_SIZE);
		System.out.println("加盐：" + salt);
		String data2 = SM3Utils.sm3(s, salt, HASH_ITERATIONS);
		System.out.println("带盐密文：" + data2);

		// 一、标准测试向量：SM3("abc") = 66c7f0f4...
		equals("66c7f0f462eeedd9d1f2d46bdc10e4e24167c4875cf2f7a2297da02b8f4ba8e0",
				SM3Utils.sm3("abc"), "SM3(abc) 标准向量");
		check(data.length() == 64, "SM3 输出长度=64");

		// 二、加盐散列、加盐迭代散列（存量口令可继续校验，算法保持不变）
		check(SM3Utils.sm3(s, salt).length() == 64, "加盐散列输出长度=64");
		check(!SM3Utils.sm3(s, salt).equals(data2), "加盐散列与加盐迭代散列结果不同");

		// 三、HMAC-SM3 为标准消息认证码，结果稳定且与加盐散列不同
		String hmac = SM3Utils.hmacSm3(s, salt);
		System.out.println("带key密文：" + hmac);
		check(hmac.length() == 64, "HMAC-SM3 输出长度=64");
		equals(hmac, SM3Utils.hmacSm3(s, salt), "HMAC-SM3 结果稳定");
		check(!hmac.equals(SM3Utils.sm3(s, salt)), "HMAC-SM3 与加盐散列不同");

		System.out.println("全部验证通过 ^_^");
	}

	private static void equals(String expected, String actual, String name) {
		check(expected.equals(actual), name + "：" + actual);
	}

	private static void check(boolean result, String name) {
		if (!result) {
			throw new RuntimeException("验证失败：" + name);
		}
		System.out.println("[OK] " + name);
	}

}
