package gmc.project.blockchain.miner.peer.services;

import java.util.List;
import java.util.concurrent.ExecutionException;

import gmc.project.blockchain.miner.peer.models.PendingTransactionModel;

public interface MiningService {
	
	public List<PendingTransactionModel> getAllPendingTransactions();
	public void addTransactionToQueue(PendingTransactionModel pendingTransactionModel);
	
	public void process() throws InterruptedException, ExecutionException;

}
