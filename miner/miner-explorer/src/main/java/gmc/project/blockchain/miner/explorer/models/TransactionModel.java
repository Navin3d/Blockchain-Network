package gmc.project.blockchain.miner.explorer.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class TransactionModel implements Serializable, Comparable<TransactionModel> {

	private static final long serialVersionUID = 7849974095287969919L;
	
	private Integer order;
	
	private String fromAddress;
	
	private String toAddress;
	
	private String data;
	
	private String dateTime;
	
	private String previousHash;
	
	private String transactionHash;
	
	@Override
	public int compareTo(TransactionModel o)  {
		if(this.order == o.getOrder())
			return 0;
		else if(this.order > o.getOrder())
			return 1;
		else
			return -1;
	}

}
