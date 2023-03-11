package gmc.project.blockchain.miner.peer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;


@EnableKafka
@EnableDiscoveryClient
@SpringBootApplication
@EnableReactiveMongoRepositories
public class MinerPeerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinerPeerApplication.class, args);
	}

}
