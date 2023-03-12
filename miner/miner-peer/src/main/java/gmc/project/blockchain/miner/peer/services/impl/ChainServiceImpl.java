package gmc.project.blockchain.miner.peer.services.impl;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.peer.configurations.BlockchainConfig;
import gmc.project.blockchain.miner.peer.dao.BlockchainDao;
import gmc.project.blockchain.miner.peer.entities.BlockchainEntity;
import gmc.project.blockchain.miner.peer.models.KafkaModel;
import gmc.project.blockchain.miner.peer.models.TransactionModel;
import gmc.project.blockchain.miner.peer.services.ChainService;

@Service
public class ChainServiceImpl implements ChainService {

	@Autowired
	private BlockchainConfig blockchainConfig;
	@Autowired
	private BlockchainDao blockchainDao;
	@Autowired
	private KafkaTemplate<String, KafkaModel> kafkaTemplate;
	
	private final String ENCRYPTED = "ENCRYPTED";
	private final String ENCRYPT = "ENCRYPT";
	private final String BLOCKCHAIN = "BLOCKCHAIN";
	
	@Override
	public void process() throws InterruptedException, ExecutionException  {
		sendTransactionToKafkaQueue();
	}
	
	private void sendTransactionToKafkaQueue() throws InterruptedException, ExecutionException  {
		BlockchainEntity blockchain = blockchainDao.findById(blockchainConfig.getChainId()).toFuture().get();
		TransactionModel selectedTransaction = null;
		try {
			selectedTransaction = blockchain.getPendingTransactions().get(0);
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
		
		if(selectedTransaction != null) {
			KafkaModel model = new KafkaModel();
			model.setBlocks(blockchain.getBlocks());
			model.setTransaction(selectedTransaction);
			kafkaTemplate.send(ENCRYPT, model);
			blockchain.getPendingTransactions().remove(selectedTransaction);
			blockchainDao.save(blockchain);
		}
	}
	
	@KafkaListener(topics = ENCRYPTED, groupId = BLOCKCHAIN)
	private void presistTransactionHashFromKafkaQueue(@Payload KafkaModel kafkaModel)  throws InterruptedException, ExecutionException  {
		BlockchainEntity blockchain = blockchainDao.findById(blockchainConfig.getChainId()).toFuture().get();
		blockchain.setBlocks(kafkaModel.getBlocks());
		blockchainDao.save(blockchain);
	}

}
