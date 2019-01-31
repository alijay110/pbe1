package com.pearson.sam.bridgeapi.controller;


public class EntitlementResponseModel {
	
	private String decision;
	private String messagetype;
	private String messagesummary;
	private String messagetodisplay;
	private String policyidreferences;
	

	public String toString() 
    { 
        return 	" decision: "+decision+	
        		"\n messagetype: "+messagetype+
        		"\n messagesummary: "+messagesummary+
        		"\n messagetodisplay: "+messagetodisplay+
        		"\n policyidreferences: "+policyidreferences;
    }


	public String getDecision() {
		return decision;
	}


	public void setDecision(String decision) {
		this.decision = decision;
	}


	public String getMessagetype() {
		return messagetype;
	}


	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}


	public String getMessagesummary() {
		return messagesummary;
	}


	public void setMessagesummary(String messagesummary) {
		this.messagesummary = messagesummary;
	}


	public String getMessagetodisplay() {
		return messagetodisplay;
	}


	public void setMessagetodisplay(String messagetodisplay) {
		this.messagetodisplay = messagetodisplay;
	}


	public String getPolicyidreferences() {
		return policyidreferences;
	}


	public void setPolicyidreferences(String policyidreferences) {
		this.policyidreferences = policyidreferences;
	} 
	
	
	/*<Response xmlns="urn:oasis:names:tc:xacml:3.0:core:schema:wd-17">
    <Result>
        <Decision>Permit</Decision>
        <Status>
            <StatusCode Value="urn:oasis:names:tc:xacml:1.0:status:ok"/>
        </Status>
        <AssociatedAdvice>
            <Advice AdviceId="permit-assist-advice" >
                <AttributeAssignment  AttributeId="http://www.pearson.com/attributes/messagetype" DataType="http://www.w3.org/2001/XMLSchema#string">Success</AttributeAssignment>
                <AttributeAssignment  AttributeId="http://www.pearson.com/attributes/messagesummary" DataType="http://www.w3.org/2001/XMLSchema#string">Confirmation</AttributeAssignment>
                <AttributeAssignment  AttributeId="http://www.pearson.com/attributes/messagetodisplay" DataType="http://www.w3.org/2001/XMLSchema#string">Scenario: 1, 9: (take to the next modal with confirmation
						message and products details)
					</AttributeAssignment>
            </Advice>
        </AssociatedAdvice>
        <PolicyIdentifierList>
            <PolicyIdReference>TeacherAccessCodePolicy_V1</PolicyIdReference>
        </PolicyIdentifierList>
    </Result>
</Response>*/
	}
