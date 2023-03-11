package gmc.project.blockchain.miner.hashgenerator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.blockchain.miner.hashgenerator.models.BlockModel;
import gmc.project.blockchain.miner.hashgenerator.models.TransactionModel;
import gmc.project.blockchain.miner.hashgenerator.services.HashService;

@RestController
@RequestMapping(path="/hash")
public class HashController {
	
	@Autowired
	private HashService hashService;
	
	@GetMapping(path = "/block/{hash}")
	public ResponseEntity<BlockModel> getBlockByHash(@PathVariable String hash) throws JSONException {
		BlockModel returnValue = hashService.getBlockModelFromHash(hash);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
	
	@GetMapping(path = "/transaction/{hash}")
	public ResponseEntity<TransactionModel> getTransactionByHash(@PathVariable String hash) throws JSONException {
		TransactionModel returnValue = hashService.getTransactionModelFromHash(hash);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
	
	@PostMapping(path = "/block")
	public ResponseEntity<String> getBlockByHash(@RequestBody BlockModel blockModel) throws JSONException {
		String returnValue = hashService.hashBlock(blockModel);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}
	
	@PostMapping(path = "/transaction")
	public ResponseEntity<String> getTransactionByHash(@RequestBody TransactionModel transactionModel) throws JSONException {
		String returnValue = hashService.hashTransaction(transactionModel);
		return ResponseEntity.status(HttpStatus.OK).body(returnValue);
	}

}
