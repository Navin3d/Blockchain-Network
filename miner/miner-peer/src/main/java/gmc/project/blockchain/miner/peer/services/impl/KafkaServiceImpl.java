package gmc.project.blockchain.miner.peer.services.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.peer.configurations.KafkaConfig;
import gmc.project.blockchain.miner.peer.models.BlockModel;
import gmc.project.blockchain.miner.peer.models.TransactionModel;
import gmc.project.blockchain.miner.peer.services.KafkaService;

@Service
public class KafkaServiceImpl implements KafkaService {
	
	@Autowired
	private KafkaConfig kafkaConfig;	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Override
	public BlockModel getBlockModelFromHash(String blockHash) throws InterruptedException, ExecutionException {
		kafkaTemplate.send(kafkaConfig.getDecodingTopic(), kafkaConfig.getDecodingBlockGroupId(), blockHash);
		return null;
	}

	@Override
	public String hashBlock(BlockModel blockModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionModel getTransactionModelFromHash(String transactionHash) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String hashTransaction(TransactionModel transactionModel) {
		// TODO Auto-generated method stub
		return null;
	}

}
