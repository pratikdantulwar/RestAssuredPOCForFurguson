package TestRestAPI.SalesOrder;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.Record;
import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.RestAPITesting.DatabaseConnection.DatabaseConnection;
import com.RestAPITesting.RestUtilities.BaseClass;
import com.RestAPITesting.RestUtilities.ExtentTestManager;
import com.RestAPITesting.RestUtilities.FrameworkConfigConstants;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import groovyjarjarasm.asm.commons.Method;

public class listTransactionSummaryforSourceBid extends BaseClass{


	/**
	 * Logger for logging step by step Execution
	 */
	public static final Logger log = Logger.getLogger(listTransactionSummaryforSourceBid.class.getName());

	@Test(description="This test would verify response for valid input parameters as MstrCustID in request.",enabled=true)
	public static void MTC001_VerifyResponseForValid_MstrCustID_accept_JSON() throws IOException, SQLException, InterruptedException {			

	
		try {
			
			//Global Variables
			String servdbConnectionString = FrameworkConfigConstants.propertyReader().getProperty("servTestDatabase");
			String oracleDriver=FrameworkConfigConstants.propertyReader().getProperty("oracleConnectionDriver");
			String serTestUserName = FrameworkConfigConstants.propertyReader().getProperty("servTestUsername");
			String serTestPassword = FrameworkConfigConstants.propertyReader().getProperty("servTestPassword");
			String AMConnection=FrameworkConfigConstants.propertyReader().getProperty("AMConnection");
			
			
			//Taking Values from AM in List
			List<String> jsonBody = new ArrayList<String>();	
			List<String> URI = new ArrayList<String>();
			List<String> preQuery = new ArrayList<String>();	
			List<String> validationQuery = new ArrayList<String>();
			
			Map<String, List<String>> AMdata= DatabaseConnection.getDatafromAM(AMConnection);
			
			for (String key : AMdata.keySet()) {

				if(key.contains("JsonBody")) {
					jsonBody= AMdata.get(key);

				}
				if(key.contains("URI")) {
					URI= AMdata.get(key);

				}
				if(key.contains("preQuery")) {
					preQuery= AMdata.get(key);

				}
				if(key.contains("validationQuery")) {
					validationQuery= AMdata.get(key);

				}				
			}
			
			int a=0;			
			int iterationCount = preQuery.size();
			for(a=0;a<iterationCount;a++) {
				
			//Start Report for the iteration
			reporter.appendChild(ExtentTestManager.startTest("Iteration " .concat(Integer.toString(a)), "Iteration "+Integer.toString(a)+" verify response for valid input parameters as MstrCustID"));
			
			/*
			 * Pre Query Required 
			 */
			DatabaseConnection.OpenDBConnection(servdbConnectionString, oracleDriver, serTestUserName,serTestPassword);
			log.info("Database Open Connection for sqlDB");

			//fetch single value in string below
			String CustID = DatabaseConnection.RunQuerywithIndex(preQuery.get(a), 1);

			//DatabaseConnection.CloseDBConnection();
			log.info("Database Close Connection for sqlDB");

			//Taking URI from Automation Management
			RestAssured.baseURI = URI.get(a);
			ExtentTestManager.getTest().log(LogStatus.INFO, "URI is: " +RestAssured.baseURI);
			
			//Taking JSON Body from AutomationManagement
			String JsonBody =jsonBody.get(a);

			//Replacing JSON Body with prequery value
			JsonBody = JsonBody.replace("MASTER_CUST_ID", CustID);
			ExtentTestManager.getTest().log(LogStatus.INFO, "Body is: " +JsonBody);

		
			//Hitting POST Request
			ValidatableResponse response = (ValidatableResponse) RestAssured.given()
					.when().
					header("Content-Type", "application/json").header("accept","application/json")
					.body(JsonBody).post()
					.then();


			//Taking Response as a string
			String resJson = response.extract().asString();
			log.info("JSON Response is "+resJson);

			ExtentTestManager.getTest().log(LogStatus.INFO, "Response is: " +resJson);
			response.statusCode(200);
			ExtentTestManager.getTest().log(LogStatus.PASS, "Have verified the Status Code Successfully !!");

			
			//Json deserialization and validation query part
			JSONObject objectResponse = new JSONObject(resJson);
			
			//First list parent object
			String listTransactionSummaryResponse = objectResponse.optString("listTransactionSummaryResponse");
			JSONObject objectMultipleCustomer = new JSONObject(listTransactionSummaryResponse);

			//first object under list
			Boolean isError = objectMultipleCustomer.getBoolean("isError");
			System.out.println(isError);


			JSONArray jArrayMultipleCustomer = new JSONArray();
			//empty list
			JSONArray jArraysingleCustomer = new JSONArray();
			JSONObject objectSingleCustomer=null;

			jArrayMultipleCustomer = objectMultipleCustomer.getJSONArray("customer");


			//check for multiple array
			System.out.println(jArrayMultipleCustomer .length());

			if(jArrayMultipleCustomer .length()==1)
			{
				
				objectSingleCustomer  = jArrayMultipleCustomer.getJSONObject(0).getJSONObject("customer"); 

				//simply put obj into jsonArray
				jArraysingleCustomer.put(objectSingleCustomer);
				jArrayMultipleCustomer = jArraysingleCustomer;
				
			}

			//calling query functions
			List<DTO> record = new ArrayList<DTO>();

			DatabaseConnection.OpenDBConnection(servdbConnectionString, oracleDriver, serTestUserName,serTestPassword);
			record  = DatabaseConnection.getData(validationQuery.get(a));		
			DatabaseConnection.CloseDBConnection();

			//db foreach loop
			for(DTO vendor:record)
			{
				//response foreach loop
				for(int i = 0; i < jArrayMultipleCustomer .length(); i++)
				{
					JSONObject objectCustomerElements = jArrayMultipleCustomer.getJSONObject(i);

					//matching condition
					if(vendor.getMasterCustid().equals(objectCustomerElements.getString("mstrCustId")))
					{

						ExtentTestManager.getTest().log(LogStatus.INFO, "checkpoint for MasterCustomer ID 1 - 	EXPECTED = " + vendor.getMasterCustid() + " ACTUAL = " +   objectCustomerElements.getString("mstrCustId"));
						Assert.assertEquals(vendor.getMasterCustid(),  objectCustomerElements.getString("mstrCustId"));
						ExtentTestManager.getTest().log(LogStatus.PASS, "checkpoint for MasterCustomer ID 1 "  );

					}

				}

			}
			
		}
		} catch (JSONException e) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, "MTC001 failed " + e.getMessage());
			log.error("MTC001 failed");			
			e.printStackTrace();

		} 		

	}

	@Test(description="This test would verify response for valid input parameters as MstrCustID in request.",enabled=true)
	public static void MTC002_VerifyResponseForValid_MstrCustID_accept_XML() throws IOException, SQLException, InterruptedException {			


		try {
			
			//Global Variables
			String servdbConnectionString = FrameworkConfigConstants.propertyReader().getProperty("servTestDatabase");
			String oracleDriver=FrameworkConfigConstants.propertyReader().getProperty("oracleConnectionDriver");
			String serTestUserName = FrameworkConfigConstants.propertyReader().getProperty("servTestUsername");
			String serTestPassword = FrameworkConfigConstants.propertyReader().getProperty("servTestPassword");
			String AMConnection=FrameworkConfigConstants.propertyReader().getProperty("AMConnection");
			
			
			//Taking Values from AM in List
			List<String> jsonBody = new ArrayList<String>();	
			List<String> URI = new ArrayList<String>();
			List<String> preQuery = new ArrayList<String>();	
			List<String> validationQuery = new ArrayList<String>();
			Map<String, List<String>> AMdata= DatabaseConnection.getDatafromAM(AMConnection);
			
			for (String key : AMdata.keySet()) {

				if(key.contains("JsonBody")) {
					jsonBody= AMdata.get(key);

				}
				if(key.contains("URI")) {
					URI= AMdata.get(key);

				}
				if(key.contains("preQuery")) {
					preQuery= AMdata.get(key);

				}
				if(key.contains("validationQuery")) {
					validationQuery= AMdata.get(key);

				}				

			}
			
			int a=0;			
			int iterationCount =preQuery.size();
			for(a=0;a<iterationCount;a++) {
				
				reporter.appendChild(ExtentTestManager.startTest("Iteration " .concat(Integer.toString(a))));
			
			/**
			 * PRE Query Required 
			 */

			DatabaseConnection.OpenDBConnection(servdbConnectionString, oracleDriver, serTestUserName,serTestPassword);
			log.info("Database Open COnnection for sqlDB");

			//fetch single value in string below
			String CustID = DatabaseConnection.RunQuerywithIndex(preQuery.get(a), 1);

			DatabaseConnection.CloseDBConnection();
	

			log.info("Database Close Connection for Automation Management");
			DatabaseConnection.CloseDBConnection();

			                     		
			RestAssured.baseURI = URI.get(a);
			ExtentTestManager.getTest().log(LogStatus.INFO, "URI is: " +RestAssured.baseURI);
			
			String xmlBody =jsonBody.get(a);
			xmlBody = xmlBody.replace("MASTER_CUST_ID", CustID);


			
			ExtentTestManager.getTest().log(LogStatus.INFO, "Body is: " +xmlBody);


			ValidatableResponse response = (ValidatableResponse) RestAssured.given().when().
					header("Content-Type", "application/json").header("accept","application/xml")
					.body(xmlBody).post().then();


			String resXml = response.extract().asString();	
			log.info("JSON Response is "+resXml);

			ExtentTestManager.getTest().log(LogStatus.INFO, "Response is: " + resXml);
			response.statusCode(200);
			ExtentTestManager.getTest().log(LogStatus.PASS, "Have verified the Status Code Successfully !!");			

			List<DTO> record = new ArrayList<DTO>();

			//XML deserialization and validation query part
			JAXBContext jaxbContext = JAXBContext.newInstance(ResponseXML.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(resXml);
			ResponseXML temp = (ResponseXML) jaxbUnmarshaller.unmarshal(reader);

			System.out.println("temp is "+temp);    


			//calling query functions

			DatabaseConnection.OpenDBConnection(servdbConnectionString, oracleDriver, serTestUserName,serTestPassword);
			record  = DatabaseConnection.getData(validationQuery.get(a));		
			DatabaseConnection.CloseDBConnection();

			//db foreach loop
			for(DTO d1:record)
			{
				//response foreach loop
				for(Customer c1: temp.customer)
				{
				  
					//matching condition
					if(d1.getMasterCustid().equals(c1.getMstrCustId()))
					{
					
						ExtentTestManager.getTest().log(LogStatus.INFO, "checkpoint for MasterCustomer ID 1 - 	EXPECTED = " + d1.getMasterCustid() + " ACTUAL = " +   c1.getMstrCustId());
						Assert.assertEquals(d1.getMasterCustid(),  c1.getMstrCustId());
						ExtentTestManager.getTest().log(LogStatus.PASS, "checkpoint for MasterCustomer ID 1 "  );


						ExtentTestManager.getTest().log(LogStatus.INFO, "checkpoint for MasterCustomer ID 2 - 	EXPECTED = " + d1.getMasterCustid() +  " ACTUAL =  12345"   );
						Assert.assertEquals(d1.getMasterCustid(),  "12345");
						ExtentTestManager.getTest().log(LogStatus.PASS, "checkpoint for MasterCustomer ID 2"  );

					}


				}
			
			}

			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			ExtentTestManager.getTest().log(LogStatus.FAIL, "MTC002 failed " + e.getMessage());
			log.error("MTC002 failed");			
			e.printStackTrace();
		} 

	}


}
