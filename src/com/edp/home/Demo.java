package com.edp.home;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File imageFile;
		ITesseract t = new Tesseract();
		for(int i=0;i<6;i++){
			imageFile = new File("testdata/eBayCaptcha"+i+".jpg");
			
			try{
				String result = t.doOCR(imageFile);
				System.out.println("Image" +i+": " + result);
			}catch(TesseractException te){
				te.printStackTrace();
			}
		}		
	}

}
