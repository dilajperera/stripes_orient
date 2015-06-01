package com.dilaj.orient.services.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.record.impl.ORecordBytes;

public class OrientBinaryData {

	public static void main(String[] args) throws IOException {
		OrientBinaryData o = new OrientBinaryData();
		//o.test();
		o.readData();
	}
	
	public void test() {
		ODatabaseDocumentTx database = new ODatabaseDocumentTx(
				"remote:127.0.0.1/testdb").open("admin", "admin");
		database.declareIntent(new OIntentMassiveInsert());

		List<ORID> chunks = new ArrayList<ORID>();
		File file = new File("");
		InputStream in = null;

		try {
			in = new BufferedInputStream(new FileInputStream(
					"C:/Users/Dilaj/Desktop/stripes.pdf"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			while (in.available() > 0) {
				final ORecordBytes chunk = new ORecordBytes();
				chunk.fromInputStream(in, 8192); // READ REMAINING DATA, BUT NOT
													// MORE THAN 8K
				database.save(chunk); // SAVE THE CHUNK TO GET THE REFERENCE
										// (IDENTITY) AND FREE FROM THE MEMORY
				chunks.add(chunk.getIdentity()); // SAVE ITS REFERENCE INTO THE
													// COLLECTION
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// SAVE THE COLLECTION OF REFERENCES IN A NEW DOCUMENT
		ODocument record = new ODocument("OData");
		record.field("data", chunks);
		database.save(record);
		database.declareIntent(null);
	}

	public void readData() throws IOException {

		ODatabaseDocumentTx database = new ODatabaseDocumentTx(
				"remote:127.0.0.1/testdb").open("admin", "admin");
		ORecordId recordId = new ORecordId("#16:35");
		
		File file = new File("C:/Users/Dilaj/Desktop/stripes12345.pdf");
		ODocument record = database.load(recordId);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		
		record.setLazyLoad(false);
		for (OIdentifiable id : (List<OIdentifiable>) record.field("data")) {
		    ORecordBytes chunk = (ORecordBytes) id.getRecord();
		    System.out.println("::chunk size "+chunk.getSize());
		    chunk.toOutputStream(out);
		    chunk.unload();
		}
		
	
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
