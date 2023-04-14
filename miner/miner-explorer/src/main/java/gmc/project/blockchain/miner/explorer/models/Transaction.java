package gmc.project.blockchain.miner.explorer.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class Transaction implements Serializable {

	private static final long serialVersionUID = 34788454522416559L;
	
	private String chainId;
	
	private String hash;

	private String data;

	private String dateTime;

}
