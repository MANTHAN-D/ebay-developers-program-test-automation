package com.edp.signin;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.pagefactory.ByChained;

/**
 * @author Manthan
 *
 */
public class EmailSignIn {

	private WebDriver driver;
	private String baseUrl;
	private Properties testData;
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EmailSignIn.class);

    @Before
    public void setUp()throws IOException{
    	testData = new Properties();
        testData.load(new FileInputStream("testdata/signin-credentials.properties"));
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        baseUrl = "https://developer.ebay.com/signin";        
        
    }
    
    @Test
    public void testValid(){
        
    	int noOfVTests = Integer.parseInt(testData.getProperty("noOfVTests"));
    	while(noOfVTests > 0){
    		driver.get(baseUrl);

    		driver.findElement(By.id("w4-w0-subject")).sendKeys(testData.getProperty("eV"+noOfVTests));
			driver.findElement(By.id("w4-w0-password")).sendKeys(testData.getProperty("pV"+noOfVTests));
    		driver.findElement(By.xpath("//*[@id=\"w4-w0-signin-button\"]")).click();
    		String titlePage = driver.getTitle();
    		try{
    			assertTrue(titlePage.equals("Application Keys | eBay"));
    		}catch(AssertionError ae){
    			log.error("Error: Valid credentials not accepted in login..!!!!! test-case no "+ noOfVTests);
    		}
    		noOfVTests--;
    	}
    }
    
    @Test
    public void testInValid(){
        
    	int noOfIVTests = Integer.parseInt(testData.getProperty("noOfIVTests"));
    	while(noOfIVTests > 0){
    		driver.get(baseUrl);

    		driver.findElement(By.id("w4-w0-subject")).sendKeys(testData.getProperty("eIV"+noOfIVTests));
			driver.findElement(By.id("w4-w0-password")).sendKeys(testData.getProperty("pIV"+noOfIVTests));
    		driver.findElement(By.xpath("//*[@id=\"w4-w0-signin-button\"]")).click();    
    		
    		WebElement existingUserAlert = driver.findElement(new ByChained(By.id("w0-error"),By.className("message")));
    		
    		try{
    			assertTrue(existingUserAlert.getText().contains("The user name and/or password for Developer account is incorrect"));
    			driver.findElement(new ByChained(By.className("status-container"),By.className("icon-close"))).click();
    		}catch(AssertionError ae){
    			log.error("Error: Invalid credentials accepted in login..!!!!!test-case no "+ noOfIVTests);
    		}
    		noOfIVTests--;
    	}
    }       
       
    @Test
    public void testForgotPasswordLink(){
        driver.get(baseUrl);
        WebElement linkElement = driver.findElement(new ByChained(By.id("w4-w0-signin-form"),By.className("fyp-link"),By.tagName("a")));
              
        linkElement.click();
        String titlePage = driver.getTitle();
        log.debug("Title of page: "+titlePage);            
        try{
        	assertTrue(titlePage.equals("eBay Developers Program Forgot Your Password | eBay"));
        }catch(AssertionError ae){
        	log.error("Error : Forgot password link not working!!!!!");
        }     
    }        		                                  
	
	@After
    public void closureActivities(){
        driver.quit();
    }
}
