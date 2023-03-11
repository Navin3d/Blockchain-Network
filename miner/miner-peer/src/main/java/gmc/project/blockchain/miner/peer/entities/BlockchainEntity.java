package gmc.project.blockchain.miner.peer.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import gmc.project.blockchain.miner.peer.models.TransactionModel;
import lombok.Data;

@Data
@Document(collection = "smart_ledger")
public class BlockchainEntity implements Serializable {

	private static final long serialVersionUID = 794483179056516842L;
	
	@Id
	private String id;
	
	private List<String> blocks = new ArrayList<>();
	
	private List<TransactionModel> pendingTransactions = new ArrayList<>();

}
