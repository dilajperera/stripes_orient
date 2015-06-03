package com.dilaj.stripes.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dilaj.orient.services.BinaryDataExecutor;
import com.dilaj.util.ServiceConstant;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * @author Dilaj
 *
 */
@UrlBinding("/binarydata/{action}")
public class UploadActionBean extends BaseActionBean {

	private FileBean fileBean;
	private String action;
	private BinaryDataExecutor binaryDataExecutor = new BinaryDataExecutor();
	private static Logger LOGGER = LoggerFactory.getLogger(UploadActionBean.class);
	
	@DefaultHandler
	public Resolution main() {
		switch (action) {
			case ServiceConstant.UPLOAD:
				return uploadFile();

			case ServiceConstant.DOWNLOAD:
				return downloadFile();
				
			default:
				break;
		}

		return new  StreamingResolution("text", new StringReader("{'sucess : 0'}"));
	}

	/**
	 * upload the file 
	 * @return the success message
	 */
	private StreamingResolution uploadFile(){
		try {
			InputStream inputStream = fileBean.getInputStream();
			HashMap<String,Object> metaData = fromJson(data);
			metaData.put(ServiceConstant.CONTENT_TYPE, fileBean.getContentType());
			LOGGER.info("File : {}  content-type : {} is ready to upload",fileBean.getFileName(),fileBean.getContentType());
			LOGGER.info("MetaData : {}",toJson(metaData));
			String result = binaryDataExecutor.uploadFile(toJson(metaData), inputStream);
			return new  StreamingResolution("text", new StringReader(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * download the file 
	 * @return stream of the file
	 */
	private StreamingResolution downloadFile(){
		Map<String,Object>  result = binaryDataExecutor.readFile(data);
		InputStream inputStream = (InputStream) result.get(ServiceConstant.INPUT_STREAM);
		String contentType = (String) result.get(ServiceConstant.CONTENT_TYPE); 
		String fileNme = (String) result.get(ServiceConstant.FILE_NAME); 
		StreamingResolution resolution = new StreamingResolution(contentType, inputStream);
		resolution.setFilename(fileNme);
		LOGGER.info("File : {} is ready to download",fileNme);
		return resolution;
	}
	
	public FileBean getFileBean() {
		return fileBean;
	}

	public void setFileBean(FileBean fileBean) {
		this.fileBean = fileBean;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	/**
	 * return the json string
	 * @param metaData
	 * @return
	 */
	private String toJson(final HashMap<String,Object> metaData){
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(metaData);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	/**
	 * convert string to a json node
	 * @param data
	 * @return
	 */
	private HashMap<String,Object> fromJson(final String data){	
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
			return mapper.readValue(data, typeRef);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return null;
	}

}
