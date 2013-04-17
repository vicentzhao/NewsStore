package com.ccdrive.moviestore.suffix;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtil {

	private static final CryptUtil instance = new CryptUtil();
	private static String defaultkey = "#$d*s|33z3e4)";
    public static final String queyflag="html2";
	private CryptUtil() {

	}

	public static CryptUtil getInstance() {
		return instance;
	}

	private Key initKeyForAES(String key) throws NoSuchAlgorithmException {
		if (null == key || key.length() == 0) {
			throw new NullPointerException("key not is null");
		}
		SecretKeySpec key2 = null;
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(key.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			key2 = new SecretKeySpec(enCodeFormat, "AES");
		} catch (NoSuchAlgorithmException ex) {
			throw new NoSuchAlgorithmException();
		}
		return key2;

	}

	// MD5鍔犵爜銆�2浣�
	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return "";
		}

		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}

		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * AES鍔犲瘑绠楁硶锛屼笉鍙楀瘑閽ラ暱搴﹂檺鍒�
	 * 
	 * @param content
	 * @param key
	 * @return
	 */
	public String encryptAES(String content, String key) {
		try {
			SecretKeySpec secretKey = (SecretKeySpec) initKeyForAES(key);
			Cipher cipher = Cipher.getInstance("AES");// 鍒涘缓瀵嗙爜鍣�
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);// 鍒濆鍖�
			byte[] result = cipher.doFinal(byteContent);

			return asHex(result); // 鍔犲瘑
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String encryptAES(String content) {
		  
		return this.encryptAES(content, this.defaultkey);
	}

	public String decryptAES(String content) {
		return this.decryptAES(content, this.defaultkey);
	}

	// 鍔犲瘑url
	public String encryptURL(String url) {
		 
		return StrBinaryTurn.StrToHex(url);
		 //return UTF8Encode(url);
		//return this.encryptAES(url, defaultkey);
	}

	// 瑙ｅ瘑url
	public static String decryptURL(String url) {	 
		return StrBinaryTurn.HexToStr(url);
		 //return UTF8DEcode(url);
		//return this.decryptAES(url, defaultkey);
	}
	
	public String decryptQuery(String url){
		
		return decryptURL(url);
	}

	public String encryptQuery(String url){
		String temp="";
		if(url.contains("convert=false"))return url;
		if(url.contains(".action?")){
			temp=url.substring(url.indexOf(".action")+8);
			
			url=url.substring(0,url.indexOf(".action"));
			url=url+".action?qs1="+encryptURL(temp);
		}	
		return url;
	}
	
	/**
	 * aes瑙ｅ瘑绠楁硶锛屼笉鍙楀瘑閽ラ暱搴﹂檺鍒�
	 * 
	 * @param content
	 * @param key
	 * @return
	 */
	public String decryptAES(String content, String key) {

		try {
			SecretKeySpec secretKey = (SecretKeySpec) initKeyForAES(key);
			Cipher cipher = Cipher.getInstance("AES");// 鍒涘缓瀵嗙爜鍣�
			cipher.init(Cipher.DECRYPT_MODE, secretKey);// 鍒濆鍖�
			byte[] result = cipher.doFinal(asBytes(content));

			return new String(result); // 瑙ｅ瘑
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 灏�杩涘埗鏁板�杞崲涓�6杩涘埗瀛楃涓�
	 * 
	 * @param buf
	 * @return
	 */
	public String asHex(byte buf[]) {

		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;
		for (i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10)
				strbuf.append("0");
			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}
		return UTF8Encode(strbuf.toString());
	}

	public String UTF8Encode(String str) {
		
		try {
			return URLEncoder.encode(str,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String UTF8DEcode(String str) {
		try {
			return URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 灏�6杩涘埗杞崲
	 * 
	 * @param hexStr
	 * @return
	 */
	public byte[] asBytes(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		hexStr=UTF8DEcode(hexStr);
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}


	public static void main(String[] args) {
		
		StrBinaryTurn s = new StrBinaryTurn();
		
	
		System.out.print(s.BinstrToStr((s.StrToBinstr("鎬婚棶  "))));
	

		System.out.print("%%%%");
//		CryptUtil crypt = CryptUtil.getInstance();
//		String content = "涓枃";
//		System.out.println(crypt.encryptAES(content));
//		String dcontent = crypt.encryptAES(content);
//		dcontent = "c5daea3a849ba31cddf0553759224438246e399d222dfcac41578ccdc1456d7793cc07c392fc78e303b4efe95c106b5202742deb43f88a7185ccdf02bba0b187ec78a9e3a2b3a16b3838a18c0914d1f7";
//		System.out.println(crypt.decryptAES(dcontent));
//		System.out.print("###########");
	}
}