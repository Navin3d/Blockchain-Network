package gmc.project.blockchain.miner.peer.services;

import java.util.concurrent.ExecutionException;

public interface ChainService {

	public void process() throws InterruptedException, ExecutionException ;
	
}
