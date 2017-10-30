package it.polito.dp2.NFFG.sol3.client2;

import java.util.Set;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;
public class MyPolicy implements PolicyReader {
	Boolean posetive;
	String name;
	NffgReader nffgName;
	Myresult result = null;
	MyReachPolicy reachpo= null;	
	Set<FunctionalType> ft;

	public MyPolicy(String name,NffgReader nffg_name,Boolean posetive,MyReachPolicy mrp) {
		this.name=name;
		this.nffgName=nffg_name;
		this.reachpo=mrp;
		
	}
	
	public MyPolicy(String name,NffgReader nffg_name,Boolean posetive,Myresult res,MyReachPolicy mrp) {
		this.name=name;
		this.nffgName=nffg_name;
		this.result=res;
		this.reachpo=mrp;
		
	}
	public MyPolicy(String name,NffgReader nffg_name,Boolean posetive,Myresult res,MyReachPolicy mrp,Set<FunctionalType> func) {
		this.name=name;
		this.nffgName=nffg_name;
		this.result=res;
		this.reachpo=mrp;
		this.ft=func;
	}
	public MyPolicy(String name){
		this.name=name;
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public NffgReader getNffg() {
	
		return this.nffgName;
	}

	@Override
	public VerificationResultReader getResult() {
	
		return this.result;
	}

	@Override
	public Boolean isPositive() {
		// TODO Auto-generated method stub
		return this.posetive;
	}

}
