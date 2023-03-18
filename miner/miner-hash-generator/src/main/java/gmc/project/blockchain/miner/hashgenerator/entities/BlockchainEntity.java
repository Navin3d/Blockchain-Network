package gmc.project.blockchain.miner.hashgenerator.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gmc.project.blockchain.miner.hashgenerator.models.BlockModel;
import lombok.Data;

@Data
public class BlockchainEntity implements Serializable {

	private static final long serialVersionUID = 794483179056516842L;

	private String id;

	private String title;

	private String description;

	private String organization;

	private String coinName;

	private String coinSymbol;

	private List<BlockModel> blocks = new ArrayList<>();

}
