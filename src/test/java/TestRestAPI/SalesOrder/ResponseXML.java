package TestRestAPI.SalesOrder;

import javax.xml.bind.annotation.XmlAttribute;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


                
                @XmlRootElement(name = "listTransactionSummaryResponse")
                class ResponseXML {
                                @XmlElement 
                                Customer[] customer;
                                public String isError;
                                
                                }

                                class Customer {
                                     
                                 public String getMstrCustId() {
                                                                return mstrCustId;
                                                }

                                @XmlAttribute()    
                                 String mstrCustId;
                                
                                
                                 
                }

                
                
                