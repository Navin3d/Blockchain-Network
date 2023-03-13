package gmc.project.blockchain.miner.peer.controllers;

import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.blockchain.miner.peer.entities.BlockchainEntity;
import gmc.project.blockchain.miner.peer.services.CRUDService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/crud")
public class CRUDController {
	
	private final CRUDService crudService;
	
	@GetMapping(path = "/inialize/genesis")
	private String mineGenesisBlock() throws InterruptedException, ExecutionException {
		crudService.initiateMiningGenesisBlock();
		return "Working ON IT!";
	}
	
	@PostMapping
	private BlockchainEntity initializeBlockchain(@RequestBody BlockchainEntity blockchainEntity) throws InterruptedException, ExecutionException {
		return crudService.createBlockchain(blockchainEntity);
	}

}
