package gmc.project.blockchain.miner.hashchecker.models;

import java.io.Serializable;

import gmc.project.blockchain.miner.hashchecker.entities.BlockchainEntity;
import lombok.Data;

@Data
public class KafkaModel implements Serializable {
	
	private static final long serialVersionUID = -3722653883715994615L;
	
	private BlockchainEntity blockchainEntity;
	
	private BlockModel block;
	
	private TransactionModel transaction;
	
	private Boolean isVlaid;

}
