import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler 
{
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js" + "|mp3|mp4|zip|gzmid|mp2|wav|avi|mov|mpeg|ram|m4v))$");
	HashMap<String,ArrayList<String>> hmvisit=new HashMap<String,ArrayList<String>>();
	HashMap<String,Integer> hmfetch=new HashMap<String,Integer>();
	int fetchesAttempted=0;//fetchesSucceeded=0,fetchesAborted=0,fetchesFailed=0;
	int urlsWithin=0,urlsOutside=0;
	int status200=0,status301=0,status401=0,status403=0,status404=0;
	int file1kb=0,file10kb=0,file100kb=0,file1mb=0,fileg1mb=0;
	int typetext=0,gif=0,jpeg=0,png=0,pdf=0;
	
			 @Override
			 public boolean shouldVisit(Page referringPage, WebURL url) {
				 
				 String href = url.getURL().toLowerCase();
				 
				 if(href.startsWith("http://www.nbcnews.com/"))
					 urlsWithin++;
				 else
					 urlsOutside++;
				 
				 
				 return !FILTERS.matcher(href).matches() && href.startsWith("http://www.nbcnews.com/");
				 
				 

			 }
			 
		  @Override
		  public void visit(Page page) 
		  {
			  
			  String url = page.getWebURL().getURL();
			  //System.out.println("URL: " + url);
			  if (page.getParseData() instanceof HtmlParseData) 
			  {
				  HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
				  String text = htmlParseData.getText();
				  String html = htmlParseData.getHtml();
				  Set<WebURL> links = htmlParseData.getOutgoingUrls();
				  System.out.println("Text length: " + text.length());
				  System.out.println("Html length: " + html.length());
				  System.out.println("Number of outgoing links: " + links.size());
				  				  
			  String url2=url.replaceAll(",","_");
			  
			  String filesize=String.valueOf(html.length());
			  
			  String outlinks=String.valueOf(links.size());
			  
			  String contentType=page.getContentType();
			  
			  int fsize=html.length();
			  if(fsize<1000)
				  file1kb++;
			  else if(fsize<10000)
				  file10kb++;
			  else if(fsize<100000)
				  file100kb++;
			  else if(fsize<1000000)
				  file1mb++;
			  else
				  fileg1mb++;
			  
			  if(contentType.startsWith("text/html"))
			  {
				  contentType="text/html";
			  }
			  
			  if(contentType=="text/html")
			  {
				  typetext++;
			  }
			  else if(contentType=="gif")
			  {
				  gif++;
			  }
			  else if(contentType=="jpeg")
			  {
				  jpeg++;
			  }
			  if(contentType=="png")
			  {
				  png++;
			  }
			  
			  ArrayList<String> al=new ArrayList<String>();
			  al.add(filesize);
			  al.add(outlinks);
			  al.add(contentType);
			  
			  
			  hmvisit.put(url2,al);
					 
			  }	 
				  
				  
			   
				 
		  }
				
		  @Override
		  protected void handlePageStatusCode(WebURL webUrl,int statusCode,String statusDescription)
		  {
			 
					 String url=String.valueOf(webUrl).replaceAll(",","_");
					 if(statusCode==200)
						 status200++;
					 else if(statusCode==301)
						 status301++;
					 else if(statusCode==401)
						 status401++;
					 else if(statusCode==403)
						 status403++;
					 else if(statusCode==404)
						 status404++;
					 hmfetch.put(url,statusCode); 
					 
		  }
		  
		  @Override
		  public void onBeforeExit()
		  {
			  PrintWriter pwfetch = null;
				try {
					pwfetch = new PrintWriter(new File("fetch_NBC_News.csv"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(String url:hmfetch.keySet())
				{
					StringBuilder sb = new StringBuilder();
			        
			        sb.append(url);
			        sb.append(',');
			        sb.append(hmfetch.get(url));
			        sb.append('\n');

			        pwfetch.write(sb.toString());
				}
			        
			        pwfetch.close();
			  
			  PrintWriter pw = null;
				try {
					pw = new PrintWriter(new File("visit_NBC_News.csv"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(String url:hmvisit.keySet())
				{
					StringBuilder sb = new StringBuilder();
			        
			        sb.append(url);
			        sb.append(',');
			        sb.append(hmvisit.get(url).get(0));
			        sb.append(',');
			        sb.append(hmvisit.get(url).get(1));
			        sb.append(',');
			        sb.append(hmvisit.get(url).get(2));
			        sb.append('\n');

			        pw.write(sb.toString());
				}
			        
			        pw.close();
			        
		       System.out.println("URLs within: "+urlsWithin+" URLs outside: "+urlsOutside);
		       System.out.println("200: "+status200+" 301: "+status301+" 401: "+status401+" 403: "+status403+" 404: "+status404);
		       System.out.println("File sizes: "+file1kb+" "+file10kb+" "+file100kb+" "+file1mb+" "+fileg1mb);
		       System.out.println("File Types: "+typetext+" "+gif+" "+jpeg+" "+png);
		  }
			  
}
