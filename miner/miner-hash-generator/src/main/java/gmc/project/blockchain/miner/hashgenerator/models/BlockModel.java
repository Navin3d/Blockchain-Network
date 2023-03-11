package gmc.project.blockchain.miner.hashgenerator.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BlockModel implements Serializable {

	private static final long serialVersionUID = 5158779638584221144L;
	
	private String blockId;
	
	private Integer nonce;
	
	private List<TransactionModel> transactions = new ArrayList<>();
	
	private String previousHash;
	
	private String hash;

}
