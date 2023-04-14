package gmc.project.blockchain.miner.explorer.dao;

import org.springframework.data.repository.CrudRepository;

import gmc.project.blockchain.miner.explorer.entities.BlockchainEntity;

public interface BlockchainDao extends CrudRepository<BlockchainEntity, String> {
}
