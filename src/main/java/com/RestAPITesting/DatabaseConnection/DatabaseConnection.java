package com.RestAPITesting.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import TestRestAPI.SalesOrder.DTO;
import TestRestAPI.SalesOrder.listTransactionSummaryforSourceBid;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

public class DatabaseConnection {

	protected static Connection con = null;
	protected static Statement stmt = null;
	protected static ResultSet result = null;
	protected static ResultSetMetaData rsmd= null;
	protected static CallableStatement myStmt=null;
	
	/** = 
	 * Logger for logging step by step Execution
	 */
	public static final Logger log = Logger.getLogger(DatabaseConnection.class.getName());
	//Opening DB connection
	public static void OpenDBConnection(String dbUrl, String driver, String username, String password){
		log.info("Database Open Connection ");

		//Making connection to DB
		try {
			Class.forName(driver);
			
			con = DriverManager.getConnection(dbUrl, username, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Closing DB connection
	public static void CloseDBConnection(){
		try {
			//Closing DB connection
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Executing query and fetching data from DB
	public static ResultSet GetRecords(String query){
		
		//Executing query and saving result into result set
		try {
			stmt = con.createStatement();
			
			result = stmt.executeQuery(query);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	
	}
	
    //Method to open , get query and close connection
    public static String RunQuerywithIndex(String Query, int irowindex) throws IOException {
		String strRequiredRecord = null;
		try {
			
			
			stmt = con.createStatement();
			// Execute the given query
			result = stmt.executeQuery(Query);
			
			// Fetch the value at given index
			while(DatabaseConnection.result.next())
			{
				strRequiredRecord = result.getString(irowindex);
			}
			
			

		} catch (SQLException e1) {
			System.out.println("Query Execution Error");
			e1.printStackTrace();
		}

		return strRequiredRecord;
	}
    
  //Method to open , get query and close connection
    public static List<DTO> getData(String Query) throws IOException
    {
		String r1 = null;
		String r2 = null;
		
		List<DTO> list = new ArrayList<DTO>();
		
		
		try {
			
			//OpenDBConnection("odstest");
			
			stmt = con.createStatement();
			// Execute the given query
			result = stmt.executeQuery(Query);
		
			
			
			while (result.next())
			{
					r1 = result.getString(1);
					r2 = result.getString(2);
					
				DTO user  = new DTO(r1,r2);
				
				 list.add(user);
			        
			       
			    }
			

		}
		catch (SQLException e1) {
			System.out.println("Query Execution Error");
			e1.printStackTrace();
		}
		return list;
		
		

	}
    
    
	
    public static ArrayList<Map<String, Object>> getRows(String query)
        throws SQLException
    {
    	
    	con = DriverManager.getConnection(
				"jdbc:sqlserver://feibustst3-db\\feibus_tst3;databaseName=AutomationQA", "AutomationQA", "Aut0mat10nQA");

        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery(query);
        ResultSetMetaData rsmd = rset.getMetaData();
        int columncount = rsmd.getColumnCount();
        
        ArrayList<Map<String, Object>> queryResult = new ArrayList<Map<String, Object>>();
        while (rset.next()) {
            Map<String, Object> row = new HashMap<String, Object>();
            for (int i = 1; i <= columncount; i++) {
                row.put(rset.getMetaData().getColumnName(i), rset.getObject(i));
            }
            queryResult.add(row);
        }
            
        return queryResult;
    }
    
	

    //Get Data FRom Automation Management
    public static  Map<String, List<String>> getDatafromAM(String  Query) throws SQLException{
    	 
		log.info("Database Open Connection for Automation Management");
    	con = DriverManager.getConnection("jdbc:sqlserver://feibustst3-db\\feibus_tst3;databaseName=AutomationQA", "AutomationQA", "Aut0mat10nQA");

    	 Statement stmt = con.createStatement();
         ResultSet rset = stmt.executeQuery(Query);
         ResultSetMetaData rsmd = rset.getMetaData();
         int AMcolumncount = rsmd.getColumnCount();
         int mapKeys=0;
         
         List<String> preQuery = null;
         List<String> columnNames = new LinkedList<String>();
         Map<String,List<String>> columnNameToValuesMap=new HashMap<String, List<String>>();

          for (int i = 1; i <= AMcolumncount; i++) {
                 String columnName = rsmd.getColumnName(i);
                 columnNames.add(columnName);

                 //Load the Map initially with keys(columnnames) and empty list
                 columnNameToValuesMap.put(columnName, new ArrayList());
          }

          try {
        	//Iterate the resultset for each row
             while (rset.next()) { 
            	 
               for (String columnName : columnNames) {
                  //Get the list mapped to column name
                  List<String> columnDataList = columnNameToValuesMap.get(columnName);

                   //Add the current row's column data to list
                   columnDataList.add(rset.getString(columnName));

                   //add the updated list of column data to the map now
                   columnNameToValuesMap.put(columnName, columnDataList);
                 }
//             for (String key : columnNameToValuesMap.keySet()) {
//        	   if(key.contains("JsonBody")) {
//        		  preQuery= columnNameToValuesMap.get(key);
//        		   
//        	   }
//               System.out.println(key + " = " + columnNameToValuesMap.get(key));           
//         		
//           }

             } 
          } catch (SQLException e) {
             e.printStackTrace();
         }
          
        
          
		return columnNameToValuesMap;
   	
    }
    
	
	private static void close(Connection myConn, Statement myStmt) throws SQLException {
		if (myStmt != null) {
			myStmt.close();
		}

		if (myConn != null) {
			myConn.close();
		}
		log.info("Database Close Connection ");
	}
}
