package gmc.project.blockchain.miner.hashgenerator.services.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.hashgenerator.models.BlockModel;
import gmc.project.blockchain.miner.hashgenerator.models.KafkaModel;
import gmc.project.blockchain.miner.hashgenerator.models.TransactionModel;
import gmc.project.blockchain.miner.hashgenerator.services.ChainService;
import gmc.project.blockchain.miner.hashgenerator.services.HashService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChainServiceImpl implements ChainService {
	
	private final String ENCRYPTBLOCK = "ENCRYPTBLOCK";
	private final String ENCRYPTEDBLOCK = "ENCRYPTEDBLOCK";
	private final String BLOCKCHAIN = "BLOCKCHAIN";
	private final String ENCRYPTGENESISBLOCK = "ENCRYPTGENESISBLOCK";
	private final String GENESISBLOCK = "GENESISBLOCK";
	
	private final HashService hashService;
	private final KafkaTemplate<String, KafkaModel> kafkaTemplate;

	@Override
	public void process() {
		// TODO Auto-generated method stub
		
	}
	
	@KafkaListener(topics = ENCRYPTGENESISBLOCK, groupId = BLOCKCHAIN)
	private void hashGenesisBlock(@Payload String kafkaModel) throws JSONException {
		JSONObject jsonObject = new JSONObject(kafkaModel);
		JSONObject blockJson = jsonObject.getJSONObject("block");
		JSONObject transactionsJson = jsonObject.getJSONObject("transaction");
		
		BlockModel blockModel = hashService.jsonObjectToBlockModel(blockJson);
		TransactionModel transactionModel = hashService.jsonObjectToTransactionModel(transactionsJson);
		transactionModel.setData(hashService.encrypt(transactionModel.getData()));
		JSONObject transJsonObject = hashService.transactionModelToJSONObject(transactionModel);
		
		
		String hash = hashService.sha256(transJsonObject.toString());
		while (!hash.substring(0, 4).equals("0000")) {
			hash = hashService.sha256(hash);
		}
		transactionModel.setTransactionHash(hash);
		
		
		blockModel.getTransactions().add(transactionModel);
		String blockHash = hashService.hashBlock(blockModel);
		while (!blockHash.substring(0, 4).equals("0000")) {
			blockModel.setNonce(blockModel.getNonce() + 1);
			blockHash = hashService.sha256(hashService.hashBlock(blockModel));
		}
		blockModel.setHash(blockHash);
		
		
		KafkaModel kafkaModel2 = new KafkaModel();
		kafkaModel2.setBlock(blockModel);
		kafkaTemplate.send(GENESISBLOCK, kafkaModel2);
	}
	
	@KafkaListener(topics = ENCRYPTBLOCK, groupId = BLOCKCHAIN)
	private void hashBlock(@Payload String kafkaModel) throws JSONException {
		JSONObject jsonObject = new JSONObject(kafkaModel);
		JSONObject blockJson = jsonObject.getJSONObject("block");
		
		BlockModel blockModel = hashService.jsonObjectToBlockModel(blockJson);
		List<TransactionModel> transactions = blockModel.getTransactions();
		Collections.sort(transactions);
		
		TransactionModel transactionModel = transactions.get(transactions.size() - 1);
		blockModel.getTransactions().remove(transactionModel);
		transactionModel.setData(hashService.encrypt(transactionModel.getData()));
		JSONObject transJsonObject = hashService.transactionModelToJSONObject(transactionModel);
		
		
		String hash = hashService.sha256(transJsonObject.toString());
		while (!hash.substring(0, 4).equals("0000")) {
			hash = hashService.sha256(hash);
		}
		transactionModel.setTransactionHash(hash);
		
		
		blockModel.getTransactions().add(transactionModel);
		String blockHash = hashService.hashBlock(blockModel);
		while (!blockHash.substring(0, 4).equals("0000")) {
			blockModel.setNonce(blockModel.getNonce() + 1);
			blockHash = hashService.sha256(hashService.hashBlock(blockModel));
		}
		blockModel.setHash(blockHash);
		
		
		KafkaModel kafkaModel2 = new KafkaModel();
		kafkaModel2.setBlock(blockModel);
		kafkaTemplate.send(ENCRYPTEDBLOCK, kafkaModel2);
	}

}
