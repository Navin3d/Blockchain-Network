package gmc.project.blockchain.miner.peer.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfig {
	
	private String serverUrl;
	
	private String encodingTopic;	
	private String encodingBlockGroupId;	
	private String encodingTransactionGroupId;
	
	private String decodingTopic;	
	private String decodingBlockGroupId;	
	private String decodingTransactionGroupId;

}
