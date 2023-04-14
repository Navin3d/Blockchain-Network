package gmc.project.blockchain.miner.hashgenerator.services.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	
	@Override
	public String sha256(String data) {
		String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
	}

	@Override
	public String encrypt(String strToEncrypt) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return stringToHex(Base64.getEncoder()
					.encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.ISO_8859_1))));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public String decrypt(String strToDecrypt) {
		log.error(strToDecrypt);
		strToDecrypt = hexToString(strToDecrypt);
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public JSONObject blockModelToJSONObject(BlockModel blockModel) throws JSONException {
		JSONArray transactions = new JSONArray();
		for (TransactionModel transactionModel : blockModel.getTransactions()) {
			JSONObject transJsonObject = transactionModelToJSONObject(transactionModel);
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
	
	@Override
	public JSONObject transactionModelToJSONObject(TransactionModel transactionModel) throws JSONException {
		JSONObject returnValue = new JSONObject();
		returnValue.put("order", transactionModel.getFromAddress());
		returnValue.put("fromAddress", transactionModel.getFromAddress());
		returnValue.put("toAddress", transactionModel.getToAddress());
		returnValue.put("data", transactionModel.getData());
		returnValue.put("dateTime", transactionModel.getDateTime());
		returnValue.put("previousHash", transactionModel.getPreviousHash());
		returnValue.put("transactionHash", transactionModel.getFromAddress());
		return returnValue;
	}

	@Override
	public BlockModel jsonObjectToBlockModel(JSONObject jsonObject) throws JSONException {
		List<TransactionModel> transactions = new ArrayList<>();
		JSONArray jsonArrayTransaction = jsonObject.getJSONArray("transactions");
		for (int index = 0; index < jsonArrayTransaction.length(); index++) {
			JSONObject transactionObj = jsonArrayTransaction.getJSONObject(index);
			TransactionModel transactionModel = jsonObjectToTransactionModel(transactionObj);
			transactions.add(transactionModel);
		}
		BlockModel returnValue = new BlockModel();
		returnValue.setBlockId(Integer.valueOf(jsonObject.get("blockId").toString()));
		returnValue.setNonce(Integer.valueOf(jsonObject.get("nonce").toString()));
		returnValue.setTransactions(transactions);
		returnValue.setPreviousHash(jsonObject.get("previousHash").toString());
		returnValue.setHash(jsonObject.get("hash").toString());
		return returnValue;
	}

	@Override
	public TransactionModel jsonObjectToTransactionModel(JSONObject jsonObject) throws JSONException {
		TransactionModel returnValue = new TransactionModel();
		returnValue.setOrder(Integer.valueOf(jsonObject.get("order").toString()));
		returnValue.setFromAddress(jsonObject.get("fromAddress").toString());
		returnValue.setToAddress(jsonObject.get("toAddress").toString());
		returnValue.setData(jsonObject.get("data").toString());
		returnValue.setDateTime(jsonObject.get("dateTime").toString());
		returnValue.setPreviousHash(jsonObject.get("previousHash").toString());
		returnValue.setTransactionHash(jsonObject.get("transactionHash").toString());
		return returnValue;
	}

	@Override
	public String stringToHex(String stringValue) {
		StringBuffer sb = new StringBuffer();
		char ch[] = stringValue.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			String hexString = Integer.toHexString(ch[i]);
			sb.append(hexString);
		}
		String result = sb.toString();
		return result;
	}
	
	@Override
	public String hexToString(String hexadecimalValue) {
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
		String data = decrypt(blockHash);
		JSONObject jsonObject = new JSONObject(data);
		BlockModel returnValue = jsonObjectToBlockModel(jsonObject);
		return returnValue;
	}
	
	@Override
	public TransactionModel getTransactionModelFromHash(String transactionHash) throws JSONException {
		String data = decrypt(transactionHash);
		JSONObject jsonObject = new JSONObject(data);
		TransactionModel returnValue = jsonObjectToTransactionModel(jsonObject);
		return returnValue;
	}

	@Override
	public String hashBlock(BlockModel blockModel) throws JSONException {
		JSONObject jsonObj = blockModelToJSONObject(blockModel);
		String encrypted = sha256(jsonObj.toString());
		String returnValue = encrypted;
		return returnValue;
	}

	@Override
	public String hashTransaction(TransactionModel transactionModel) throws JSONException {
		JSONObject jsonObj = transactionModelToJSONObject(transactionModel);
		String encrypted = encrypt(jsonObj.toString());
		return encrypted;
	}

}