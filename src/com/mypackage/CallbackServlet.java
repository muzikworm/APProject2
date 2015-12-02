package com.mypackage;

/*
 * @Author1 : Kunal Sharma 2014054
 * @Author2 : Sahil Ruhela 2014092
 */
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
@WebServlet("/callbackServlet")
public class CallbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
//    private static String getAccessTokenFromWebContent (String webContent) {
//    	String accessToken = null;
//    	int s = webContent.indexOf("access_token=") + ("access_token=".length());
//        int e = webContent.indexOf("&");
//        accessToken = webContent.substring(s, e);
//        return accessToken;
//        }
    
    
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
        		
        		accessURL = "https://graph.facebook.com/oauth/access_token?client_id=" + facebookAppId + 
        				"&redirect_uri=" + redirectURL + "&client_secret=" + facebookAppSecret + "&code=" + code;
        		
        		webContent = getWebContentFromURL(accessURL);
        	
        		
        		accessToken = "CAACEdEose0cBAA2TBlFrQhZCJd2X8Kq8CXCTHGpwThV9sMFDQprLW8ZAJPfdZAFdeSZAnRBR88zZBBqKStFIq4WIw5G4bp7zP6U8sa7K08xXwtK7syuNBHcuVmaCAtdnV7IINAfGATF2kbGhuZCwWbPxvlU1ckyDze2cX0SIJnovAzuUyMrQhjCQYJI3rtlvZCHZCvY9kFVZCTA9bJEJvVcxZA";
        	} else {
        		response.sendRedirect(request.getContextPath() + "/error.html");
        		return;
        	}
        	
        	
        	DataHandler data= new DataHandler();
            
        	if(accessToken!=null) {
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
							//System.out.println("Post ID: "+post.getId()+" Type: "+ post.getType());
							
							
							JsonObject jsonObject = facebookClient.fetchObject(post.getId() + "/likes",JsonObject.class, Parameter.with("summary", true),Parameter.with("limit", 1000));
					        long count = jsonObject.getJsonObject("summary").getLong("total_count");
					       // System.out.println("Likes : "+count);
					        countlist.add(count);
					        data.databytype(post.getType(),count);
					        data.databytypenumber(post.getType());
					        if(post.getDescription().toString()!=null)
					        {
					        	des.add(post.getDescription().toString());
					        }
					        else if(post.getDescription().toString()==null)
					        {
					        	des.add(" NULL");
					        }
					        
							}catch (Exception e)
							{}
						  }
				}
            	
	            
				long likesaverage=data.datainterpretationaveragelikes(countlist);
				data.datainterpretation(des,countlist);
                data.manipulate();
    
				FBUser fbUser = new FBUser(user.getId(),user.getName(),likesaverage,data.finalhashmap);
            	request.getSession().setAttribute("fbUser", fbUser);
            	request.getSession().setAttribute("finalmap",data.finalhashmap);
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
