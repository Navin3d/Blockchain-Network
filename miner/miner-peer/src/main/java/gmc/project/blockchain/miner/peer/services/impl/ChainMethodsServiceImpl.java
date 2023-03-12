package gmc.project.blockchain.miner.peer.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.peer.configurations.BlockchainConfig;
import gmc.project.blockchain.miner.peer.dao.BlockchainDao;
import gmc.project.blockchain.miner.peer.entities.BlockchainEntity;
import gmc.project.blockchain.miner.peer.models.BlockModel;
import gmc.project.blockchain.miner.peer.models.TransactionModel;
import gmc.project.blockchain.miner.peer.services.ChainMethodsService;
import gmc.project.blockchain.miner.peer.services.HashServiceFeighClient;

@Service
public class ChainMethodsServiceImpl implements ChainMethodsService {
	
	@Autowired
	private BlockchainDao blockchainDao;
	@Autowired
	private HashServiceFeighClient hashServiceFeighClient;
	@Autowired
	private BlockchainConfig blockchainConfig;

	@Override
	public BlockModel getBlockModel(String blockHash) throws JSONException {
		BlockchainEntity blockchain = blockchainDao.findById(blockchainConfig.getChainId()).block();
		Optional<String> foundBlockHash = blockchain.getBlocks().stream().filter(block -> block.equals(blockHash)).findFirst();
		if(foundBlockHash.isEmpty())
			throw new RuntimeException("Block Not Found With Hash " + blockHash);
		ResponseEntity<BlockModel> response = hashServiceFeighClient.getBlockByHash(blockHash);
		return response.getBody();
	}

	@Override
	public TransactionModel getTransaction(String transactionId) throws JSONException {
		BlockchainEntity blockchain = blockchainDao.findById(blockchainConfig.getChainId()).block();
		for(String blockHash : blockchain.getBlocks()) {
			ResponseEntity<BlockModel> response = hashServiceFeighClient.getBlockByHash(blockHash);
			BlockModel blockDecoded = response.getBody();
			Optional<TransactionModel> transactionModelOptional = blockDecoded.getTransactions().stream().filter(transaction -> {
				if(transaction.getTransactionHash().equals(transactionId)) {
					return true;
				} else {
					return false;
				}
			}).findFirst();
			if(!transactionModelOptional.isEmpty()) {
				return transactionModelOptional.get();
			}
		}
		throw new RuntimeException("Transaction with Id: " + transactionId + " not found...");
	}

	@Override
	public List<TransactionModel> getAddressData(String senderAddress) throws JSONException {
		List<TransactionModel> returnValue = new ArrayList<>();
		BlockchainEntity blockchain = blockchainDao.findById(blockchainConfig.getChainId()).block();
		for(String blockHash : blockchain.getBlocks()) {
			ResponseEntity<BlockModel> response = hashServiceFeighClient.getBlockByHash(blockHash);
			BlockModel blockDecoded = response.getBody();
			blockDecoded.getTransactions().forEach(transaction -> {
				if(transaction.getFromAddress().equals(senderAddress))
					returnValue.add(transaction);
			});
		}
		return returnValue;
	}

}
