package com.mypackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.mypackage.dao.StopWords;

public class DataHandler {
 long datainterpretationaveragelikes(ArrayList<Long> countlist) {
		// TODO Auto-generated method stub
		int post=countlist.size();
		long likesum=0;
		int i=0;
		while(i<post)
		{ 
			likesum=likesum+countlist.get(i);
		    i++;	
		}
		
		return (likesum/post);
	}

	

void datainterpretation(ArrayList<String> list, ArrayList<Long> countlist)
    {
		int i=0;
		while(i<list.size())
		{
			StopWords remove= new StopWords();
	    	String result=remove.removeStopWords(list.get(i));
	    	list.remove(i);
	    	list.add(i, result);
	    	i++;
		}
		i=0;
		Long maxlike = Collections.max(countlist);
		System.out.println(maxlike);
		while(i<list.size())
		{
			
			if(countlist.get(i)>(maxlike-40))
			System.out.println("Post: " +list.get(i)+" Likes: "+countlist.get(i));
	    	i++;
		}
    }
HashMap<String,Long> typelike= new HashMap<String,Long>();
HashMap<String,Long> typenumber= new HashMap<String,Long>();
void databytype(String type,Long like)
{
	
	if(typelike.containsKey(type))
	{
		long value=typelike.get(type);
		value=value+like;
		typelike.remove(type);
		typelike.put(type, value);
	}
	else
	{
		typelike.put(type,like);
	}
	
}
void databytypenumber(String type)
{
	if(typenumber.containsKey(type))
	{
		Long value=typenumber.get(type);
		value++;
		typenumber.remove(type);
		typenumber.put(type, value);
	}
	else
	{
		Long value=(long) 1;
		typenumber.put(type, value);
	}
	
}
HashMap <String, Long> finalhashmap= new HashMap<String,Long>(); 
void manipulate ()
{
	
	for (String keys: typelike.keySet())
	{
		Long like= typelike.get(keys);
		Long numberoftype= typenumber.get(keys);
		Long value=like/numberoftype;
		finalhashmap.put(keys, value);
	}
	for (String keys: finalhashmap.keySet())
	{
		System.out.println("key: " + keys + " value: " + finalhashmap.get(keys));
	}
}
}
