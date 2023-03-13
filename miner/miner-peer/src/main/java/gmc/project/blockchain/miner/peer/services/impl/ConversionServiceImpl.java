package gmc.project.blockchain.miner.peer.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.peer.models.TransactionModel;
import gmc.project.blockchain.miner.peer.models.BlockModel;
import gmc.project.blockchain.miner.peer.services.ConversionService;

@Service
public class ConversionServiceImpl implements ConversionService {

	@Override
	public BlockModel blockStringToBlockModel(String blockString) throws NumberFormatException, JSONException {
		JSONObject jsonObject = new JSONObject();
		List<TransactionModel> transactions = new ArrayList<>();
		JSONArray jsonArrayTransaction = jsonObject.getJSONArray("transactions");
		for (int index = 0; index < jsonArrayTransaction.length(); index++) {
			JSONObject transactionObj = jsonArrayTransaction.getJSONObject(index);
			TransactionModel transactionModel = new TransactionModel();
			transactionModel.setTransactionHash(transactionObj.get("transactionHash").toString());
			transactionModel.setFromAddress(transactionObj.get("fromAddress").toString());
			transactionModel.setToAddress(transactionObj.get("toAddress").toString());
			transactionModel.setData(transactionObj.get("data").toString());
			transactionModel.setDateTime(transactionObj.get("dateTime").toString());
			transactions.add(transactionModel);
		}

		BlockModel returnValue = new BlockModel();
		returnValue.setBlockId(Integer.valueOf(jsonObject.get("blockId").toString()));
		returnValue.setNonce(Integer.valueOf(jsonObject.get("nonce").toString()));
		returnValue.setTransactions(transactions);
		returnValue.setPreviousHash(jsonObject.get("previousHash").toString());
		return returnValue;
	}

}
