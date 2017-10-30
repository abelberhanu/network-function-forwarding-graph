package it.polito.dp2.NFFG.sol3.client2;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NodeReader;

public class MyNffg implements NffgReader {
	Set<NodeReader> nodelist = new HashSet<NodeReader>();
	 String name;
   Calendar updatetime = Calendar.getInstance();
	DateFormat dateFormat;

	public MyNffg(String name){
		this.name=name;
	}
	
	public MyNffg(String name,Calendar updtime,Set<NodeReader> nr) {
	
		this.name=name;
		this.updatetime=updtime;
		this.nodelist=nr;
		
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public NodeReader getNode(String arg0) {
		NodeReader nr = null;
		for (NodeReader noder:this.nodelist)
		{
			if(noder.getName().equals(arg0))
			{
				nr=noder;
			}
		}
		// TODO Auto-generated method stub
		return nr;
	}

	@Override
	public Set<NodeReader> getNodes() {
		// TODO Auto-generated method stub
		return this.nodelist;
	}

	@Override
	public Calendar getUpdateTime() {
		// TODO Auto-generated method stub
		return this.updatetime;
	}

}
