<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>FileManager</title>
</head>
<body>
	<h1>This is a sample file uploader || downloader</h1>

	<%-- <stripes:form beanclass="com.dilaj.stripes.web.UploadActionBean" enctype="multipart-form/data">
		<stripes:file name="fileBean" />
		<stripes:submit name="upload" value="upload" />
	</stripes:form> --%>
	
	<p> 
	  <br> To upload the file using HttpClient <br>
	  <br> HttpClient client =  new HttpClientImpl(); <br>
      <br> client.login("admin", "admin"); <br>
      <br> String data = "{'@class' : 'OData', 'bField': 'b_data', 'file':'The.Pirate.Fairy.2014.720p.BluRay.x264.YIFY.mp4','path':'C:/Users/Dilaj/Videos/Animations/The Pirate Fairy (2014)/The.Pirate.Fairy.2014.720p.BluRay.x264.YIFY.mp4'}"; <br>
      <br>  client.uploadFile(data); <br>

	  <br> To download the file using HttpClient <br>
      <br> String data = "{'@rid':'16:51','bField': 'b_data', 'path': 'C:/Users/Dilaj/Desktop/testDownload'}"; <br>
      <br> client.downloadFile(data);  <br>
	</p>

</body>
</html>