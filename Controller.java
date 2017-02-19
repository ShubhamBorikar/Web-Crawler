import java.util.List;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	public static void main(String[] args) throws Exception { 
		  String crawlStorageFolder = "/data/root"; 
		  int numberOfCrawlers = 1; 
		   
		  CrawlConfig config = new CrawlConfig(); 
		  config.setCrawlStorageFolder(crawlStorageFolder); 
		  //config.setPolitenessDelay(300); 
		  config.setMaxDepthOfCrawling(16); 
		  config.setMaxPagesToFetch(20000); 
		  config.setResumableCrawling(false); 
		   
		  PageFetcher pageFetcher = new PageFetcher(config); 
		  RobotstxtConfig robotstxtConfig = new RobotstxtConfig(); 
		  RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher); 
		  CrawlController crawlController = new CrawlController(config, pageFetcher, robotstxtServer); 
		   
		  crawlController.addSeed("http://www.nbcnews.com/"); 
		  
		   
		  crawlController.start(MyCrawler.class, numberOfCrawlers); 
		 } 
}
