package gmc.project.blockchain.miner.explorer.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import gmc.project.blockchain.miner.explorer.models.BlockModel;
import lombok.Data;

@Data
@Document(collection = "smart_ledger")
public class BlockchainEntity implements Serializable {

	private static final long serialVersionUID = 794483179056516842L;
	
	@Id
	private String id;
	
	private String title;
	
	private String description;
	
	private String organization;
	
	private String coinName;
	
	private String coinSymbol;
	
	private List<BlockModel> blocks = new ArrayList<>();

}
