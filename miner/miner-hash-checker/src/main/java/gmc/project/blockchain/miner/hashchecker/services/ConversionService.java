package gmc.project.blockchain.miner.hashchecker.services;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import gmc.project.blockchain.miner.hashchecker.entities.BlockchainEntity;
import gmc.project.blockchain.miner.hashchecker.models.BlockModel;
import gmc.project.blockchain.miner.hashchecker.models.TransactionModel;

public interface ConversionService {
	
	public BlockchainEntity stringToBlockchainEntity(String kafkaModel) throws JSONException;
	
	public JSONObject blockModelToJSONObject(BlockModel blockModel) throws JSONException;
	public JSONObject transactionModelToJSONObject(TransactionModel transactionModel) throws JSONException;
	
	public String hashBlock(BlockModel blockModel) throws JSONException;
	public String hashTransaction(TransactionModel transactionModel) throws JSONException;

}
