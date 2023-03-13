package gmc.project.blockchain.miner.peer.services;

import org.springframework.boot.configurationprocessor.json.JSONException;

import gmc.project.blockchain.miner.peer.models.BlockModel;

public interface ConversionService {
	public BlockModel blockStringToBlockModel(String blockString) throws NumberFormatException, JSONException;
}
