package gmc.project.blockchain.miner.peer;

import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;

import gmc.project.blockchain.miner.peer.models.PendingTransactionModel;


@EnableKafka
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableReactiveMongoRepositories
public class MinerPeerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinerPeerApplication.class, args);
	}
	
	@Bean(name = "pendingTransactions")
	public Queue<PendingTransactionModel> pendingTransactions() {
		return new PriorityQueue<>();
	}

}
