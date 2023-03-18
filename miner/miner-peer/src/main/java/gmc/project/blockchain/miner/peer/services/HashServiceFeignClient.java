package gmc.project.blockchain.miner.peer.services;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("Miner-Hash-Generating-Service")
public interface HashServiceFeignClient {
	
//	public String stringToHex(String string);

}
