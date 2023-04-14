package gmc.project.blockchain.miner.hashchecker.services.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.hashchecker.entities.BlockchainEntity;
import gmc.project.blockchain.miner.hashchecker.models.BlockModel;
import gmc.project.blockchain.miner.hashchecker.models.KafkaModel;
import gmc.project.blockchain.miner.hashchecker.models.TransactionModel;
import gmc.project.blockchain.miner.hashchecker.services.ConversionService;
import gmc.project.blockchain.miner.hashchecker.services.HashCheckingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashCheckingServiceImpl implements HashCheckingService {
	
	private final String VALIDATE = "VALIDATE";
	private final String VALIDATED = "VALIDATED";
	
	private final ConversionService conversionService;
	private final KafkaTemplate<String, KafkaModel> kafkaTemplate;
	
	private String sha256(String data) {
		String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
	}
	
	@KafkaListener(topics = VALIDATE, groupId = "Blockchain")
	private void process(@Payload String kafkaString) throws JSONException {
		BlockchainEntity blockchainEntity = conversionService.stringToBlockchainEntity(kafkaString);
		validateBlolckchain(blockchainEntity);
	}

	@Override
	public Boolean validateBlolckchain(BlockchainEntity blockchainEntity) throws JSONException {
		Boolean isValid = true;
		if(blockchainEntity.getBlocks().size() != 0) {
			log.error("validatind...");
			isValid = isValid & validateBlocks(blockchainEntity.getBlocks());
		}
		KafkaModel kafkaModel = new KafkaModel();
		kafkaModel.setIsVlaid(isValid);
		kafkaTemplate.send(VALIDATED, kafkaModel);
		return isValid;
	}
	
	private Boolean validateBlocks(List<BlockModel> blocks) throws JSONException {
		Collections.sort(blocks);
		Boolean isValid = true;
		for(int index = 0; index < blocks.size(); index++) {
			if(index == 0) {
				if(!blocks.get(index).getPreviousHash().equals("BOOMERS"))
					isValid = false;
			} else {
				int prevTxIndex = index - 1;
				if(!blocks.get(index).getPreviousHash().equals(blocks.get(prevTxIndex).getHash()))
					isValid = false;
				isValid = isValid & validateTansactionsInBlock(blocks.get(index).getTransactions()) & validateBlockHash(blocks.get(index));
			}
		}
		return isValid;
	}
	
	private Boolean validateBlockHash(BlockModel blockModel) throws JSONException {
		blockModel.setHash("");
		JSONObject jsObj = conversionService.blockModelToJSONObject(blockModel);
		String jsonStr = jsObj.toString();
		String calculatedHash = sha256(jsonStr);
		String prevHash = blockModel.getHash();
		return calculatedHash.equals(prevHash);
	}
	
	private Boolean validateTansactionsInBlock(List<TransactionModel> transactions) throws JSONException {
		Collections.sort(transactions);
		Boolean isValid = true;
		for(int index = 0; index < transactions.size(); index++) {
			if(index == 0) {
				if(!transactions.get(index).getPreviousHash().equals("BOOMERS"))
					isValid = false;
			} else {
				int prevTxIndex = index - 1;
				if(!transactions.get(index).getPreviousHash().equals(transactions.get(prevTxIndex).getTransactionHash()))
					isValid = false;
				isValid = isValid & validateTransactionHash(transactions.get(index));
			}
		}
		return isValid;
	}
	
	private Boolean validateTransactionHash(TransactionModel transactionModel) throws JSONException {
		transactionModel.setTransactionHash("");
		JSONObject jsObj = conversionService.transactionModelToJSONObject(transactionModel);
		String jsonStr = jsObj.toString();
		String calculatedHash = sha256(jsonStr);
		String prevHash = transactionModel.getTransactionHash();
		return calculatedHash.equals(prevHash);
	}

}
