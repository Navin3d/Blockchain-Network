package gmc.project.blockchain.miner.hashgenerator.services.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.hashgenerator.models.BlockModel;
import gmc.project.blockchain.miner.hashgenerator.models.TransactionModel;
import gmc.project.blockchain.miner.hashgenerator.services.HashService;

@Service
public class HashServiceImpl implements HashService {

	private final Environment env;
	
	private SecretKeySpec secretKey;
	private byte[] key;

	public HashServiceImpl(Environment environment) {
		this.env = environment;
		MessageDigest sha = null;
		try {
			key = env.getProperty("hashing.secret").getBytes(StandardCharsets.ISO_8859_1);
			sha = MessageDigest.getInstance("SHA-256");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private String encrypt(String strToEncrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder()
					.encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.ISO_8859_1)));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private String decrypt(String strToDecrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private JSONObject blockModelToJSONObject(BlockModel blockModel) throws JSONException {
		JSONArray transactions = new JSONArray();
		for (TransactionModel transactionModel : blockModel.getTransactions()) {
			JSONObject transJsonObject = new JSONObject();
			transJsonObject.put("transactionHash", transactionModel.getTransactionHash());
			transJsonObject.put("fromAddress", transactionModel.getFromAddress());
			transJsonObject.put("toAddress", transactionModel.getToAddress());
			transJsonObject.put("data", transactionModel.getData());
			transJsonObject.put("dateTime", transactionModel.getDateTime());
			transactions.put(transJsonObject);
		}
		JSONObject returnValue = new JSONObject();
		returnValue.put("blockId", blockModel.getBlockId());
		returnValue.put("nonce", blockModel.getNonce());
		returnValue.put("transactions", transactions);
		returnValue.put("previousHash", blockModel.getPreviousHash());
		returnValue.put("hash", blockModel.getHash());
		return returnValue;
	}
	
	private JSONObject transactionModelToJSONObject(TransactionModel transactionModel) throws JSONException {
		JSONObject returnValue = new JSONObject();
		returnValue.put("transactionHash", transactionModel.getTransactionHash());
		returnValue.put("fromAddress", transactionModel.getFromAddress());
		returnValue.put("toAddress", transactionModel.getToAddress());
		returnValue.put("data", transactionModel.getData());
		returnValue.put("dateTime", transactionModel.getDateTime());
		return returnValue;
	}

	private BlockModel jsonObjectToBlockModel(JSONObject jsonObject) throws JSONException {
		List<TransactionModel> transactions = new ArrayList<>();
		JSONArray jsonArrayTransaction = jsonObject.getJSONArray("transactions");
		for (int index = 0; index < jsonArrayTransaction.length(); index++) {
			JSONObject transactionObj = jsonArrayTransaction.getJSONObject(index);
			TransactionModel transactionModel = new TransactionModel();
			transactionModel.setTransactionHash(transactionObj.get("transactionHash").toString());
			transactionModel.setFromAddress(transactionObj.get("fromAddress").toString());
			transactionModel.setToAddress(transactionObj.get("toAddress").toString());
			transactionModel.setData(transactionObj.get("data").toString());
			transactionModel.setDateTime(LocalDateTime.parse(transactionObj.get("dateTime").toString()));
			transactions.add(transactionModel);
		}

		BlockModel returnValue = new BlockModel();
		returnValue.setBlockId(jsonObject.get("blockId").toString());
		returnValue.setNonce(Integer.valueOf(jsonObject.get("nonce").toString()));
		returnValue.setTransactions(transactions);
		returnValue.setPreviousHash(jsonObject.get("previousHash").toString());
		returnValue.setHash(jsonObject.get("hash").toString());
		return returnValue;
	}

	private TransactionModel jsonObjectToTransactionModel(JSONObject jsonObject) throws JSONException {
		TransactionModel returnValue = new TransactionModel();
		returnValue.setTransactionHash(jsonObject.get("transactionHash").toString());
		returnValue.setFromAddress(jsonObject.get("fromAddress").toString());
		returnValue.setToAddress(jsonObject.get("toAddress").toString());
		returnValue.setData(jsonObject.get("data").toString());
		returnValue.setDateTime(LocalDateTime.parse(jsonObject.get("dateTime").toString()));
		return returnValue;
	}

	private String stringToHex(String stringValue) {
		StringBuffer sb = new StringBuffer();
		char ch[] = stringValue.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			String hexString = Integer.toHexString(ch[i]);
			sb.append(hexString);
		}
		String result = sb.toString();
		return "0x" + result;
	}
	
	private String hexToString(String hexadecimalValue) {
		hexadecimalValue = hexadecimalValue.substring(2, hexadecimalValue.length());
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
	public BlockModel getBlockModelFromHash(String blockHash) throws JSONException {
		String data = decrypt(hexToString(blockHash));
		JSONObject jsonObject = new JSONObject(data);
		BlockModel returnValue = jsonObjectToBlockModel(jsonObject);
		return returnValue;
	}
	
	@Override
	public TransactionModel getTransactionModelFromHash(String transactionHash) throws JSONException {
		String data = decrypt(hexToString(transactionHash));
		JSONObject jsonObject = new JSONObject(data);
		TransactionModel returnValue = jsonObjectToTransactionModel(jsonObject);
		return returnValue;
	}

	@Override
	public String hashBlock(BlockModel blockModel) throws JSONException {
		JSONObject jsonObj = blockModelToJSONObject(blockModel);
		String encrypted = encrypt(jsonObj.toString());
		String returnValue = stringToHex(encrypted);
		return returnValue;
	}

	@Override
	public String hashTransaction(TransactionModel transactionModel) throws JSONException {
		transactionModel.setTransactionHash(stringToHex(transactionModel.getTransactionHash()));
		JSONObject jsonObj = transactionModelToJSONObject(transactionModel);
		String encrypted = encrypt(jsonObj.toString());
		String returnValue = stringToHex(encrypted);
		return returnValue;
	}

}
