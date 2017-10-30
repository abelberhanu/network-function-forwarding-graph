package it.polito.dp2.NFFG.sol3.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class policyDB {
	
	//public static ConcurrentMap<String, TypeNffg> MapNffg = new ConcurrentHashMap<String, TypeNffg>();
	public static Map<String, TypePolicy> MapPolicy = new HashMap<String, TypePolicy>();
	
	public static Map<String, TypePolicy> getMapPolicy() {
		return MapPolicy;
	}
	public static void setMapPolicy(Map<String, TypePolicy> mapPolicy) {
		MapPolicy = mapPolicy;
	}
	
	
	
	

}
