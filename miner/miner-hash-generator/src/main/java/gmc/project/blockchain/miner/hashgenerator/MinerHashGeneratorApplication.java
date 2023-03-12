package gmc.project.blockchain.miner.hashgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MinerHashGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinerHashGeneratorApplication.class, args);
	}

}
