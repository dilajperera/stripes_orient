package com.dilaj.stripes.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.stripes.controller.FileUploadLimitExceededException;
import net.sourceforge.stripes.controller.multipart.CommonsMultipartWrapper;
/**
 * 
 * @author Dilaj
 *
 */
public class BCommonsMultipartWrapper extends CommonsMultipartWrapper {
	
	private long maxPostSizeInBytes = 1024 * 1024 * 2000; // Defaults to 10MB * 2000 = 2 GB
	private static Logger LOGGER = LoggerFactory.getLogger(UploadActionBean.class);
	
	@Override
	public void build(HttpServletRequest request, File tempDir, long maxPostSize){
		LOGGER.info("File is ready to upload");
		try {
			super.build(request, tempDir, maxPostSizeInBytes);
		} catch (FileUploadLimitExceededException | IOException e) {
			e.printStackTrace();
		}
	}

}
