package com.meetingbot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.Context;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;

@Component
public class assistantUnit {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static String ASSISTANT_USERNAME;
	private static String ASSISTANT_PASSWORD;
	private static String ASSISTANT_ENDPOINT;
	private static String ASSISTANT_WORKSPACE_ID;
	@Value("${assistant.username}")
	public void setASSISTANT_USERNAME(String aSSISTANT_USERNAME) {
		ASSISTANT_USERNAME = aSSISTANT_USERNAME;
	}
	@Value("${assistant.password}")
	public void setASSISTANT_PASSWORD(String aSSISTANT_PASSWORD) {
		ASSISTANT_PASSWORD = aSSISTANT_PASSWORD;
	}
	@Value("${assistant.endpoint}")
	public void setASSISTANT_ENDPOINT(String aSSISTANT_ENDPOINT) {
		ASSISTANT_ENDPOINT = aSSISTANT_ENDPOINT;
	}
	@Value("${assistant.workspaceid}")
	public void setASSISTANT_WORKSPACE_ID(String aSSISTANT_WORKSPACE_ID) {
		ASSISTANT_WORKSPACE_ID = aSSISTANT_WORKSPACE_ID;
	}

	private static Context contextTest = new Context();

	
	/**
	 * assistant 시작
	 * @return
	 */
	public String start(String inputText) {

		logger.info("assistant Started!");
		
		Assistant assistantService = new Assistant("2018-07-10");
		assistantService.setEndPoint(ASSISTANT_ENDPOINT);
	
		assistantService.setUsernameAndPassword(ASSISTANT_USERNAME, ASSISTANT_PASSWORD);

		InputData input = new InputData.Builder(inputText).build();
		MessageOptions options = new MessageOptions.Builder(ASSISTANT_WORKSPACE_ID)
		  .input(input)
		  .context(contextTest)
		  .build();

		MessageResponse response = assistantService.message(options).execute();
		
		contextTest.putAll(response.getContext());
		
//		System.out.println(response.getContext());
		
		if(response.getContext().get("redirect") != null && response.getContext().get("redirect").equals("redirect")) {
			return response.getOutput().getText().get(0) + "redirect";
		}
		else if(response.getContext().get("study") != null && response.getContext().get("study").equals("study")) {
			return "study";
		}
		else {
			return response.getOutput().getText().get(0);
		}

	}

}
