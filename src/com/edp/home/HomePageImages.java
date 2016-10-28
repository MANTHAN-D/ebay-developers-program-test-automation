/**
 * 
 */
package com.edp.home;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author Manthan
 *
 */
public class HomePageImages {

	private WebDriver driver;
	private String baseUrl;	
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HomePageImages.class);

    @Before
    public void setUp(){
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        baseUrl = "https://go.developer.ebay.com/";
        
    }
    
	@Test
    public void testImages(){
    	driver.get(baseUrl);
        List<WebElement> imageList = driver.findElements(By.tagName("img"));
        log.debug("Scanning images for alt attribute...");
        for(WebElement imageElement : imageList){   
        	String altValue = imageElement.getAttribute("alt"); 
        	if(altValue != null && !altValue.equals("")){
        		// image has alt attribute
        		log.debug("Alt found as : "+ altValue);
        	}
        	else{
        		log.error("Missing Alt for src : "+imageElement.getAttribute("src"));
        	}
        }                              
    }
	
	@After
    public void closureActivities(){
        driver.quit();
    }
}
