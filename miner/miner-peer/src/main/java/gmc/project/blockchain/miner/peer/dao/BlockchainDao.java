package gmc.project.blockchain.miner.peer.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import gmc.project.blockchain.miner.peer.entities.BlockchainEntity;

public interface BlockchainDao extends ReactiveMongoRepository<BlockchainEntity, String> {
	
}
