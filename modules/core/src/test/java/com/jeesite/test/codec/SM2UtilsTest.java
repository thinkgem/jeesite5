/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.codec;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.codec.SM2Utils;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 国密 SM2 加密解密工具类，非对称加密
 * @author ThinkGem
 * @version 2026-10-22
 */
public class SM2UtilsTest {

	private static final String S = "Hello word! 你好，中文！";

	public static void main(String[] args) {
//		System.setProperty("encrypt.storeBase64", "true");

		String s = S;
		System.out.println("原文：" + s);

		String[] keys = SM2Utils.genKeys();
		System.out.println("公钥：" + keys[0]);
		PublicKey publicKey = SM2Utils.toPublicKey(keys[0]);
		System.out.println("私钥：" + keys[1]);
		PrivateKey privateKey = SM2Utils.toPrivateKey(keys[1]);

		// 一、新版 C1C3C2 密文格式加密解密
		byte[] data = SM2Utils.encode(s.getBytes(StandardCharsets.UTF_8), publicKey);
		System.out.println("新版(C1C3C2)加密数据：" + EncodeUtils.encodeBase64(data));
		byte[] data2 = SM2Utils.decode(data, privateKey);
		String dataString2 = new String(data2, StandardCharsets.UTF_8);
		System.out.println("新版(C1C3C2)解密数据：" + dataString2);
		equals(s, dataString2, "新版 C1C3C2 字节数组解密");
		equals(s, SM2Utils.decode(SM2Utils.encode(s, publicKey), privateKey), "新版 C1C3C2 字符串解密");
		checkFail(() -> SM2Utils.decodeLegacy(data, privateKey), "新版 C1C3C2 密文按旧版 C1C2C3 解密失败");

		// 二、兼容旧版解密：C1C2C3 密文格式的历史数据
		byte[] legacyData = SM2Utils.encodeLegacy(s.getBytes(StandardCharsets.UTF_8), publicKey);
		System.out.println("旧版(C1C2C3)加密数据：" + EncodeUtils.encodeBase64(legacyData));
		equals(s, new String(SM2Utils.decode(legacyData, privateKey), StandardCharsets.UTF_8),
				"旧版 C1C2C3 字节数组解密");
		equals(s, new String(SM2Utils.decodeLegacy(legacyData, privateKey), StandardCharsets.UTF_8),
				"旧版 C1C2C3 decodeLegacy 解密");
		String legacyDataString = SM2Utils.encodeLegacy(s, publicKey);
		System.out.println("旧版(C1C2C3)字符串加密数据：" + legacyDataString);
		equals(s, SM2Utils.decode(legacyDataString, privateKey), "旧版 C1C2C3 字符串解密");

		// 三、数据签名与验签
		byte[] sign = SM2Utils.sign(s.getBytes(StandardCharsets.UTF_8), privateKey);
		System.out.println("数据签名：" + EncodeUtils.encodeBase64(sign));
		check(SM2Utils.verify(s.getBytes(StandardCharsets.UTF_8), publicKey, sign), "数据验签通过");
		check(!SM2Utils.verify((s + "1").getBytes(StandardCharsets.UTF_8), publicKey, sign), "篡改数据验签失败");

		// 四、非法密文（长度不足、C1 点编码类型非法）直接报错
		checkFail(() -> SM2Utils.decode(new byte[32], privateKey), "非法长度密文解密失败");
		byte[] invalidPoint = new byte[80];
		invalidPoint[0] = (byte) 0xFF;
		checkFail(() -> SM2Utils.decode(invalidPoint, privateKey), "C1 点编码类型非法时解密失败");

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

	private static void checkFail(Runnable runnable, String name) {
		try {
			runnable.run();
		} catch (Exception e) {
			System.out.println("[OK] " + name + "：" + e.getClass().getSimpleName());
			return;
		}
		throw new RuntimeException("验证失败：" + name);
	}

}
