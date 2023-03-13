package gmc.project.blockchain.miner.hashgenerator.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class TransactionModel implements Serializable {

	private static final long serialVersionUID = 7849974095287969919L;
	
	private String transactionHash;
	
	private String fromAddress;
	
	private String toAddress;
	
	private String data;
	
	private String dateTime;
	
	private String previousHash;

}
