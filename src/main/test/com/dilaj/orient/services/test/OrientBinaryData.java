package com.dilaj.orient.services.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.io.FileInputStream;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.primitives.Bytes;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.record.impl.ORecordBytes;
import com.orientechnologies.orient.core.serialization.OBase64Utils;


public class OrientBinaryData {

	public static void main(String[] args) throws IOException {
		OrientBinaryData o = new OrientBinaryData();
		//o.test();
		o.readData();
	}
	
	public void test() {
		ODatabaseDocumentTx database = new ODatabaseDocumentTx(
				"remote:127.0.0.1/SampleServices").open("admin", "admin");
		database.declareIntent(new OIntentMassiveInsert());

		List<ORID> chunks = new ArrayList<ORID>();
		
		InputStream in = null;

		File _file = new File("C:/Users/dperera/Desktop/Machine Movement Access Form.xls");

		try {
			in = new BufferedInputStream(new FileInputStream(
					_file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
 
		int size = 0;
		try {
			while (in.available() > 0) {
				final ORecordBytes chunk = new ORecordBytes();
				chunk.fromInputStream(in, 8192); // READ REMAINING DATA, BUT NOT
													// MORE THAN 8K
				database.save(chunk); // SAVE THE CHUNK TO GET THE REFERENCE
										// (IDENTITY) AND FREE FROM THE MEMORY
				chunks.add(chunk.getIdentity()); // SAVE ITS REFERENCE INTO THE
				size += chunk.getSize();								// COLLECTION
				System.out.println(">>>>>>>>>>>>>>  chunk size "+chunk.getSize());
			}
			
			System.out.println("::::::::::::::::: total size :::::"+size);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// SAVE THE COLLECTION OF REFERENCES IN A NEW DOCUMENT
		ODocument record = new ODocument("OData");
		record.field("data", chunks);
		database.save(record);
		System.out.println(":::::::::: record "+record);
		database.declareIntent(null);
	}

	public void readData() throws IOException {

		ODatabaseDocumentTx database = new ODatabaseDocumentTx(
				"remote:127.0.0.1/SampleServices").open("admin", "admin");
		ORecordId recordId = new ORecordId("#35:0");
		
		File file = new File("C:/Users/dperera/Desktop/testDownload/sample.pdf");
		ODocument record = database.load(recordId);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		
		//OBase64Utils.OutputStream base64OutputStream = new OBase64Utils.OutputStream(out);
		int size = 0;
		record.setLazyLoad(false);
		//ORecordBytes r = new 
		List<Byte> oRecord = new ArrayList<>();

		for (OIdentifiable id : (List<OIdentifiable>) record.field("data")) {
		    ORecordBytes chunk = (ORecordBytes) id.getRecord();
		    System.out.println("::chunk size "+chunk.getSize());
	/*	    byte[] bytes = chunk.toStream();
		    oRecord.addAll(Arrays.asList(ArrayUtils.toObject(bytes)));
		    
		    size += chunk.getSize();*/			
		//    chunk.toOutputStream(base64OutputStream);
		    chunk.toOutputStream(out);
			
		    chunk.unload();
		}
		
		out.flush();
		/*Byte[] bytes = oRecord.toArray(new Byte[oRecord.size()]);
		ORecordBytes oRecordBytes = new ORecordBytes(ArrayUtils.toPrimitive(bytes));
		System.out.println(":::::::::: total size :::::::: "+oRecordBytes.getSize());
		oRecordBytes.toOutputStream(out);
		out.flush();
		oRecordBytes.unload();*/
		
	
		
	/*	
		record.setLazyLoad(false);
		for (OIdentifiable id : (List<OIdentifiable>) record.field("data")) {
			ORecordBytes chunk = (ORecordBytes) id.getRecord();
			OutputStream out = null;
			try {
				out = new FileOutputStream(
						"C:/Users/Dilaj/Desktop/sample122.zip");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				chunk.toOutputStream(out);
			} catch (IOException e) {
				e.printStackTrace();
			}

			chunk.unload();
			
			

		}*/
	}

}
