package gmc.project.blockchain.miner.peer.services;

import java.util.concurrent.ExecutionException;

import gmc.project.blockchain.miner.peer.entities.BlockchainEntity;

public interface CRUDService {

	public BlockchainEntity createBlockchain(BlockchainEntity blockchainEntity) throws InterruptedException, ExecutionException ;
	
}
