package it.polito.dp2.NFFG.sol3.client2;

import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NodeReader;

public class Mylink implements LinkReader {
	String name;
	NodeReader source;
	NodeReader destination;

	public Mylink(String linkname, Mynode source, Mynode destination) {
		this.name=linkname;
		this.source=source;
		this.destination=destination;	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
				return this.name;
	}

	@Override
	public NodeReader getDestinationNode() {
		return this.destination;

	}

	@Override
	public NodeReader getSourceNode() {
		return this.source;

	}

}
