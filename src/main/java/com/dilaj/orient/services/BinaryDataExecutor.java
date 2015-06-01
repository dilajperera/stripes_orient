package com.dilaj.orient.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dilaj.util.ServiceConstant;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.record.impl.ORecordBytes;

/**
 * 
 * @author Dilaj
 *
 */
public class BinaryDataExecutor {


	private ODatabaseDocumentTx database = new ODatabaseDocumentTx("remote:127.0.0.1/testdb").open("admin", "admin");
	//private ODatabaseDocumentTx database = new ODatabaseDocumentTx("remote:node98200-dilaj.j.layershift.co.uk/testdb").open("root", "TVRvaa93427");
	private static Logger LOGGER = LoggerFactory.getLogger(BinaryDataExecutor.class);

	/**
	 * Save the binary data as chunks
	 * @param rid
	 * @param bField
	 * @param inputStream
	 * @param metaData
	 */
	public String uploadFile(final String metaData, final InputStream inputStream) {

		
		database.declareIntent( new OIntentMassiveInsert() );
		List<ORID> chunks = new ArrayList<ORID>();	
		//add meta data to the chunks
		ORecordBytes metaDataBytes = new ORecordBytes(metaData.getBytes());
		database.save(metaDataBytes);
		chunks.add(metaDataBytes.getIdentity());
		
		try {
			LOGGER.info("File : {} is saving to the db....",metaData);
			while (inputStream.available() > 0 ) {
			  ORecordBytes chunk = new ORecordBytes();
			  chunk.fromInputStream(inputStream, 8192 ); 
			  database.save( chunk ); 
			  chunks.add( chunk.getIdentity() );  
			  LOGGER.info("Data chunk {} is saved. ",chunk.getIdentity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		ODocument record = setODocument(metaData,chunks);
		if(Objects.nonNull(record)){
			database.save(record);
			LOGGER.info("File : {} is successfully saved to the db....",record.toJSON());
			return record.toJSON();
		}	
		
		database.declareIntent( null );
		return null;
	}
	
	/**
	 * update the ODocument 
	 * @param metaData
	 * @param chunks
	 * @return
	 */
	private ODocument setODocument(final String metaData,final List<ORID> chunks){
	
		JsonNode metaJson = toJson(metaData);
		JsonNode rid = metaJson.get(ServiceConstant.AT_RID);
		JsonNode bField = metaJson.get(ServiceConstant.BINARY_FIELD);
		JsonNode clazz = metaJson.get(ServiceConstant.AT_CLASS);
		ODocument oDocument = null;
		
		if (Objects.nonNull(bField)) {
			if (Objects.nonNull(rid)) {
				ORecordId _rid = new ORecordId(rid.asText());
				if (_rid.isPersistent()) {
					oDocument = database.load(_rid);
				}
			} else if(Objects.nonNull(clazz)){
				oDocument = new ODocument(clazz.asText());
			}
			return Optional.ofNullable(oDocument).
					map(odoc -> odoc.field(bField.asText(), chunks)).orElse(null);
		}
		return null;
	
	}
	
	/**
	 * convert string to a json node
	 * @param data
	 * @return
	 */
	private JsonNode toJson(final String data){	
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			return mapper.readTree(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * read file from the db
	 * @param data
	 * @return
	 */
	public Map<String,Object> readFile(final String data){
		
		JsonNode fileInfo = toJson(data);
		JsonNode rid = fileInfo.get(ServiceConstant.AT_RID);
		JsonNode bField = fileInfo.get(ServiceConstant.BINARY_FIELD);
		Map<String,Object> result = new HashMap<>();
		
		if (Objects.nonNull(rid) && Objects.nonNull(bField)) {
			
			ORecordId _rid = new ORecordId(rid.asText());
			if (_rid.isPersistent()) {
				
				ODocument oDocument = database.load(_rid);
				oDocument.setLazyLoad(false);
				List<OIdentifiable> brids = oDocument.field(bField.asText());

				if (Objects.nonNull(brids)) {
					ORecordBytes metaDataBytes = (ORecordBytes) brids.remove(0).getRecord();
					JsonNode metaData = toJson(new String(metaDataBytes.toStream()));
					result.put(ServiceConstant.CONTENT_TYPE, metaData.get(ServiceConstant.CONTENT_TYPE).asText());
					result.put(ServiceConstant.FILE_NAME, metaData.get(ServiceConstant.FILE_NAME).asText());
					LOGGER.info("File : {} is reading from the db....",metaData.get(ServiceConstant.FILE_NAME).asText());
					 
					try {
						File file = File.createTempFile("speed", ".tmp");
						OutputStream out = new BufferedOutputStream(new FileOutputStream(file));	 
						for (OIdentifiable id : brids) {
							ORecordBytes chunk = (ORecordBytes) id.getRecord();
							chunk.toOutputStream(out);
							chunk.unload();
							LOGGER.info("Data chunk {} is reading from the db. ",chunk.getIdentity());
						}
						result.put(ServiceConstant.INPUT_STREAM, new FileInputStream(file));
						LOGGER.info("File : {} is successfully read from the db....",metaData.get(ServiceConstant.FILE_NAME).asText());
						file.delete();
						return result;
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					 
				}
			}
		}
		
		return null;
	}
	
}
