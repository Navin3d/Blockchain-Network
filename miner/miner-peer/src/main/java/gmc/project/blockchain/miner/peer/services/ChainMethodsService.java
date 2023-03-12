package gmc.project.blockchain.miner.peer.services;

import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONException;

import gmc.project.blockchain.miner.peer.models.BlockModel;
import gmc.project.blockchain.miner.peer.models.TransactionModel;

public interface ChainMethodsService {
	
	public BlockModel getBlockModel(String blockHash) throws JSONException;	
	public TransactionModel getTransaction(String transactionId) throws JSONException;	
	public List<TransactionModel> getAddressData(String senderAddress) throws JSONException;

}
