package com.mypackage.dao;
/*
 * @Author1 : Kunal Sharma 2014054
 * @Author2 : Sahil Ruhela 2014092
 */
import java.util.HashMap;

public class FBUser {
	
	private String id;
	private String name;
	private long likesaverage;
	HashMap<String,Long> map= new HashMap<String,Long>();
	public FBUser(String i, String n,long likesaverage,HashMap<String,Long> map){
		this.id = i;
		this.name = n;
		this.likesaverage=likesaverage;
		this.map=map;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	public Long getlikesaverage() {
		return likesaverage;
	}
    public HashMap<String,Long> getmap()
    {
		return map;
    	
    }
	}
