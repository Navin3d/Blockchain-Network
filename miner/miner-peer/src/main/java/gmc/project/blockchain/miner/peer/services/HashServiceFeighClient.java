package gmc.project.blockchain.miner.peer.services;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import gmc.project.blockchain.miner.peer.models.BlockModel;
import gmc.project.blockchain.miner.peer.models.TransactionModel;


@FeignClient("Miner-Hash-Generating-Service")
public interface HashServiceFeighClient {
	
	@GetMapping(path = "/hash/block/{hash}")
	public ResponseEntity<BlockModel> getBlockByHash(@PathVariable String hash) throws JSONException;
	
	@GetMapping(path = "/hash/transaction/{hash}")
	public ResponseEntity<TransactionModel> getTransactionByHash(@PathVariable String hash) throws JSONException;
	
	@PostMapping(path = "/hash//block")
	public ResponseEntity<String> getBlockByHash(@RequestBody BlockModel blockModel) throws JSONException;
	
	@PostMapping(path = "/hash/transaction")
	public ResponseEntity<String> getTransactionByHash(@RequestBody TransactionModel transactionModel) throws JSONException;

}
