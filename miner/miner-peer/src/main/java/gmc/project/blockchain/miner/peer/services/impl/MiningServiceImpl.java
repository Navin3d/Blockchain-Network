package gmc.project.blockchain.miner.peer.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gmc.project.blockchain.miner.peer.entities.BlockchainEntity;
import gmc.project.blockchain.miner.peer.models.BlockModel;
import gmc.project.blockchain.miner.peer.models.KafkaModel;
import gmc.project.blockchain.miner.peer.models.PendingTransactionModel;
import gmc.project.blockchain.miner.peer.models.TransactionModel;
import gmc.project.blockchain.miner.peer.services.CRUDService;
import gmc.project.blockchain.miner.peer.services.ConversionService;
import gmc.project.blockchain.miner.peer.services.MiningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MiningServiceImpl implements MiningService {
	
	private final String VALIDATE = "VALIDATE";
	private final String VALIDATED = "VALIDATED";
	private final String ENCRYPTBLOCK = "ENCRYPTBLOCK";
	private final String ENCRYPTEDBLOCK = "ENCRYPTEDBLOCK";

	private final Environment env;
	private final CRUDService crudService;
	private final ConversionService conversionService;
	private final KafkaTemplate<String, KafkaModel> kafkaTemplate;
	private final Queue<PendingTransactionModel> pendingTransactions;

	@Override
	public List<PendingTransactionModel> getAllPendingTransactions() {
		List<PendingTransactionModel> returnvalue = new ArrayList<>();
		pendingTransactions.forEach(tx -> {
			returnvalue.add(tx);
		});
		return returnvalue;
	}
	
	@Override
	public void addTransactionToQueue(PendingTransactionModel pendingTransactionModel) {
		pendingTransactions.add(pendingTransactionModel);
	}
	

	public void process() throws InterruptedException, ExecutionException {
		BlockchainEntity blockchainEntity = crudService.getBlockchain();
		KafkaModel kafkaModel = new KafkaModel();
		kafkaModel.setBlockchainEntity(blockchainEntity);
		kafkaTemplate.send(VALIDATE, kafkaModel);
	}
	
	@KafkaListener(topics = VALIDATED, groupId = "Blockchain")
	private void blockchainIsValid(@Payload String kafkaModelStr) throws InterruptedException, ExecutionException, JSONException {
		JSONObject kafkaModelObj = new JSONObject(kafkaModelStr);
		Boolean isValid = kafkaModelObj.getBoolean("isVlaid");
		if(!isValid)
			throw new RuntimeException("The chain is Broken...");
		addTransactionsToBlockchain();
	}
	
	private void addTransactionsToBlockchain() throws InterruptedException, ExecutionException{		
		if(pendingTransactions.size() != 0) {
			PendingTransactionModel pendingTransactionModel = pendingTransactions.peek();
			pendingTransactions.remove();
			
			log.error("Selected Transaction Priority: {}", pendingTransactionModel.getOrder());
			
			TransactionModel transaction = getHighPriorityTransaction(pendingTransactionModel);
			KafkaModel kafkaModel = new KafkaModel();
			kafkaModel.setBlock(getBlockToHash(transaction));
			
			log.error(kafkaModel.getBlock().toString());
			
			kafkaTemplate.send(ENCRYPTBLOCK, kafkaModel);
		}
		
	}
	
	@KafkaListener(topics = "PROCESSED", groupId = "PYTHON")
	private void getDataFromOrderer(@Payload String data) throws JsonMappingException, JsonProcessingException, InterruptedException, ExecutionException {
		ObjectMapper mapper = new ObjectMapper();
		PendingTransactionModel toAdd = mapper.readValue(data, PendingTransactionModel.class);
		toAdd.setDateTime(LocalDateTime.now().toString());
		addTransactionToQueue(toAdd);
		addTransactionsToBlockchain();
	}
	
	
	@KafkaListener(topics = ENCRYPTEDBLOCK, groupId = ENCRYPTEDBLOCK)
	private void storeBlock(@Payload String kafkaModel) throws NumberFormatException, JSONException, InterruptedException, ExecutionException {
		BlockchainEntity blockchain = crudService.getBlockchain();
		BlockModel blockModel = conversionService.blockStringToBlockModel(kafkaModel);

		Boolean blockFound = false;
		for(int index = 0; index < blockchain.getBlocks().size(); index++) {
			BlockModel block = blockchain.getBlocks().get(index);
			if(block.getBlockId() == blockModel.getBlockId()) {
				blockFound = true;
				blockchain.getBlocks().remove(block);
				blockchain.getBlocks().add(blockModel);
			}
		}
		
		if(!blockFound) {
			blockchain.getBlocks().add(blockModel);
		}
		
		log.error(blockModel.toString());		
		crudService.save(blockchain);
		
		if(!pendingTransactions.isEmpty()) {
			addTransactionsToBlockchain();
		}
	}
	
	private TransactionModel getHighPriorityTransaction(PendingTransactionModel pendingTransactionModel) {
		TransactionModel transaction = new TransactionModel();
		transaction.setFromAddress(pendingTransactionModel.getFromAddress());
		transaction.setToAddress(pendingTransactionModel.getToAddress());
		transaction.setData(pendingTransactionModel.getData());
		transaction.setDateTime(pendingTransactionModel.getDateTime());
		return transaction;
	}
	
	private BlockModel getBlockToHash(TransactionModel transactionModel) throws InterruptedException, ExecutionException {
		BlockchainEntity blockchain = crudService.getBlockchain();
		
		List<BlockModel> blocksList = blockchain.getBlocks();
		Collections.sort(blocksList);
		
		BlockModel prevBlock = blocksList.get(blocksList.size() - 1);
		
		if(Integer.valueOf(env.getProperty("blockchain.max-transactions-per-block")) > prevBlock.getTransactions().size()) {
			List<TransactionModel> transactionList = prevBlock.getTransactions();
			Collections.sort(transactionList);
			
			TransactionModel prevTx = transactionList.get(transactionList.size() - 1);
			transactionModel.setOrder(prevTx.getOrder() + 1);
			transactionModel.setPreviousHash(prevTx.getTransactionHash());
			transactionModel.setTransactionHash("");
			
			prevBlock.setNonce(0);
			prevBlock.getTransactions().add(transactionModel);
			
			return prevBlock;
		} else {
			transactionModel.setOrder(1);
			transactionModel.setPreviousHash("BOOMERS");
			transactionModel.setTransactionHash("");
			
			BlockModel block = new BlockModel();
			block.setBlockId(prevBlock.getBlockId() + 1);
			block.setNonce(0);
			block.getTransactions().add(transactionModel);
			block.setPreviousHash(prevBlock.getPreviousHash());
			block.setHash("");
			
			return block;
		}
		
	}

}
