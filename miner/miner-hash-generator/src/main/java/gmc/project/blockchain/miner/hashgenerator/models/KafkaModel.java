package gmc.project.blockchain.miner.hashgenerator.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class KafkaModel implements Serializable {
	
	private static final long serialVersionUID = -3722653883715994615L;
	
	private List<String> blocks = new ArrayList<>();
	
	private TransactionModel transaction;
	
}
