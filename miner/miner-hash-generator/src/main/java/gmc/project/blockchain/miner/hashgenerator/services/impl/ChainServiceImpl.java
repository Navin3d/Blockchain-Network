package gmc.project.blockchain.miner.hashgenerator.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.hashgenerator.configurations.BlockchainConfig;
import gmc.project.blockchain.miner.hashgenerator.models.BlockModel;
import gmc.project.blockchain.miner.hashgenerator.models.KafkaModel;
import gmc.project.blockchain.miner.hashgenerator.models.TransactionModel;
import gmc.project.blockchain.miner.hashgenerator.services.ChainService;
import gmc.project.blockchain.miner.hashgenerator.services.HashService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChainServiceImpl implements ChainService {
	
	private final String ENCRYPT = "ENCRYPT";
	private final String BLOCKCHAIN = "BLOCKCHAIN";
	private final String GENESISBLOCK = "GENESISBLOCK";
	
	private final HashService hashService;
	private final BlockchainConfig blockchainConfig;
	private final KafkaTemplate<String, KafkaModel> kafkaTemplate;

	@Override
	public void process() {
		// TODO Auto-generated method stub
		
	}
	
	private List<BlockModel> getBlockModel(List<String> chainHashList) throws JSONException {
		List<BlockModel> returnValue = new ArrayList<>();
		for(String chainHash : chainHashList) {
			BlockModel blockModel = hashService.getBlockModelFromHash(chainHash);
			returnValue.add(blockModel);
		}
		Collections.sort(returnValue);
		return returnValue;
	}
	
	@KafkaListener(topics = ENCRYPT, groupId = BLOCKCHAIN)
	private void hashBlock(@Payload String kafkaModel) throws JSONException {
		log.error(kafkaModel);
		JSONObject jsonObject = new JSONObject(kafkaModel);
		JSONObject blockJson = jsonObject.getJSONObject("block");
		JSONObject transactionsJson = jsonObject.getJSONObject("transaction");
		TransactionModel transactionModel = hashService.jsonObjectToTransactionModel(transactionsJson);
		transactionModel.setData(hashService.encrypt(transactionModel.getData()));
		JSONObject transJsonObject = hashService.transactionModelToJSONObject(transactionModel);
		String hash = hashService.sha256(transJsonObject.toString());
		while (!hash.substring(0, 4).equals("0000")) {
			hash = hashService.sha256(hash);
			log.error(hash);
		}
		transactionModel.setTransactionHash(hash);
		BlockModel blockModel = hashService.jsonObjectToBlockModel(blockJson);
		String blockHash = hashService.hashBlock(blockModel);
		while (!blockHash.substring(0, 4).equals("0000")) {
			blockModel.setNonce(blockModel.getNonce() + 1);
			blockHash = hashService.sha256(hashService.hashBlock(blockModel));
			log.error("2 - " + blockHash);
		}
		blockModel.setHash(blockHash);
		KafkaModel kafkaModel2 = new KafkaModel();
		kafkaModel2.setBlock(blockModel);
		kafkaTemplate.send(GENESISBLOCK, kafkaModel2);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@KafkaListener(topics = ENCRYPT, groupId = BLOCKCHAIN)
	private void hashBlockchain(@Payload String kafkaModel) throws JSONException {
		log.error(kafkaModel);
		JSONObject jsonObject = new JSONObject(kafkaModel);
		JSONObject transactionsJson = jsonObject.getJSONObject("transaction");
		
		String jsonListString = jsonObject.get("blocks").toString();
		Pattern listItem = Pattern.compile("\"(.*)\"");
		Matcher matcher = listItem.matcher(jsonListString);
		
		List<String> chainList = new ArrayList<>();

		while(matcher.find()) {
			String hash = matcher.group().toString().replace("\"", "");
			chainList.add(hash);
			log.error(hash);
		}
		
		TransactionModel selectedTransaction = hashService.jsonObjectToTransactionModel(transactionsJson);
		
		List<BlockModel> blockListModel = getBlockModel(chainList);
		BlockModel requiredBlock = blockListModel.get(blockListModel.size() - 1);
		
		if(requiredBlock.getTransactions().size() < blockchainConfig.getMaxTransactionsPerBlock()) {
			selectedTransaction.setTransactionHash(hashService.stringToHex(selectedTransaction.getTransactionHash()));
			requiredBlock.getTransactions().add(selectedTransaction);
		} else {
			requiredBlock = new BlockModel();
			BlockModel prevBlock = blockListModel.get(blockListModel.size() - 1);
			Integer blockId = Integer.valueOf(prevBlock.getBlockId()) + 1;
			requiredBlock.setBlockId(blockId);
			String prevHash = hashService.hashBlock(prevBlock);
			requiredBlock.setPreviousHash(prevHash);
		}
		
		Integer nonce = 0;
		String hash = "nnnnn";
		
		requiredBlock.getTransactions().add(selectedTransaction);
		
		while("0000" != hash.substring(0, 4)) {
			requiredBlock.setNonce(nonce);
			hash = hashService.hashBlock(requiredBlock);
			log.error(nonce.toString());
			log.error(hash);
			nonce++;
		}
		
		log.error(nonce.toString());
		log.error(hash);
	}

}
