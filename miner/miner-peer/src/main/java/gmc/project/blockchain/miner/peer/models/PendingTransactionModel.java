package gmc.project.blockchain.miner.peer.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class PendingTransactionModel implements Serializable, Comparable<PendingTransactionModel> {

	private static final long serialVersionUID = -7743002489562049452L;
	
	private Double order;
	
	private String fromAddress;
	
	private String toAddress;
	
	private String data;
	
	private String dateTime;

	@Override
	public int compareTo(PendingTransactionModel o) {
		if(this.order == o.getOrder())
			return 0;
		else if(this.order > o.getOrder())
			return 1;
		else
			return -1;
	}

}
