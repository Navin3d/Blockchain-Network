package gmc.project.blockchain.miner.peer.services;

import org.json.JSONException;

import gmc.project.blockchain.miner.peer.models.BlockModel;

public interface ConversionService {
	public String stringToHex(String strValue);
	public BlockModel blockStringToBlockModel(String blockString) throws NumberFormatException, JSONException;
}
