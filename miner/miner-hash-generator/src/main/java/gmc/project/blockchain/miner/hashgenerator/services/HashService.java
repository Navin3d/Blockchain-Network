package gmc.project.blockchain.miner.hashgenerator.services;

import org.springframework.boot.configurationprocessor.json.JSONException;

import gmc.project.blockchain.miner.hashgenerator.models.BlockModel;
import gmc.project.blockchain.miner.hashgenerator.models.TransactionModel;

public interface HashService {
	
	public BlockModel getBlockModelFromHash(String blockHash) throws JSONException;
	public String hashBlock(BlockModel blockModel) throws JSONException;
	
	public TransactionModel getTransactionModelFromHash(String transactionHash) throws JSONException;
	public String hashTransaction(TransactionModel transactionModel) throws JSONException;

}
