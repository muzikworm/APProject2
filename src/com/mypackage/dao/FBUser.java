package com.mypackage.dao;

public class FBUser {
	
	private String id;
	private String name;

	public FBUser(String i, String n){
		System.out.println("FB User class");
		System.out.println(i);
		System.out.println(n);
		this.id = i;
		this.name = n;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	}
