package com.mypackage;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mypackage.dao.FBUser;
import com.mypackage.dao.StopWords;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.types.Post;
import com.restfb.types.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CallbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
//    private static String getAccessTokenFromWebContent (String webContent) {
//    	String accessToken = null;
//    	int s = webContent.indexOf("access_token=") + ("access_token=".length());
//        int e = webContent.indexOf("&");
//        accessToken = webContent.substring(s, e);
//        return accessToken;
//    }
    
    
    private static String getWebContentFromURL(String webnames) {
    	try {
            URL url = new URL(webnames);
            URLConnection urlc = url.openConnection();
            //BufferedInputStream buffer = new BufferedInputStream(urlc.getInputStream());
            BufferedReader buffer = new BufferedReader(new InputStreamReader(urlc.getInputStream(), "UTF8"));
            StringBuffer builder = new StringBuffer();
            int byteRead;
            while ((byteRead = buffer.read()) != -1)
                builder.append((char) byteRead);
            buffer.close();
            String text=builder.toString();
            return text;
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    	return null;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String code = null;
        String facebookAppId = getServletContext().getInitParameter("facebookAppId");
        String facebookAppSecret = getServletContext().getInitParameter("facebookAppSecret");
        String redirectURL = null;
        String accessURL = null;
    	String accessToken = null;
    	String webContent = null;
    	System.out.println("My accesss token"+request.getParameter("accesstoken_html"));
        try {
            StringBuffer redirectURLbuffer = request.getRequestURL();
            int index = redirectURLbuffer.lastIndexOf("/");
            redirectURLbuffer.replace(index, redirectURLbuffer.length(), "").append("/callback");
            redirectURL = URLEncoder.encode(redirectURLbuffer.toString(), "UTF-8");
            
        	code = request.getParameter("code");
        	if(null!=code) {
        		System.out.println("Code: " + code);
        		accessURL = "https://graph.facebook.com/oauth/access_token?client_id=" + facebookAppId + 
        				"&redirect_uri=" + redirectURL + "&client_secret=" + facebookAppSecret + "&code=" + code;
        		System.out.println("accessURL: " + accessURL);
        		webContent = getWebContentFromURL(accessURL);
        		System.out.println("accessURL: " + webContent);
        		
        		accessToken = "CAACEdEose0cBAN9luzIZClZAZB7TrO1GKCLbWnZAS65JG3W7RZA4aHFqmo85Nf63lPisDDCfWAjVJATtDBdm2sjmkWrYmhBWgYqW5b15lQBIOpewZBr9o9guZCxZAZCRGZAK4ah0nap91B2xpFBD3JJhw5IpaurpHaiUEEtsPZCgmfT2FlfODJP9rnTcv1ZACbZCQyF0ZCNkGdkIZCTl3nH6kCXqJx5";
        	} else {
        		response.sendRedirect(request.getContextPath() + "/error.html");
        		return;
        	}
        	
        	
        	
            
        	if(accessToken!=null) {
            	System.out.println("accessToken: " + accessToken);
            	@SuppressWarnings("deprecation")
				FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
            	User user = facebookClient.fetchObject("me", User.class);
            	
            	ArrayList <Long> countlist= new ArrayList<Long>();
            	ArrayList <String> des= new ArrayList <String>();
            	Connection<Post> myFeed = facebookClient.fetchConnection("me/feed", Post.class,Parameter.with("type", "post"));
	            for (List<Post> myFeedConnectionPage : myFeed)
					{	
						for (Post post : myFeedConnectionPage)
						  { 
							try{
								//System.out.println("Post ID: "+post.getId());
								
							
							JsonObject jsonObject = facebookClient.fetchObject(post.getId() + "/likes",JsonObject.class, Parameter.with("summary", true),
					                        Parameter.with("limit", 1));
					        long count = jsonObject.getJsonObject("summary").getLong("total_count");
					       // System.out.println("Likes : "+count);
					        countlist.add(count);
					        if(post.getDescription().toString()!=null)
					        {
					        	des.add(post.getDescription().toString());
					        }
					        
							}catch (Exception e)
							{}
						  }
				}
            	
				long likesaverage=datainterpretationaveragelikes(countlist);
				datainterpretation(des,countlist);
				FBUser fbUser = new FBUser(user.getId(),user.getName(),likesaverage);
            	request.getSession().setAttribute("fbUser", fbUser);
            	System.out.println(user.getId()+" "+user.getName());
            	
            	response.sendRedirect(request.getContextPath() + "/welcome.jsp");
            }
  
            if(null==accessToken)
            	response.sendRedirect(request.getContextPath() + "/error.html");
        	
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/error.html");
            throw new ServletException(e);
        }
        
    }
 
 

	private long datainterpretationaveragelikes(ArrayList<Long> countlist) {
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
    	
    }
}
