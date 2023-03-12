package gmc.project.blockchain.miner.peer.services.impl;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.peer.dao.BlockchainDao;
import gmc.project.blockchain.miner.peer.entities.BlockchainEntity;
import gmc.project.blockchain.miner.peer.services.CRUDService;
import gmc.project.blockchain.miner.peer.services.ChainService;

@Service
public class CRUDServiceImpl implements CRUDService {
	
	@Autowired
	private ChainService chainService;
	@Autowired
	private BlockchainDao blockchainDao;

	@Override
	public BlockchainEntity createBlockchain(BlockchainEntity blockchainEntity) throws InterruptedException, ExecutionException {
		BlockchainEntity saved = blockchainDao.save(blockchainEntity).toFuture().get();
		chainService.process();
		return saved;
	}

}
