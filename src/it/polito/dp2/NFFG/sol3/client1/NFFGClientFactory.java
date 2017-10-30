package it.polito.dp2.NFFG.sol3.client1;

import it.polito.dp2.NFFG.lab3.NFFGClient;
import it.polito.dp2.NFFG.lab3.NFFGClientException;

public class NFFGClientFactory extends it.polito.dp2.NFFG.lab3.NFFGClientFactory {

	private NFFGClient c;
	@Override
	public NFFGClient newNFFGClient() throws NFFGClientException {
	    
		c= new it.polito.dp2.NFFG.sol3.client1.NFFGClient();
		return c;
	}

}
