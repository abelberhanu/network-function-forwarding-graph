package it.polito.dp2.NFFG.sol3.client2;

import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NodeReader;

public class Mynode implements NodeReader {
	 FunctionalType func;
	 Set<LinkReader> linklist = new HashSet<LinkReader>();
	 String name;
	 

	 public Mynode(String name,FunctionalType func,Set<LinkReader> linklist){
		 this.name=name;
		 this.func=func;
		 this.linklist=linklist;
	 }

	 public Mynode(String nodename) {
		this.name=nodename;
	}
	
	// public Mynode(){}
	 
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public FunctionalType getFuncType() {
		// TODO Auto-generated method stub
		
		return this.func;
	}

	@Override
	public Set<LinkReader> getLinks() {
		// TODO Auto-generated method stub
		return this.linklist;
	}

}
