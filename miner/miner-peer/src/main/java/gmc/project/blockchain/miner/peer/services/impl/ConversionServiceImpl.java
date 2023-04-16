package gmc.project.blockchain.miner.peer.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.peer.models.TransactionModel;
import gmc.project.blockchain.miner.peer.models.BlockModel;
import gmc.project.blockchain.miner.peer.services.ConversionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConversionServiceImpl implements ConversionService {
	
	@Override
	public String stringToHex(String strValue) {
		StringBuffer sb = new StringBuffer();
		char ch[] = strValue.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			String hexString = Integer.toHexString(ch[i]);
			sb.append(hexString);
		}
		String result = sb.toString();
		return result;
	}

	@Override
	public BlockModel blockStringToBlockModel(String kafkaModelString) throws NumberFormatException, JSONException {
		JSONObject kafkaModelJson = new JSONObject(kafkaModelString);
		JSONObject jsonObject = kafkaModelJson.getJSONObject("block");
		log.error(jsonObject.toString());
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
