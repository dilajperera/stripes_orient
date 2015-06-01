package com.dilaj.orient.services.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

import com.dilaj.orient.services.BinaryDataExecutor;
import com.dilaj.util.ServiceConstant;
import com.orientechnologies.orient.client.remote.OServerAdmin;
import com.orientechnologies.orient.client.remote.OStorageRemote;
import com.orientechnologies.orient.core.config.OStorageConfiguration;
import com.orientechnologies.orient.core.storage.OStorage;

public class BinaryDataExecutorTest {
	//private static BinaryDataExecutor binaryDataExecutor = new BinaryDataExecutor();

	public static void main(String[] args) {
	//uploadFile();
		//downloadFile();
		System.out.println("::::::::::::::::;");

		try {
			System.out.println(">>>>>>>>>>>>>");
			//OServerAdmin s = new OServerAdmin("https://node98180-env-6826425.j.layershift.co.uk");
			
			
		
			OServerAdmin s = new OServerAdmin("remote:node98180-env-6826425.j.layershift.co.uk/testdb");
			System.out.println("::::::::;; "+s.existsDatabase());
		//s.connect("root", "AMGqcy91190");
		//	System.out.println(":::::::: "+s.isConnected());
			//s.listDatabases();
			//s.connect("root", "AMGqcy91190");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/*private static void uploadFile(){
		
		try {
			String metaData = "{'@class' : 'OData', 'bField': 'b_data', 'file':'adaraye_unusuma_laga_2.mp3'}";
			InputStream inputStream = new FileInputStream("C:/Users/Dilaj/Desktop/adaraye_unusuma_laga_2.mp3");
			binaryDataExecutor.uploadFile(metaData, inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}*/
	
/*	private static void downloadFile(){
		String data = "{'@rid':'#16:36','bField': 'b_data', 'path': 'C:/Users/Dilaj/Desktop/testDownload'}";
		InputStream inputStream = (InputStream) binaryDataExecutor.readFile(data).get(ServiceConstant.INPUT_STREAM);
		File file = new File("C:/Users/Dilaj/Desktop/testDownload/abc.mp3");
		try {
			FileUtils.copyInputStreamToFile(inputStream,file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}*/

}
