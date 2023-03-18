package gmc.project.blockchain.miner.peer.services;

import java.util.concurrent.ExecutionException;

import gmc.project.blockchain.miner.peer.entities.BlockchainEntity;
import gmc.project.blockchain.miner.peer.models.BlockModel;

public interface CRUDService {
	
	public BlockchainEntity getBlockchain() throws InterruptedException, ExecutionException;
	
	public BlockchainEntity createBlockchain(BlockchainEntity blockchainEntity) throws InterruptedException, ExecutionException;
	
	public BlockModel getBlockByHash(String hash) throws InterruptedException, ExecutionException;
	public void initiateMiningGenesisBlock() throws InterruptedException, ExecutionException;
	
	public BlockchainEntity save(BlockchainEntity blockchainEntity) throws InterruptedException, ExecutionException;
}
