package gmc.project.blockchain.miner.explorer.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.explorer.dao.BlockchainDao;
import gmc.project.blockchain.miner.explorer.entities.BlockchainEntity;
import gmc.project.blockchain.miner.explorer.models.BlockModel;
import gmc.project.blockchain.miner.explorer.models.Transaction;
import gmc.project.blockchain.miner.explorer.models.TransactionModel;
import gmc.project.blockchain.miner.explorer.services.ExplorerService;
import gmc.project.blockchain.miner.explorer.services.HashService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExplorerServiceImpl implements ExplorerService {
	
	private final BlockchainDao blockchainDao;
	private final HashService hashService;
	
	private BlockchainEntity findChain(String chainId) {
		BlockchainEntity foundChain = blockchainDao.findById(chainId).orElse(null);
		if(foundChain == null) 
			throw new RuntimeException("Chain not found " + chainId);
		return foundChain;
	}
	
	private Transaction convertToTransaction(TransactionModel transactionModel) {
		Transaction returnValue = new Transaction();
		returnValue.setChainId("92");
		returnValue.setHash(transactionModel.getTransactionHash().substring(4, transactionModel.getTransactionHash().length()));
		returnValue.setData(hashService.getDataFromHash(transactionModel.getData()));
		returnValue.setDateTime(transactionModel.getDateTime());
		return returnValue;
	}

	@Override
	public List<Transaction> findTransactionsFrom(String chainId, String fromAddress) {
		List<Transaction> returnValue = new ArrayList<>();
		BlockchainEntity foundChain = findChain(chainId);
				
		for(BlockModel block: foundChain.getBlocks())
			for(TransactionModel tx: block.getTransactions())
				if(tx.getFromAddress().equals(fromAddress)) 
					returnValue.add(convertToTransaction(tx));
		
		return returnValue;
	}

	@Override
	public List<Transaction> findTransactionTo(String chainId, String toAddress) {
		List<Transaction> returnValue = new ArrayList<>();
		BlockchainEntity foundChain = findChain(chainId);
				
		for(BlockModel block: foundChain.getBlocks())
			for(TransactionModel tx: block.getTransactions())
				if(tx.getToAddress().equals(toAddress)) 
					returnValue.add(convertToTransaction(tx));
		
		return returnValue;
	}

}
