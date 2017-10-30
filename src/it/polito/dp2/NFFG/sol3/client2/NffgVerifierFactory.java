package it.polito.dp2.NFFG.sol3.client2;

import java.text.ParseException;

import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;

public class NffgVerifierFactory extends it.polito.dp2.NFFG.NffgVerifierFactory {

	private NFFGClient2 nf;
	@Override
	public NffgVerifier newNffgVerifier() throws NffgVerifierException {
	try {
		nf = new it.polito.dp2.NFFG.sol3.client2.NFFGClient2();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return nf;
	}

}
