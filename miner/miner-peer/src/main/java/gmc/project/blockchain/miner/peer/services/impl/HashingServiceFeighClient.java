package gmc.project.blockchain.miner.peer.services.impl;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("Miner-Hash-Generating-Service")
public interface HashingServiceFeighClient {
	
	public String encrypt(String dataToEncrypt);
	public String decrypt(String encryptedData);

}
