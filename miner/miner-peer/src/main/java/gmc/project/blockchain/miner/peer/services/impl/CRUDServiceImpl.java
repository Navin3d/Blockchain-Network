package gmc.project.blockchain.miner.peer.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.peer.dao.BlockchainDao;
import gmc.project.blockchain.miner.peer.entities.BlockchainEntity;
import gmc.project.blockchain.miner.peer.models.BlockModel;
import gmc.project.blockchain.miner.peer.models.KafkaModel;
import gmc.project.blockchain.miner.peer.models.TransactionModel;
import gmc.project.blockchain.miner.peer.services.CRUDService;
import gmc.project.blockchain.miner.peer.services.ConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CRUDServiceImpl implements CRUDService {
	
	private final String ENCRYPTGENESISBLOCK = "ENCRYPTGENESISBLOCK";
	private final String GENESISBLOCK = "GENESISBLOCK";
	
	private final Environment env;
	private final BlockchainDao blockchainDao;
	private final ConversionService conversionService;
	private final KafkaTemplate<String, KafkaModel> kafkaTemplate;
	
	@Override
	public BlockchainEntity getBlockchain() throws InterruptedException, ExecutionException {
		String chainId = env.getProperty("blockchain.chain-id");
		BlockchainEntity foundBlockchain = blockchainDao.findById(chainId).toFuture().get();
		if(foundBlockchain == null)
			throw new RuntimeException("Blockchain with Id: " + chainId + " not found...");
		return foundBlockchain;
	}

	@Override
	public BlockchainEntity createBlockchain(BlockchainEntity blockchainEntity) throws InterruptedException, ExecutionException {
		BlockchainEntity createdBlockchain = blockchainDao.save(blockchainEntity).toFuture().get();
		return createdBlockchain;
	}

	@Override
	public BlockModel getBlockByHash(String hash) throws InterruptedException, ExecutionException {
		BlockchainEntity requiredBlockchain = getBlockchain();
		Optional<BlockModel> returnValue = requiredBlockchain.getBlocks().stream().filter(block -> block.getHash().equals(hash)).findFirst();
		if(returnValue.isEmpty())
			throw new RuntimeException("Block with hash: " + hash + " not found...");
		return returnValue.get();
	}

	@Override
	public void initiateMiningGenesisBlock() throws InterruptedException, ExecutionException  {
		KafkaModel kafkaModel = new KafkaModel();
		TransactionModel transaction = new TransactionModel();
		transaction.setOrder(0);
		transaction.setFromAddress("oxnvn3d");
		transaction.setToAddress("oxsampes");
		transaction.setData("The Best Friend...");
		transaction.setPreviousHash("BOOMERS");
		transaction.setDateTime(LocalDateTime.now().toString());
		
		BlockModel genesisBlock = new BlockModel();
		genesisBlock.setBlockId(1);
		genesisBlock.setNonce(0);
		genesisBlock.setPreviousHash("BOOMERS");
		
		kafkaModel.setBlock(genesisBlock);
		kafkaModel.setTransaction(transaction);
		kafkaTemplate.send(ENCRYPTGENESISBLOCK, kafkaModel);
	}
	
	@KafkaListener(topics = GENESISBLOCK, groupId = "GenecisBlock")
	private void mineGenesisBlock(@Payload String blockString) throws InterruptedException, ExecutionException, NumberFormatException, JSONException {
		log.error(blockString);
		BlockchainEntity requiredBlockchain = getBlockchain();
		BlockModel blockModel = conversionService.blockStringToBlockModel(blockString);
		requiredBlockchain.getBlocks().add(blockModel);
		log.error(requiredBlockchain.toString());		
		blockchainDao.save(requiredBlockchain).toFuture().get();
	}

	@Override
	public BlockchainEntity save(BlockchainEntity blockchainEntity) throws InterruptedException, ExecutionException {
		BlockchainEntity saved = blockchainDao.save(blockchainEntity).toFuture().get();
		return saved;
	}

}
