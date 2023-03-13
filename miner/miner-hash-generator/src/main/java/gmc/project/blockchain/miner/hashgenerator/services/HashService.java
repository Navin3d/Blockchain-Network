package gmc.project.blockchain.miner.hashgenerator.services;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import gmc.project.blockchain.miner.hashgenerator.models.BlockModel;
import gmc.project.blockchain.miner.hashgenerator.models.TransactionModel;

public interface HashService {
	
	public String encrypt(String strToEncrypt);
	public String decrypt(String strToDecrypt);
	
	public String sha256(String data);
	
	public String stringToHex(String stringValue);
	public String hexToString(String hexadecimalValue);
	
	public BlockModel getBlockModelFromHash(String blockHash) throws JSONException;
	public String hashBlock(BlockModel blockModel) throws JSONException;
	
	public TransactionModel getTransactionModelFromHash(String transactionHash) throws JSONException;
	public String hashTransaction(TransactionModel transactionModel) throws JSONException;
	
	public JSONObject blockModelToJSONObject(BlockModel blockModel) throws JSONException;
	public JSONObject transactionModelToJSONObject(TransactionModel transactionModel) throws JSONException;
	public BlockModel jsonObjectToBlockModel(JSONObject jsonObject) throws JSONException;
	public TransactionModel jsonObjectToTransactionModel(JSONObject jsonObject) throws JSONException;

}
