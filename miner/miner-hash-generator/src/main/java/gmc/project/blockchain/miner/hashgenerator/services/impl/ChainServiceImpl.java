package gmc.project.blockchain.miner.hashgenerator.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import gmc.project.blockchain.miner.hashgenerator.models.KafkaModel;
import gmc.project.blockchain.miner.hashgenerator.services.ChainService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChainServiceImpl implements ChainService {
	
	private final String ENCRYPT = "ENCRYPT";
	private final String BLOCKCHAIN = "BLOCKCHAIN";
	
	@Autowired
	private KafkaTemplate<String, KafkaModel> kafkaTemplate;

	@Override
	public void process() {
		// TODO Auto-generated method stub
		
	}
	
	@KafkaListener(topics = ENCRYPT, groupId = BLOCKCHAIN)
	private void hashBlockchain(@Payload String kafkaModel) {
		log.error(kafkaModel);
	}

}
