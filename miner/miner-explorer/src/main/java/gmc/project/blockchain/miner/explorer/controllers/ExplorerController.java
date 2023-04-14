package gmc.project.blockchain.miner.explorer.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.blockchain.miner.explorer.models.Transaction;
import gmc.project.blockchain.miner.explorer.services.ExplorerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/explorer")
@RequiredArgsConstructor
public class ExplorerController {
	
	private final ExplorerService explorerService;
	
	@PostMapping("/from")
	private ResponseEntity<List<Transaction>> getTransactionsFrom(@RequestBody Transaction transaction) throws InterruptedException, ExecutionException {
		return ResponseEntity.status(HttpStatus.OK).body(explorerService.findTransactionsFrom(transaction.getChainId(), transaction.getHash()));
	}
	
	@PostMapping("/to")
	private ResponseEntity<List<Transaction>> getTransactionsTo(@RequestBody Transaction transaction) throws InterruptedException, ExecutionException {
		return ResponseEntity.status(HttpStatus.OK).body(explorerService.findTransactionTo(transaction.getChainId(), transaction.getHash()));
	}

}
