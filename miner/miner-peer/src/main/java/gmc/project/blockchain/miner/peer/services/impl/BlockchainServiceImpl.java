package gmc.project.blockchain.miner.peer.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.peer.configurations.BlockchainConfig;
import gmc.project.blockchain.miner.peer.dao.BlockchainDao;
import gmc.project.blockchain.miner.peer.entities.BlockchainEntity;
import gmc.project.blockchain.miner.peer.models.BlockModel;
import gmc.project.blockchain.miner.peer.models.TransactionModel;
import gmc.project.blockchain.miner.peer.services.BlockchainService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class BlockchainServiceImpl implements BlockchainService {
	
	@Autowired
	private BlockchainDao blockchainDao;
	
	@Autowired
	private BlockchainConfig blockchainConfig;

	@Override
	public BlockModel getBlockModel(String blockHash) {
		BlockchainEntity blockchain = blockchainDao.findById(blockchainConfig.getChainId()).block();
		Optional<String> foundBlockHash = blockchain.getBlocks().stream().filter(block -> block.equals(blockHash)).findFirst();
		if(foundBlockHash.isEmpty())
			throw new RuntimeException("Block Not Found With Hash " + blockHash);
		
		return null;
	}

	@Override
	public TransactionModel getTransaction(String transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionModel getAddressData(String senderAddress) {
		// TODO Auto-generated method stub
		return null;
	}

}
