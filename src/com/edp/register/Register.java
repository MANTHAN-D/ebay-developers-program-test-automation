package com.edp.register;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;


/**
 * @author Manthan
 *
 */
public class Register {

	private WebDriver driver;
	private String baseUrl;	
	private Properties testData;
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Register.class);

    @Before
    public void setUp() throws IOException{
    	testData = new Properties();
        testData.load(new FileInputStream("testdata/register-credentials.properties"));
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        baseUrl = "https://developer.ebay.com/signin?tab=register";        
        
    }
    
	@Test
    public void testValid(){
        
		int noOfVTests = Integer.parseInt(testData.getProperty("noOfVTests"));
		while(noOfVTests > 0){
			driver.get(baseUrl);

			driver.findElement(By.id("w4-w1-registration-username")).sendKeys(testData.getProperty("uV"+noOfVTests));
			driver.findElement(By.id("w4-w1-w1-password")).sendKeys(testData.getProperty("pV"+noOfVTests));
			driver.findElement(By.id("w4-w1-registration-email")).sendKeys(testData.getProperty("reV"+noOfVTests));
			driver.findElement(By.id("w4-w1-registration-reenter-email")).sendKeys(testData.getProperty("rreV"+noOfVTests));
			driver.findElement(By.id("w4-w1-w2-phone")).sendKeys(testData.getProperty("phV"+noOfVTests));
			driver.findElement(By.id("w4-w1-checkbox-user-agreement")).click();	
			driver.findElement(By.id("w4-w1-w3-captcha-response-field")).sendKeys(getCaptcha());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driver.findElement(By.id("w4-w1-join-button")).click();
			log.debug(driver.getTitle());        			
			try{
				WebElement errorElement  = driver.findElement(By.id("w4-w1-w3-captcha-error"));
				for(int i=0;i<15;i++){
					if(errorElement.getAttribute("class").contains("hidden")){
						break;
					}
					driver.findElement(By.id("w4-w1-w3-captcha-response-field")).sendKeys(getCaptcha());
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					driver.findElement(By.id("w4-w1-join-button")).click();
					errorElement  = driver.findElement(By.id("w4-w1-w3-captcha-error"));
				}
				errorElement  = driver.findElement(By.id("w4-w1-w3-captcha-error"));
				assertTrue(!errorElement.isDisplayed());
			}catch(NoSuchElementException e){				
				log.error("Error: Something went wrong with registration..!! test-case no:"+noOfVTests);				
			}
			catch(AssertionError e){
				log.error("Error: Valid credentials not accepted on register page..!!!!!test-case no:"+noOfVTests);
			}
			noOfVTests--;
		}
                
    }  
	
	@Test
    public void testInValid(){
        
		int noOfIVTests = Integer.parseInt(testData.getProperty("noOfIVTests"));
		while(noOfIVTests > 0){
			driver.get(baseUrl);

			driver.findElement(By.id("w4-w1-registration-username")).sendKeys(testData.getProperty("uIV"+noOfIVTests));
			driver.findElement(By.id("w4-w1-w1-password")).sendKeys(testData.getProperty("pIV"+noOfIVTests));
			driver.findElement(By.id("w4-w1-registration-email")).sendKeys(testData.getProperty("reIV"+noOfIVTests));
			driver.findElement(By.id("w4-w1-registration-reenter-email")).sendKeys(testData.getProperty("rreIV"+noOfIVTests));
			driver.findElement(By.id("w4-w1-w2-phone")).sendKeys(testData.getProperty("phIV"+noOfIVTests));
			driver.findElement(By.id("w4-w1-checkbox-user-agreement")).click();		
			driver.findElement(By.id("w4-w1-w3-captcha-response-field")).sendKeys(getCaptcha());					

			WebElement uErrorElement  = driver.findElement(By.id("w4-w1-username-error"));
			WebElement reErrorElement  = driver.findElement(By.id("w4-w1-email-error"));
			WebElement rreErrorElement  = driver.findElement(By.id("w4-w1-reenter-email-error"));
			WebElement phErrorElement  = driver.findElement(By.id("w4-w1-w2-phone-error"));
			
			WebElement cErrorElement  = driver.findElement(By.id("w4-w1-w3-captcha-error"));
						
			driver.findElement(By.id("w4-w1-join-button")).click();
			WebElement mainErrorElement  = driver.findElement(By.id("w0-error"));
			log.debug(driver.getTitle());
			
			try{
				assertFalse(uErrorElement.getAttribute("class").contains("hidden"));
				assertFalse(reErrorElement.getAttribute("class").contains("hidden"));
				assertFalse(rreErrorElement.getAttribute("class").contains("hidden"));
				assertFalse(phErrorElement.getAttribute("class").contains("hidden"));								
				assertFalse(cErrorElement.getAttribute("class").contains("hidden"));
				
				assertFalse(mainErrorElement.getAttribute("class").contains("hidden"));
				assertTrue(mainErrorElement.isDisplayed());
				
			}catch(AssertionError e){
				log.error("Error:Invalid credentials accepted on register page..!!!!! testcase no: " + noOfIVTests);
			}
			noOfIVTests--;
		}
                
    }	
		
	@After
    public void closureActivities(){
        driver.quit();
    }
	
	private String getCaptcha(){
		String captchaValue = "";
		try{
			byte[] arrScreen = ((FirefoxDriver)driver).getScreenshotAs(OutputType.BYTES);
			BufferedImage imageScreen = ImageIO.read(new ByteArrayInputStream(arrScreen));
			WebElement cap = driver.findElement(By.id("w4-w1-w3-captcha-image"));
			Dimension capDimension = cap.getSize();
			Point capLocation = cap.getLocation();
			BufferedImage imgCap = imageScreen.getSubimage(capLocation.x, capLocation.y, capDimension.width, capDimension.height);
			File imageFile = new File("captcha/eBayCaptcha.jpg");
			ImageIO.write(imgCap, "jpg", imageFile);
			try{				
				ITesseract t = new Tesseract();
				captchaValue = t.doOCR(imageFile);
				log.debug("Captcha Image text: " + captchaValue);
				imageFile.delete();
			}catch(TesseractException te){
				te.printStackTrace();
			}
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}
		
		return captchaValue;
	}
}
