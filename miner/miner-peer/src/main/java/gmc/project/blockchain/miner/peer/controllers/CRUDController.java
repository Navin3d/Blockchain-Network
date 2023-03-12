package gmc.project.blockchain.miner.peer.controllers;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.blockchain.miner.peer.entities.BlockchainEntity;
import gmc.project.blockchain.miner.peer.services.CRUDService;

@RestController
@RequestMapping(path = "/crud")
public class CRUDController {
	
	@Autowired
	private CRUDService crudService;
	
	@PostMapping
	private ResponseEntity<BlockchainEntity> createChain(@RequestBody BlockchainEntity blockchainEntity) throws InterruptedException, ExecutionException  {
		BlockchainEntity returnValue = crudService.createBlockchain(blockchainEntity);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}

}
