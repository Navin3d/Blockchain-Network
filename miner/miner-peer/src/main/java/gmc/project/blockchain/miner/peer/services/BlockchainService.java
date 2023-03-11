package gmc.project.blockchain.miner.peer.services;

import gmc.project.blockchain.miner.peer.models.BlockModel;
import gmc.project.blockchain.miner.peer.models.TransactionModel;

public interface BlockchainService {
	
	public BlockModel getBlockModel(String blockHash);	
	public TransactionModel getTransaction(String transactionId);	
	public TransactionModel getAddressData(String senderAddress);

}
