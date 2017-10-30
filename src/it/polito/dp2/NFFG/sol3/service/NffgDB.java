package it.polito.dp2.NFFG.sol3.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class NffgDB {
	
	public static Map<String, TypeNffg> MapNffg = new HashMap<String, TypeNffg>();
	//public static ConcurrentMap<String, TypePolicy> MapPolicy = new ConcurrentHashMap<String, TypePolicy>();
	

	public static Map<String, TypeNffg> getMapNffg() {
		return MapNffg;
	}
	public static void setMapNffg(Map<String, TypeNffg> mapNffg) {
		MapNffg = mapNffg;
	}

	
	
	

}
