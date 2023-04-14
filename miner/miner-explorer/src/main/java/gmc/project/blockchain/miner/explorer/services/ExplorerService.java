package gmc.project.blockchain.miner.explorer.services;

import java.util.List;
import java.util.concurrent.ExecutionException;

import gmc.project.blockchain.miner.explorer.models.Transaction;

public interface ExplorerService {

	public List<Transaction> findTransactionsFrom(String chainId, String fromAddress) throws InterruptedException, ExecutionException ;
	public List<Transaction> findTransactionTo(String chainId, String toAddress) throws InterruptedException, ExecutionException ;
	
}
