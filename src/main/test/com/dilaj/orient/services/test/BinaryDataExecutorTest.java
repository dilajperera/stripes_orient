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
	private static BinaryDataExecutor binaryDataExecutor = new BinaryDataExecutor();

	public static void main(String[] args) {
		//uploadFile();
		downloadFile();
	}
	
	private static void uploadFile(){
		
		try {
			String metaData = "{'@class' : 'OData', 'bField': 'b_data', 'file':'Insurance Scheme FY15.pdf'}";
			InputStream inputStream = new FileInputStream("C:/Users/dperera/Desktop/Insurance Scheme FY15.pdf");
			binaryDataExecutor.uploadFile(metaData, inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void downloadFile(){
		String data = "{'@rid':'#35:19','bField': 'b_data', 'path': 'C:/Users/dperera/Desktop/testDownload'}";
		InputStream inputStream = (InputStream) binaryDataExecutor.readFile(data).get(ServiceConstant.INPUT_STREAM);
		File file = new File("C:/Users/dperera/Desktop/testDownload/stripes.pdf");
		try {
			FileUtils.copyInputStreamToFile(inputStream,file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
