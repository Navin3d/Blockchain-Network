package gmc.project.blockchain.miner.explorer.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BlockModel implements Serializable, Comparable<BlockModel> {

	private static final long serialVersionUID = 5158779638584221144L;
	
	private Integer blockId;
	
	private Integer nonce;
	
	private List<TransactionModel> transactions = new ArrayList<>();
	
	private String previousHash;
	
	private String hash;
	
	@Override
	public int compareTo(BlockModel o) {
		if(this.blockId == o.getBlockId())
			return 0;
		else if(this.blockId > o.getBlockId())
			return 1;
		else
			return -1;
	}

}
