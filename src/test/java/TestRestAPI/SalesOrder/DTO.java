package TestRestAPI.SalesOrder;


public class DTO {
       
       
	public DTO(String masterCustid, String mainCustName)
          {
            
            this.masterCustid = masterCustid;
            this.mainCustName = mainCustName;
            
          }
       
        
       public String getMasterCustid() {
              return masterCustid;
       }


       public void setMasterCustid(String masterCustid) {
              this.masterCustid = masterCustid;
       }


       public String getMainCustName() {
              return mainCustName;
       }


       public void setMainCustName(String mainCustName) {
              this.mainCustName = mainCustName;
       }


       public String masterCustid;
       public String mainCustName;
       
       
       
       
        

}

