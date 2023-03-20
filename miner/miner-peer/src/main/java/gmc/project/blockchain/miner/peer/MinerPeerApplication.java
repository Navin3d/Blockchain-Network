package gmc.project.blockchain.miner.peer;

import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;

import gmc.project.blockchain.miner.peer.models.PendingTransactionModel;

@EnableKafka
@EnableDiscoveryClient
@SpringBootApplication
@EnableReactiveMongoRepositories
public class MinerPeerApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(MinerPeerApplication.class);
		springApplication.setWebApplicationType(WebApplicationType.REACTIVE);
		springApplication.run(args);
	}

	@Bean(name = "pendingTransactions")
	public Queue<PendingTransactionModel> pendingTransactions() {
		return new PriorityQueue<>();
	}

}
