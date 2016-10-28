/**
 * 
 */
package com.edp.home;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * @author Manthan
 *
 */
public class HomePageLinks {

	private WebDriver driver;
	private String baseUrl;	
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(HomePageLinks.class);

    @Before
    public void setUp(){
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        baseUrl = "https://go.developer.ebay.com/";        
        
    }

    @Test
    public void testLinks(){
        driver.get(baseUrl);
        List<WebElement> linkList = driver.findElements(By.tagName("a"));
        ArrayList<String> linkNames = new ArrayList<String>();
        for(WebElement linkElement : linkList){
        	if(linkElement.getText().length() > 0)
        		linkNames.add(linkElement.getText());
        }               
        
        String cssClass,id;
        for(String linkName : linkNames){
        	try{
            	log.debug("Link being checked : "+linkName);   
            	
            	JavascriptExecutor js = (JavascriptExecutor) driver;        	
            	
            	WebElement currentElement = driver.findElement(By.linkText(linkName));
            	
            	cssClass = currentElement.getAttribute("class"); 
            	id = currentElement.getAttribute("id");
            	
            	if(cssClass.contains("element-invisible") || // Skip non-visible
            			cssClass.contains("dropdown-toggle") || //Skip dropdown
            			id.contains("scrolldown")){ // Skip scrollable elements
            		continue;
            	}
            	
            	//to scroll screen to appropriate location to avoid overlapping of elements
            	js.executeScript("scroll(0,"+currentElement.getLocation().y+(-350)+")");
            	currentElement.click();
            	
            	log.debug("Title of page: "+driver.getTitle());
            	
            	driver.navigate().back();
        	}
        	catch(NoSuchElementException e){
        		//e.printStackTrace();
        		System.out.println(linkName + " generated exception");
        	}
        }
    }  
    
    @After
    public void closureActivities(){
        driver.quit();
    }
}
