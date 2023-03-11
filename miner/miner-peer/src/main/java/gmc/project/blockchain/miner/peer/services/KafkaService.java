package gmc.project.blockchain.miner.peer.services;

import java.util.concurrent.ExecutionException;

import gmc.project.blockchain.miner.peer.models.BlockModel;
import gmc.project.blockchain.miner.peer.models.TransactionModel;

public interface KafkaService {

	public BlockModel getBlockModelFromHash(String blockHash) throws InterruptedException, ExecutionException;
	public String hashBlock(BlockModel blockModel);
	
	public TransactionModel getTransactionModelFromHash(String transactionHash);
	public String hashTransaction(TransactionModel transactionModel);
	
}
