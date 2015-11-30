package com.mypackage;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mypackage.dao.FBUser;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonArray;
import com.restfb.json.JsonObject;
import com.restfb.types.Album;
import com.restfb.types.Likes;
import com.restfb.types.Photo;
import com.restfb.types.Post;
import com.restfb.types.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CallbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static String getAccessTokenFromWebContent (String webContent) {
    	String accessToken = null;
    	int s = webContent.indexOf("access_token=") + ("access_token=".length());
        int e = webContent.indexOf("&");
        accessToken = webContent.substring(s, e);
        return accessToken;
    }
    
    
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
        		accessToken = "CAACEdEose0cBAFa3PbTHLBqqN4cum5vYBmK0UJsMQ69HeiLvm06BrqoZBKBOr6OR7QHOt9qBelIh0aM78uvywAiRK07EZAwLJR914wwNWTW5JZCeZBiCTEY8jW6kXzDXRoscgrsd25vfOsayludhFyO9MMZAr8v1fu6XowXmL4OLl0chovdGlBWkrDnlfMxfHDm2j5YEKQjTCQRO964S0";
        	} else {
        		response.sendRedirect(request.getContextPath() + "/error.html");
        		return;
        	}
        	
            if(accessToken!=null) {
            	System.out.println("accessToken: " + accessToken);
            	@SuppressWarnings("deprecation")
				FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
            	User user = facebookClient.fetchObject("me", User.class);
            	FBUser fbUser = new FBUser(user.getId(),user.getName());
            	Post album= new Post();
            	request.getSession().setAttribute("fbUser", fbUser);
            	Connection<Post> myFeed = facebookClient.fetchConnection("me/feed", Post.class,Parameter.with("type", "post"));
				
					for (List<Post> myFeedConnectionPage : myFeed)
					{	
						
						for (Post post : myFeedConnectionPage)
						  { 
							
							System.out.println("Post ID: "+post.getId()+post.getLikes());
						    JsonObject jsonObject = facebookClient.fetchObject(post.getId() + "/likes",JsonObject.class, Parameter.with("summary", true),Parameter.with("limit", 1));
					        long count = jsonObject.getJsonObject("summary").getLong("total_count");
					        System.out.println("count : "+count);
					         
							
						}
				}
            	
				
				
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
}
