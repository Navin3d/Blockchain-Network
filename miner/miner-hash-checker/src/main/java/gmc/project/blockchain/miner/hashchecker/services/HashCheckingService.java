package gmc.project.blockchain.miner.hashchecker.services;

import org.springframework.boot.configurationprocessor.json.JSONException;

import gmc.project.blockchain.miner.hashchecker.entities.BlockchainEntity;

public interface HashCheckingService {
	
	public Boolean validateBlolckchain(BlockchainEntity blockchainEntity) throws JSONException;

}
