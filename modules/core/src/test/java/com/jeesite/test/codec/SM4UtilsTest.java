/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.test.codec;

import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.codec.SM4Utils;

import java.nio.charset.StandardCharsets;

/**
 * 国密 SM4 加密解密工具类，对称加密
 * @author ThinkGem
 * @version 2026-10-22
 */
public class SM4UtilsTest {

	private static final String S = "Hello word! 你好，中文！";

	public static void main(String[] args) {
//		System.setProperty("encrypt.storeBase64", "true");

		String s = S;
		System.out.println("原文：" + s);

		String k = SM4Utils.genKeyString();
		System.out.println("秘钥：" + k);
		String s1 = SM4Utils.encode(s, k);
		System.out.println("加密数据：" + s1);
		String s2 = SM4Utils.decode(s1, k);
		System.out.println("解密数据：" + s2);

		byte[] key = EncodeUtils.decodeHex(k);
		byte[] iv = SM4Utils.genIV();
		byte[] data = SM4Utils.encode(s.getBytes(StandardCharsets.UTF_8), key, iv);
		System.out.println("加密byte数据：" + EncodeUtils.encodeHex(data));
		byte[] data2 = SM4Utils.decode(data, key, iv);
		System.out.println("解密byte数据：" + new String(data2, StandardCharsets.UTF_8));

		// 一、新版 GCM 增强加密：随机初始向量、密文防篡改
		String gcm1 = SM4Utils.encode(s);
		equals(s, SM4Utils.decode(gcm1), "新版 GCM 默认秘钥解密");
		equals(s, SM4Utils.decode(SM4Utils.encode(s, k), k), "新版 GCM 指定秘钥解密");
		check(!gcm1.equals(SM4Utils.encode(s)), "新版 GCM 每次加密初始向量随机");
		check(SM4Utils.encode(s).length() > SM4Utils.encodeLegacy(s).length(), "新版 GCM 密文长度大于旧版 ECB");

		byte[] gcmData = SM4Utils.encode(s.getBytes(StandardCharsets.UTF_8), key);
		check(gcmData[0] == 0x01, "新版 GCM 数据首字节为版本号");
		check(SM4Utils.isGcmData(gcmData), "新版 GCM 格式判断为真");
		byte[] tampered = gcmData.clone();
		tampered[tampered.length - 1] ^= 0x01;
		checkFail(() -> SM4Utils.decode(tampered, key), "新版 GCM 密文被篡改时解密失败");

		// 二、兼容旧版解密：ECB 无初始向量（更早期版本默认加密方式）
		String legacyEcb = SM4Utils.encodeLegacy(s);
		System.out.println("旧版(ECB)加密数据：" + legacyEcb);
		equals(s, SM4Utils.decode(legacyEcb), "旧版 ECB：默认秘钥解密");
		String legacyEcbCustom = SM4Utils.encodeLegacy(s, k);
		System.out.println("旧版(ECB)指定秘钥加密数据：" + legacyEcbCustom);
		equals(s, SM4Utils.decode(legacyEcbCustom, k), "旧版 ECB：指定秘钥解密");
		// String 接口的存储编码由 encrypt.storeBase64 决定，这里用 bytes 接口，避免实测代码依赖具体编码
		equals(s, new String(SM4Utils.decodeLegacy(SM4Utils.encodeLegacy(
				s.getBytes(StandardCharsets.UTF_8), key), key), StandardCharsets.UTF_8), "旧版 ECB：decodeLegacy 解密");

		// 三、兼容旧版解密：CBC 有初始向量（初始向量由调用方保存，不在密文中）
		byte[] legacyIv = new byte[16];
		byte[] legacyCbc = SM4Utils.encodeLegacy(s.getBytes(StandardCharsets.UTF_8), key, legacyIv);
		System.out.println("旧版(CBC 有IV)加密数据：" + EncodeUtils.encodeHex(legacyCbc));
		equals(s, new String(SM4Utils.decodeLegacy(legacyCbc, key, legacyIv), StandardCharsets.UTF_8),
				"旧版 CBC：decodeLegacy 解密");
		equals(s, new String(SM4Utils.decode(legacyCbc, key, legacyIv), StandardCharsets.UTF_8),
				"旧版 CBC：三参 decode 自动回退解密");

		// 四、旧版短密文不能用 GCM 格式判断（长度不足最小长度）
		byte[] shortLegacy = SM4Utils.encodeLegacy("abc".getBytes(StandardCharsets.UTF_8), key);
		check(!SM4Utils.isGcmData(shortLegacy), "旧版短密文判断为非 GCM 格式");
		equals("abc", new String(SM4Utils.decode(shortLegacy, key), StandardCharsets.UTF_8), "旧版短密文解密");

		// 五、未加密的明文、非法数据，解密时抛出异常
		checkFail(() -> SM4Utils.decode(s.getBytes(StandardCharsets.UTF_8), key), "未加密的明文解密失败");
		checkFail(() -> SM4Utils.decode(new byte[]{1, 2, 3}, key), "非法长度密文解密失败");

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
