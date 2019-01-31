package com.pearson.sam.bridgeapi.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.Voucher;
import com.pearson.sam.bridgeapi.serviceimpl.AccessCodeRepoService;
import com.pearson.sam.bridgeapi.serviceimpl.VoucherServiceImpl;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.springframework.web.client.RestTemplate;
import java.util.regex.Matcher;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/graphql")
public class CSVFileDownloadController {

	private static final Logger logger = LoggerFactory.getLogger(CSVFileDownloadController.class);

	@Autowired
	private SpringTemplateEngine springTemplateEngine;

	@Autowired
	private AccessCodeRepoService service;

	@Autowired
	private VoucherServiceImpl voucherService;

	@RequestMapping(value = "/downloadAccessCodeCSV", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE })
	public void downloadAccessCodeCSV(HttpServletResponse response, @RequestBody Map<String, String> payload)
			throws IOException {

		AccessCodes req = new AccessCodes();
		req.setBatch(payload.get("batch"));

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment;filename=accesscodes.csv");
		addHeaders(response);
		ServletOutputStream out = response.getOutputStream();
		List<AccessCodes> generateAccessCodes = service.getAccessCodesFromBatch(req.getBatch());
		List<String> codeList = new ArrayList<>();
		for (AccessCodes acCode : generateAccessCodes) {
			codeList.add(acCode.getAccessCode() + "," + payload.get("type") + "," + payload.get("title") + ","
					+ acCode.getType() + "," + acCode.getBatch() + "," + acCode.getCreatedBy() + ","
					+ acCode.getDateCreated());
		}
		String headers = "Access Code ,Product, Title, Code Type, Batch ID, Batch Created By,Batch Created On"
				+ System.getProperty("line.separator");

		String codesCommaSeparated = headers + String.join(System.getProperty("line.separator"), codeList);
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(codesCommaSeparated.getBytes("UTF-8"));
			out.write(codesCommaSeparated.getBytes(), 0, codesCommaSeparated.length());
		} catch (IOException io) {
			//logger.error("Unable to download the file for batch:" + req.getBatch());
			//logger.error("Unable to download the file due to error:" + io.getMessage());
		} catch (Exception ex) {
			//logger.error("Unable to download the file for batch:" + req.getBatch());
			//logger.error("Unable to download the file due to error:" + ex.getMessage());

		} finally {
			try {
				in.close();
				out.flush();
				out.close();
			} catch (IOException io) {
				//logger.error("Unable to close the I/O resources due to error:" + io.getMessage());
			} catch (Exception ex) {
				//logger.error("Unable to close the I/O resources due to error:" + ex.getMessage());
			}
		}
	}

	@RequestMapping(value = "/downloadVoucherCodeCSV", method = RequestMethod.POST )
	public void downloadVoucherCSV(HttpServletResponse response, @RequestBody Map<String, String> payload)
			throws IOException {

		Voucher req = new Voucher();
		req.setBatch(payload.get("batch"));

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment;filename=voucher.csv");
		addHeaders(response);
		ServletOutputStream out = response.getOutputStream();
		List<Voucher> generateAccessCodes = voucherService.getVoucherFromBatch(req.getBatch());
		List<String> codeList = new ArrayList<>();
		for (Voucher vCode : generateAccessCodes) {
			codeList.add(vCode.getVoucherCode() + "," + vCode.getType() + "," + vCode.getBatch() + ","
					+ vCode.getCreatedBy() + "," + vCode.getDateCreated());
		}
		String headers = "Voucher Code ,Voucher Type, Batch ID, Batch Created By,Batch Created On"
				+ System.getProperty("line.separator");

		String codesCommaSeparated = headers + String.join(System.getProperty("line.separator"), codeList);
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(codesCommaSeparated.getBytes("UTF-8"));
			out.write(codesCommaSeparated.getBytes(), 0, codesCommaSeparated.length());
		} catch (IOException io) {
			//logger.error("Unable to download the file for batch:" + req.getBatch());
			//logger.error("Unable to download the file due to error:" + io.getMessage());
		} catch (Exception ex) {
			//logger.error("Unable to download the file for batch:" + req.getBatch());
			//logger.error("Unable to download the file due to error:" + ex.getMessage());

		} finally {
			try {
				in.close();
				out.flush();
				out.close();
			} catch (IOException io) {
				//logger.error("Unable to close the I/O resources due to error:" + io.getMessage());
			} catch (Exception ex) {
				//logger.error("Unable to close the I/O resources due to error:" + ex.getMessage());
			}
		}
	}

	/**
	 * addHeaders.PushbackInputStream
	 * 
	 */
	private void addHeaders(HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		res.setHeader("Access-Control-Max-Age", "3600");
		res.setHeader("Access-Control-Allow-Headers",
				"Content-Type, Accept, X-Requested-With, remember-me, authorization");
	}

	   @RequestMapping(value = "/evaluate", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	    @ResponseBody
	    public ResponseEntity<String> evaluate(@RequestBody @Valid AccessCodeModel accessCodeModel) {
	    	System.out.println("Accesscode: \n"+accessCodeModel.toString());
			String theJsonMessage = getTemplate(accessCodeModel);
			String returnValue = theJsonMessage.replaceAll("\"", Matcher.quoteReplacement("\\\""));
			String postJson = "{\"body\"" + ":" + "\"" + returnValue + "\"}";

			System.out.println(postJson);
			RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Accept", "application/xml");
			HttpEntity<String> entity = new HttpEntity<String>(postJson ,headers);
				
			
			ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8000/evaluate", entity,
					String.class);
			
			System.out.println(response);
			EntitlementResponseModel erm= new EntitlementResponseModel();
			erm = convertXMLtoModel(response.getBody());
	    	//return (ResponseEntity<EntitlementResponseModel> ) erm;
			System.out.println("EntitlementResponseModel:\n" + erm.toString());
	    	return response;
	    	
	    }
	   
	   @RequestMapping(value = "/evaluate1", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	    @ResponseBody
	    public ResponseEntity<EntitlementResponseModel> evaluate1(@RequestBody @Valid AccessCodeModel accessCodeModel) {
	    	System.out.println("Accesscode: \n"+accessCodeModel.toString());
			String theJsonMessage = getTemplate(accessCodeModel);
			String returnValue = theJsonMessage.replaceAll("\"", Matcher.quoteReplacement("\\\""));
			String postJson = "{\"body\"" + ":" + "\"" + returnValue + "\"}";

			System.out.println(postJson);
			RestTemplate restTemplate = new RestTemplate();
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Accept", "application/xml");
			HttpEntity<String> entity = new HttpEntity<String>(postJson ,headers);
				
			
			ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8000/evaluate", entity,
					String.class);
			
			System.out.println(response);
			EntitlementResponseModel erm= new EntitlementResponseModel();
			erm = convertXMLtoModel(response.getBody());
	    	//return (ResponseEntity<EntitlementResponseModel> ) erm;
			System.out.println("EntitlementResponseModel:\n" + erm.toString());
	    	return new ResponseEntity<EntitlementResponseModel>(erm, HttpStatus.OK);
	    	
	    }
//	@RequestMapping(value = "/test", method = RequestMethod.POST, consumes = {
//			MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = {
//					MediaType.ALL_VALUE })
	/*@PostMapping(value = "/test" )
	public String test(HttpServletResponse response,@RequestBody Object obj) throws ParseException, JSONException {
		 Retrieve JSON message with inserted parameter values. 
		Context context = new Context();

		//String theJsonMessage = springTemplateEngine.process("Student_Access_Code", context);
		String theJsonMessage = getTemplate();
		String returnValue = theJsonMessage.replaceAll("\"", Matcher.quoteReplacement("\\\""));
		String postJson = "{\"body\"" + ":" + "\"" + returnValue + "\"}";

		System.out.println(postJson);
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Accept", "application/xml");
		HttpEntity<String> entity = new HttpEntity<String>(postJson ,headers);
		//restTemplate.put(uRL, entity);
		
		
		ResponseEntity<String> response1 = restTemplate.postForEntity("http://localhost:8000/evaluate", entity,
				String.class);
		
		System.out.println(response1);
		response.setContentType("application/xml");
		//addHeaders(response);
		response.addHeader("Decision", "Permit");
		response.setStatus(200); 
		
		
		
		Map<String, String> pinfo = new HashMap<>();
      
        context.setVariable("pinfo", pinfo);
        pinfo.put("lastname", "Jordan");
        pinfo.put("firstname", "Michael");
        pinfo.put("country", "USA");

       String content = springTemplateEngine.process("xml/Student_Access_Code1.xml",context);
       return content;
       
		
		//return "xml/Student_Access_Code1.xml";
		//return returnValue;
	}
*/
	   public EntitlementResponseModel convertXMLtoModel(String response) {
		   EntitlementResponseModel erm= new EntitlementResponseModel();
		
			try {
				
				//Remove response attribute.
				if (response.indexOf(">") >=1) {
					response=response.substring(response.indexOf(">")+1, response.length());
					response= "<Response>" + response;
					}
				SAXReader reader = new SAXReader();
				Document document = reader.read(new StringReader(response));

				Element classElement = document.getRootElement();
				String rootElement =  classElement.getName();
				System.out.println(rootElement);
				
				
				String nodeName="/Response/Result";
				String attributeName="Decision";
				erm.setDecision(getAttributeValue(document, nodeName, attributeName));
			
				nodeName="/Response/Result/AssociatedAdvice/Advice";
				attributeName="AttributeAssignment";
				erm=getAttributeValueMessage(document, nodeName, attributeName, erm);
		
	
//				nodeName="/Response/Result/AssociatedAdvice/Advice";
//				attributeName="AttributeAssignment[@AttributeId = 'http://www.pearson.com/attributes/messagesummary']";
//				erm.setMessagesummary(getAttributeValue(document, nodeName, attributeName));
//	
//				nodeName="/Response/Result/AssociatedAdvice/Advice";
//				attributeName="AttributeAssignment[@AttributeId = 'http://www.pearson.com/attributes/messagetodisplay']";
//				erm.setMessagetodisplay(getAttributeValue(document, nodeName, attributeName));
	
				nodeName="/Response/Result/PolicyIdentifierList";
				attributeName="PolicyIdReference";
				erm.setPolicyidreferences(getAttributeValue(document, nodeName, attributeName));
	
				
			
			} catch (DocumentException e) {
				e.printStackTrace();
			} 
		
			return erm;
		}

	   
	public String getTemplate(AccessCodeModel accessCodeModel) {
		StringWriter outputWriter = new StringWriter();
		try {
			File inputFile = new File("Student_Access_Code.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputFile);

			Element classElement = document.getRootElement();
			String rootElement =  classElement.getName();
			System.out.println(rootElement);
			
			String nodeName="/Request";
			String attributeName="xmlns";
			String attributeValue="urn:oasis:names:tc:xacml:3.0:core:schema:wd-17";
			document.getRootElement().addAttribute(attributeName, attributeValue);
			//addXMLAttribute(document, nodeName, attributeName, attributeValue);
			
			attributeName="CombinedDecision";
			attributeValue="false";
			document.getRootElement().addAttribute(attributeName, attributeValue);
			//addXMLAttribute(document, nodeName, attributeName, attributeValue);
			
			attributeName="ReturnPolicyIdList";
			attributeValue="true";
			document.getRootElement().addAttribute(attributeName, attributeValue);
			//addXMLAttribute(document, nodeName, attributeName, attributeValue);
			
			nodeName="/Request/Attributes[@Category = 'urn:oasis:names:tc:xacml:3.0:attribute-category:action']/Attribute[@AttributeId = 'urn:oasis:names:tc:xacml:1.0:action:action-id']";
			attributeName="AttributeValue";
			//attributeValue="redeem_access_code";
			attributeValue=accessCodeModel.getAction_id().trim().toString();
			replaceXMLContent(document, nodeName, attributeName, attributeValue);

			nodeName="/Request/Attributes[@Category = 'urn:oasis:names:tc:xacml:1.0:subject-category:access-subject']/Attribute[@AttributeId = 'http://www.pearson.com/attributes/role']";
			attributeName="AttributeValue";
			//attributeValue="teacher";
			attributeValue=accessCodeModel.getRole().trim().toString();
			replaceXMLContent(document, nodeName, attributeName, attributeValue);
		
			nodeName="/Request/Attributes[@Category = 'urn:oasis:names:tc:xacml:3.0:group']/Attribute[@AttributeId = 'http://www.pearson.com/attributes/role']";
			attributeName="AttributeValue";
			//attributeValue="teacher";
			attributeValue=accessCodeModel.getRole().trim().toString();
			replaceXMLContent(document, nodeName, attributeName, attributeValue);
		
			nodeName="/Request/Attributes[@Category = 'urn:oasis:names:tc:xacml:3.0:group']/Attribute[@AttributeId = 'http://www.pearson.com/attributes/codetype']";
			attributeName="AttributeValue";
			//attributeValue="Single-use Code";
			attributeValue=accessCodeModel.getCodetype().trim().toString();
			replaceXMLContent(document, nodeName, attributeName, attributeValue);

			nodeName="/Request/Attributes[@Category = 'urn:oasis:names:tc:xacml:3.0:group']/Attribute[@AttributeId = 'http://www.pearson.com/attributes/typeofaccesscode']";
			attributeName="AttributeValue";
			//attributeValue="Individual and Bundle";
			attributeValue=accessCodeModel.getTypeofaccesscode().trim().toString();
			replaceXMLContent(document, nodeName, attributeName, attributeValue);

			nodeName="/Request/Attributes[@Category = 'http://www.pearson.com/category']/Attribute[@AttributeId = 'http://www.pearson.com/attributes/iscodeindatabase']";
			attributeName="AttributeValue";
			//attributeValue="yes";
			attributeValue=accessCodeModel.getIscodeindatabase().trim().toString();
			replaceXMLContent(document, nodeName, attributeName, attributeValue);
			
			nodeName="/Request/Attributes[@Category = 'http://www.pearson.com/category']/Attribute[@AttributeId = 'http://www.pearson.com/attributes/isvoided']";
		
			//attributeValue="no";
			attributeValue=accessCodeModel.getIsvoided().trim().toString();
			replaceXMLContent(document, nodeName, attributeName, attributeValue);

			nodeName="/Request/Attributes[@Category = 'http://www.pearson.com/category']/Attribute[@AttributeId = 'http://www.pearson.com/attributes/userprofilependingstatus']";

			//attributeValue="no";
			attributeValue=accessCodeModel.getUserprofilependingstatus().trim().toString();
			replaceXMLContent(document, nodeName, attributeName, attributeValue);

			nodeName="/Request/Attributes[@Category = 'http://www.pearson.com/category']/Attribute[@AttributeId = 'http://www.pearson.com/attributes/numberofactivations']";

			//attributeValue="0";
			attributeValue=accessCodeModel.getNumberofactivations().trim().toString();
			replaceXMLContent(document, nodeName, attributeName, attributeValue);

			nodeName="/Request/Attributes[@Category = 'http://www.pearson.com/category']/Attribute[@AttributeId = 'http://www.pearson.com/attributes/lastactivatedtime']";

			//attributeValue="6";
			attributeValue=accessCodeModel.getLastactivatedtime().trim().toString();
			replaceXMLContent(document, nodeName, attributeName, attributeValue);

			nodeName="/Request/Attributes[@Category = 'http://www.pearson.com/category']/Attribute[@AttributeId = 'http://www.pearson.com/attributes/alreadyexpired']";

			//attributeValue="no";
			attributeValue=accessCodeModel.getAlreadyexpired().trim().toString();
			replaceXMLContent(document, nodeName, attributeName, attributeValue);

			nodeName="/Request/Attributes[@Category = 'http://www.pearson.com/category']/Attribute[@AttributeId = 'http://www.pearson.com/attributes/activatedlasttimebycurrentloggedinuser']";

			//attributeValue="no";
			attributeValue=accessCodeModel.getActivatedlasttimebycurrentloggedinuser().trim().toString();
			replaceXMLContent(document, nodeName, attributeName, attributeValue);

			nodeName="/Request/Attributes[@Category = 'http://www.pearson.com/category']/Attribute[@AttributeId = 'http://www.pearson.com/attributes/processstatus']";

			//attributeValue="Valid";
			attributeValue=accessCodeModel.getProcessstatus().trim().toString();
			replaceXMLContent(document, nodeName, attributeName, attributeValue);

			
			// Pretty print the document to System.out
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer;
			writer = new XMLWriter(System.out, format);
			writer.write(document);

			// Dom4j automatically writes using UTF-8, unless otherwise specified.

			writer = new XMLWriter(outputWriter, format);
			writer.write(document);
			outputWriter.close();
			writer.close();
			outputWriter.toString();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputWriter.toString();
	}

	
	private void addXMLAttribute(Document document, String nodeName, String attributeName, String attributeValue) {
		/*	List<Node> nodes = document.selectNodes(
					"/Request/Attributes[@Category = 'urn:oasis:names:tc:xacml:3.0:attribute-category:action']/Attribute[@AttributeId = 'urn:oasis:names:tc:xacml:1.0:action:action-id']");
	*/
			List<Node> nodes = document.selectNodes(nodeName);

			for (Node node : nodes) {
				Element element = (Element) node;
				Iterator<Element> iterator = element.elementIterator(attributeName);

				while (iterator.hasNext()) {
					Element marksElement = (Element) iterator.next();
					marksElement.setText(attributeValue);
					System.out.println("Element Added:"+marksElement.getText());
				}
			}
		}
	
	private void replaceXMLContent(Document document, String nodeName, String attributeName, String attributeValue) {
	/*	List<Node> nodes = document.selectNodes(
				"/Request/Attributes[@Category = 'urn:oasis:names:tc:xacml:3.0:attribute-category:action']/Attribute[@AttributeId = 'urn:oasis:names:tc:xacml:1.0:action:action-id']");
*/
		List<Node> nodes = document.selectNodes(nodeName);

		for (Node node : nodes) {
			Element element = (Element) node;
			
			Iterator<Element> iterator = element.elementIterator(attributeName);

			while (iterator.hasNext()) {
				Element marksElement = (Element) iterator.next();
				marksElement.setText(attributeValue);
				System.out.println("Element Changed:"+marksElement.getText());
			}
		}
	}
	
	private String getAttributeValue(Document document, String nodeName, String attributeName) {
		String attributeValue="";
			List<Node> nodes = document.selectNodes(nodeName);

			for (Node node : nodes) {
				Element element = (Element) node;
				
				Iterator<Element> iterator = element.elementIterator(attributeName);

				while (iterator.hasNext()) {
					Element marksElement = (Element) iterator.next();
			
					//System.out.println("Element Changed:"+marksElement.getText());
					attributeValue=marksElement.getText().trim().toString();
				}
			}
			return attributeValue;
		}

	private EntitlementResponseModel getAttributeValueMessage(Document document, String nodeName, String attributeName, EntitlementResponseModel erm) {
		String attributeValue="";
			List<Node> nodes = document.selectNodes(nodeName);

			for (Node node : nodes) {
				Element element = (Element) node;
				
				Iterator<Element> iterator = element.elementIterator(attributeName);

				while (iterator.hasNext()) {
					Element marksElement = (Element) iterator.next();
					erm.setMessagetype(marksElement.getText().trim().toString());
					marksElement = (Element) iterator.next();
					erm.setMessagesummary(marksElement.getText().trim().toString());
					marksElement = (Element) iterator.next();
					erm.setMessagetodisplay(marksElement.getText().trim().toString());
					break;
					
				}
			}
			return erm;
		}
	
}
