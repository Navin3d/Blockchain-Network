package gmc.project.blockchain.miner.peer.controllers;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gmc.project.blockchain.miner.peer.models.PendingTransactionModel;
import gmc.project.blockchain.miner.peer.services.MiningService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/mine")
public class MiningController {
	
	private final MiningService miningService;
	
	@PostMapping(path = "/transaction/pending")
	public void addTransactionToQueue(@RequestBody PendingTransactionModel pendingTransactionModel) {
		miningService.addTransactionToQueue(pendingTransactionModel);
	}
	
	@GetMapping(path = "/transaction/pending")
	public List<PendingTransactionModel> getAllTransactionInQueue() {
		return miningService.getAllPendingTransactions();
	}
	
	@GetMapping
	public String addTransactionToQueue() throws InterruptedException, ExecutionException {
		miningService.process();
		return "Processing...";
	}

}
