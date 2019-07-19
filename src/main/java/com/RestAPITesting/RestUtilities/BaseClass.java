package com.RestAPITesting.RestUtilities;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.apache.log4j.PropertyConfigurator;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * @author mukundz
 *
 */
public abstract class BaseClass {
   
	/**
	 * 
	 * @param method
	 */
	public static  ExtentTest reporter;
	

	
	
	@BeforeMethod
    public void beforeMethod(Method method) {
		
        //reporter.appendChild(ExtentTestManager.startTest(method.getName().toUpperCase(),method.getAnnotation(Test.class).description()));        
		reporter = ExtentTestManager.startTest(method.getName(),method.getAnnotation(Test.class).description());	
		reporter.assignCategory("SalesOrder");	
		String log4jConfPath="log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
	}
    
	/**
	 * 
	 * @param result
	 */
    @AfterMethod
    protected void afterMethod(ITestResult result)
    {
        if (result.getStatus() == ITestResult.FAILURE) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            ExtentTestManager.getTest().log(LogStatus.SKIP, "Test skipped " + result.getThrowable());
        } else {
            ExtentTestManager.getTest().log(LogStatus.PASS, "Test passed");
        }
        
       // ExtentManager.getReporter().endTest(ExtentTestManager.getTest());
        ExtentManager.getReporter().endTest(reporter);
		ExtentManager.getReporter().flush();
    }
    
    

    
    /**
     * 
     * @param t
     * @return
     */
    protected String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}
