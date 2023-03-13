package gmc.project.blockchain.miner.hashgenerator.configurations;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainConfig implements Serializable {
	
	private static final long serialVersionUID = 2277381585572566509L;
	
	private String chainId;
	
	private Integer maxTransactionsPerBlock;

}
