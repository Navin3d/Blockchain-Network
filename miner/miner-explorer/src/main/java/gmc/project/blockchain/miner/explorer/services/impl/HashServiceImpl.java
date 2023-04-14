package gmc.project.blockchain.miner.explorer.services.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.explorer.services.HashService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HashServiceImpl implements HashService {

	private final Environment env;

	private SecretKeySpec secretKey;
	private byte[] key;

	public HashServiceImpl(Environment environment) {
		this.env = environment;
		MessageDigest sha = null;
		try {
			key = env.getProperty("blockchain.hash-secret").getBytes(StandardCharsets.ISO_8859_1);
			sha = MessageDigest.getInstance("SHA-256");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private String hexToString(String hexadecimalValue) {
		String result = new String();
		char[] charArray = hexadecimalValue.toCharArray();
		for (int i = 0; i < charArray.length; i = i + 2) {
			String st = "" + charArray[i] + "" + charArray[i + 1];
			char ch = (char) Integer.parseInt(st, 16);
			result = result + ch;
		}
		return result;
	}

	@Override
	public String getDataFromHash(String hashedData) {
		log.error(hashedData);
		hashedData = hexToString(hashedData);
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			String decrypted = new String(cipher.doFinal(Base64.getDecoder().decode(hashedData)));
			log.error(decrypted);
			return decrypted;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
