package gmc.project.blockchain.miner.peer.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class KafkaModel implements Serializable {
	
	private static final long serialVersionUID = -3722653883715994615L;
	
	private BlockModel block;
	
	private TransactionModel transaction;

}
