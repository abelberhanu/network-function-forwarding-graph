package it.polito.dp2.NFFG.sol3.client2;

import java.util.Set;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;

public class MyReachPolicy implements PolicyReader {

	NodeReader dest;
	NodeReader sour;
	
	String name;
	NffgReader nffgname;
	Boolean isPos;
	VerificationResultReader ver;
	Set<FunctionalType> funt;


	public MyReachPolicy(NodeReader sour,NodeReader dest) {
		this.sour=sour;
		this.dest=dest;
	}

	public MyReachPolicy(String pname, Boolean ispos, MyNffg nfname,
			Myresult verifier, NodeReader src, NodeReader dest2) {
		this.name=pname;
		this.isPos=ispos;
		this.nffgname=nfname;
		this.ver=verifier;
		this.sour=src;
		this.dest=dest2;

		
		
	}

	public MyReachPolicy(String pname, Boolean ispos2, MyNffg nfname,
			Mynode src, Mynode dest2) {
		this.name=pname;
		this.isPos=ispos2;
		this.nffgname=nfname;
		this.sour=src;
		this.dest=dest2;
	}

	public MyReachPolicy(Set<FunctionalType> ft, String pname, Boolean ispos2,
			MyNffg nfname, Myresult verifier, Mynode src, Mynode dest2) {
		this.funt = ft;
		this.name=pname;
		this.isPos=ispos2;
		this.nffgname=nfname;
		this.ver=verifier;
		this.sour=src;
		this.dest=dest2;	}

	public MyReachPolicy(Set<FunctionalType> ft, String pname, Boolean ispos2,
			MyNffg nfname, Mynode src, Mynode dest2) {
		this.funt = ft;
		this.name=pname;
		this.isPos=ispos2;
		this.nffgname=nfname;
		this.sour=src;
		this.dest=dest2;	}

	@Override
	public NffgReader getNffg() {
		// TODO Auto-generated method stub
		return this.nffgname;
	}

	@Override
	public VerificationResultReader getResult() {
		// TODO Auto-generated method stub
		return this.ver;
	}

	@Override
	public Boolean isPositive() {
		// TODO Auto-generated method stub
		return this.isPos;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	public NodeReader getDestinationNode() {
		return this.dest;
	}

	public NodeReader getSourceNode() {
	
		return this.sour;
	}

}
