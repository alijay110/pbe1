package com.pearson.sam.bridgeapi.controller;


public class AccessCodeModel {
	
	private String action;
	private String action_id;
	private String access_subject;
	private String role;
	private String codetype;
	private String typeofaccesscode;
	private String iscodeindatabase;
	private String isvoided;
	
	private String userprofilependingstatus;
	private String numberofactivations;
	private String lastactivatedtime;
	private String alreadyexpired;
	private String activatedlasttimebycurrentloggedinuser;
	private String processstatus;
	
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getAction_id() {
		return action_id;
	}
	public void setAction_id(String action_id) {
		this.action_id = action_id;
	}
	public String getAccess_subject() {
		return access_subject;
	}
	public void setAccess_subject(String access_subject) {
		this.access_subject = access_subject;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getCodetype() {
		return codetype;
	}
	public void setCodetype(String codetype) {
		this.codetype = codetype;
	}
	public String getTypeofaccesscode() {
		return typeofaccesscode;
	}
	public void setTypeofaccesscode(String typeofaccesscode) {
		this.typeofaccesscode = typeofaccesscode;
	}
	public String getIscodeindatabase() {
		return iscodeindatabase;
	}
	public void setIscodeindatabase(String iscodeindatabase) {
		this.iscodeindatabase = iscodeindatabase;
	}
	public String getIsvoided() {
		return isvoided;
	}
	public void setIsvoided(String isvoided) {
		this.isvoided = isvoided;
	}
	public String getUserprofilependingstatus() {
		return userprofilependingstatus;
	}
	public void setUserprofilependingstatus(String userprofilependingstatus) {
		this.userprofilependingstatus = userprofilependingstatus;
	}
	public String getNumberofactivations() {
		return numberofactivations;
	}
	public void setNumberofactivations(String numberofactivations) {
		this.numberofactivations = numberofactivations;
	}
	public String getLastactivatedtime() {
		return lastactivatedtime;
	}
	public void setLastactivatedtime(String lastactivatedtime) {
		this.lastactivatedtime = lastactivatedtime;
	}
	public String getAlreadyexpired() {
		return alreadyexpired;
	}
	public void setAlreadyexpired(String alreadyexpired) {
		this.alreadyexpired = alreadyexpired;
	}
	public String getActivatedlasttimebycurrentloggedinuser() {
		return activatedlasttimebycurrentloggedinuser;
	}
	public void setActivatedlasttimebycurrentloggedinuser(String activatedlasttimebycurrentloggedinuser) {
		this.activatedlasttimebycurrentloggedinuser = activatedlasttimebycurrentloggedinuser;
	}
	public String getProcessstatus() {
		return processstatus;
	}
	public void setProcessstatus(String processstatus) {
		this.processstatus = processstatus;
	}

	public String toString() 
    { 
        return 	" action_id: "+action_id+	
        		"\n role: "+role+
        		"\n codetype: "+codetype+
        		"\n typeofaccesscode: "+typeofaccesscode+
        		"\n iscodeindatabase: "+iscodeindatabase+
        		"\n isvoided: "+isvoided+
        		"\n userprofilependingstatus: "+userprofilependingstatus+
        		"\n numberofactivations: "+numberofactivations+
        		"\n lastactivatedtime: "+lastactivatedtime+
        		"\n alreadyexpired: "+alreadyexpired+
        		"\n activatedlasttimebycurrentloggedinuser: "+activatedlasttimebycurrentloggedinuser+
        		"\n processstatus: "+processstatus;
    } 
	
	/*
	
	<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"true\">
		    <Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\">
		        <Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" IncludeInResult=\"false\">
		            <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">redeem_access_code</AttributeValue>
		        </Attribute>
		    </Attributes>
		      <Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">
		            <Attribute AttributeId=\"http://www.pearson.com/attributes/role\" IncludeInResult=\"false\">
		                <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">student</AttributeValue>
		            </Attribute>
		        </Attributes>
		  <Attributes Category=\"urn:oasis:names:tc:xacml:3.0:group\">
		        <Attribute AttributeId=\"http://www.pearson.com/attributes/role\" IncludeInResult=\"false\">
		            <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">student</AttributeValue>
		            </Attribute>
		            
		               <Attribute AttributeId=\"http://www.pearson.com/attributes/codetype\" IncludeInResult=\"false\">
		            <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">In-book code</AttributeValue>
		            </Attribute>
		            
		              <Attribute AttributeId=\"http://www.pearson.com/attributes/typeofaccesscode\" IncludeInResult=\"false\">
		             <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Individual</AttributeValue>
		             </Attribute>
		    </Attributes>
		 
		  <Attributes Category=\"http://www.pearson.com/category\">
		      
		        
		        <Attribute AttributeId=\"http://www.pearson.com/attributes/iscodeindatabase\" IncludeInResult=\"false\">
		          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">yes</AttributeValue>
		        </Attribute>
		        
		        <!--
		        <Attribute AttributeId=\"http://www.pearson.com/attributes/codetype\" IncludeInResult=\"false\">
		          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">In-book code</AttributeValue>
		        </Attribute>
		  
		         <Attribute AttributeId=\"http://www.pearson.com/attributes/typeofaccesscode\" IncludeInResult=\"false\">
		          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Individual</AttributeValue>
		        </Attribute>
		          -->
		          
		          
		          <Attribute AttributeId=\"http://www.pearson.com/attributes/isvoided\" IncludeInResult=\"false\">
		          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">no</AttributeValue>
		        </Attribute>
		        
		          <Attribute AttributeId=\"http://www.pearson.com/attributes/userprofilependingstatus\" IncludeInResult=\"false\">
		          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">no</AttributeValue>
		        </Attribute>
		        
		           <Attribute AttributeId=\"http://www.pearson.com/attributes/numberofactivations\" IncludeInResult=\"false\">
		          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">4</AttributeValue>
		        </Attribute>
		        

		          <Attribute AttributeId=\"http://www.pearson.com/attributes/lastactivatedtime\" IncludeInResult=\"false\">
		          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#integer\">6</AttributeValue>
		        </Attribute>
		      
		          <Attribute AttributeId=\"http://www.pearson.com/attributes/alreadyexpired\" IncludeInResult=\"false\">
		          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">no</AttributeValue>
		        </Attribute>

		      <Attribute AttributeId=\"http://www.pearson.com/attributes/activatedlasttimebycurrentloggedinuser\" IncludeInResult=\"false\">
		          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">yes</AttributeValue>
		        </Attribute>


		      <Attribute AttributeId=\"http://www.pearson.com/attributes/processstatus\" IncludeInResult=\"false\">
		          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Invalid</AttributeValue>
		        </Attribute>

		        
		  </Attributes>
		  */

}
