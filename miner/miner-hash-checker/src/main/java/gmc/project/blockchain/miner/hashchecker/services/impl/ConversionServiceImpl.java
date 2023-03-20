package gmc.project.blockchain.miner.hashchecker.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.hashchecker.entities.BlockchainEntity;
import gmc.project.blockchain.miner.hashchecker.services.ConversionService;
import gmc.project.blockchain.miner.hashchecker.models.BlockModel;
import gmc.project.blockchain.miner.hashchecker.models.TransactionModel;

@Service
public class ConversionServiceImpl implements ConversionService {

	@Override
	public String hashBlock(BlockModel blockModel) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String hashTransaction(TransactionModel transactionModel) throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public JSONObject blockModelToJSONObject(BlockModel blockModel) throws JSONException {
		JSONArray transactions = new JSONArray();
		for (TransactionModel transactionModel : blockModel.getTransactions()) {
			JSONObject transJsonObject = transactionModelToJSONObject(transactionModel);
			transactions.put(transJsonObject);
		}
		JSONObject returnValue = new JSONObject();
		returnValue.put("blockId", blockModel.getBlockId());
		returnValue.put("nonce", blockModel.getNonce());
		returnValue.put("transactions", transactions);
		returnValue.put("previousHash", blockModel.getPreviousHash());
		returnValue.put("hash", blockModel.getHash());
		return returnValue;
	}
	
	@Override
	public JSONObject transactionModelToJSONObject(TransactionModel transactionModel) throws JSONException {
		JSONObject returnValue = new JSONObject();
		returnValue.put("order", transactionModel.getFromAddress());
		returnValue.put("fromAddress", transactionModel.getFromAddress());
		returnValue.put("toAddress", transactionModel.getToAddress());
		returnValue.put("data", transactionModel.getData());
		returnValue.put("dateTime", transactionModel.getDateTime());
		returnValue.put("previousHash", transactionModel.getPreviousHash());
		returnValue.put("transactionHash", transactionModel.getFromAddress());
		return returnValue;
	}
	
	@Override
	public BlockchainEntity stringToBlockchainEntity(String kafkaModel) throws JSONException {
		JSONObject kafkaModelObj = new JSONObject(kafkaModel);
		JSONObject blockchainObj = kafkaModelObj.getJSONObject("blockchainEntity");
		JSONArray blocksJsonArr = blockchainObj.getJSONArray("blocks");
		
		List<BlockModel> blocks = new ArrayList<>();
		for(int index = 0; index < blocksJsonArr.length(); index++) {
			JSONObject currentBlockJSON = blocksJsonArr.getJSONObject(index);
			BlockModel block = jsonObjectToBlockModel(currentBlockJSON);
			blocks.add(block);
		}
		
		BlockchainEntity returnValue = new BlockchainEntity();
		returnValue.setId(blockchainObj.getString("id"));
		returnValue.setTitle(blockchainObj.getString("title"));
		returnValue.setDescription(blockchainObj.getString("description"));
		returnValue.setOrganization(blockchainObj.getString("organization"));
		returnValue.setCoinName(blockchainObj.getString("coinName"));
		returnValue.setCoinSymbol(blockchainObj.getString("coinSymbol"));
		returnValue.setBlocks(blocks);
		return returnValue;
	}
	
	private BlockModel jsonObjectToBlockModel(JSONObject jsonObject) throws JSONException {
		List<TransactionModel> transactions = new ArrayList<>();
		JSONArray jsonArrayTransaction = jsonObject.getJSONArray("transactions");
		for (int index = 0; index < jsonArrayTransaction.length(); index++) {
			JSONObject transactionObj = jsonArrayTransaction.getJSONObject(index);
			TransactionModel transactionModel = jsonObjectToTransactionModel(transactionObj);
			transactions.add(transactionModel);
		}
		BlockModel returnValue = new BlockModel();
		returnValue.setBlockId(Integer.valueOf(jsonObject.get("blockId").toString()));
		returnValue.setNonce(Integer.valueOf(jsonObject.get("nonce").toString()));
		returnValue.setTransactions(transactions);
		returnValue.setPreviousHash(jsonObject.get("previousHash").toString());
		returnValue.setHash(jsonObject.get("hash").toString());
		return returnValue;
	}

	private TransactionModel jsonObjectToTransactionModel(JSONObject jsonObject) throws JSONException {
		TransactionModel returnValue = new TransactionModel();
		returnValue.setOrder(Integer.valueOf(jsonObject.get("order").toString()));
		returnValue.setFromAddress(jsonObject.get("fromAddress").toString());
		returnValue.setToAddress(jsonObject.get("toAddress").toString());
		returnValue.setData(jsonObject.get("data").toString());
		returnValue.setDateTime(jsonObject.get("dateTime").toString());
		returnValue.setPreviousHash(jsonObject.get("previousHash").toString());
		returnValue.setTransactionHash(jsonObject.get("transactionHash").toString());
		return returnValue;
	}

}
