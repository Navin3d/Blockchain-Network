package gmc.project.blockchain.miner.hashgenerator.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TransactionModel implements Serializable {

	private static final long serialVersionUID = 7849974095287969919L;
		
	private String fromAddress;
	
	private String toAddress;
	
	private String data;
	
	private LocalDateTime dateTime;
	
	private String previousHash;
	
	private String hash;

}
