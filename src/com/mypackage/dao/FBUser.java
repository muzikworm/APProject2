package com.mypackage.dao;

public class FBUser {
	
	private String id;
	private String name;
	private long likesaverage;

	public FBUser(String i, String n,long likesaverage){
		System.out.println("FB User class");
		System.out.println(i);
		System.out.println(n);
		this.id = i;
		this.name = n;
		this.likesaverage=likesaverage;
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

	}
