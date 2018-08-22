package com.meetingbot.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifyOptions;

@Component
public class NlcClassifier {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private NaturalLanguageClassifier nlcService;
	
	private static final double NLC_HURDLE = 0.5;
	private static final String NLC_CLASS_NAME = "conclusion";
	
	private static String NLC_USERNAME;
	private static String NLC_PASSWORD;
	private static String NLC_ENDPOINT;
	private static String NLC_CLASSIFIER_ID;
	@Value("${nlc.username}")
	public void setNLC_USERNAME(String nLC_USERNAME) {
		NLC_USERNAME = nLC_USERNAME;
	}
	@Value("${nlc.password}")
	public void setNLC_PASSWORD(String nLC_PASSWORD) {
		NLC_PASSWORD = nLC_PASSWORD;
	}
	@Value("${nlc.endpoint}")
	public void setNLC_ENDPOINT(String nLC_ENDPOINT) {
		NLC_ENDPOINT = nLC_ENDPOINT;
	}
	@Value("${nlc.classifierid}")
	public void setNLC_CLASSIFIER_ID(String nLC_CLASSIFIER_ID) {
		NLC_CLASSIFIER_ID = nLC_CLASSIFIER_ID;
	}
	/**
	 * NLC Classifier 생성
	 * 
	 * @return
	 */
	private NaturalLanguageClassifier getNlcClassifier() {
		if (this.nlcService == null) {
			this.nlcService = new NaturalLanguageClassifier();
			this.nlcService.setUsernameAndPassword(NLC_USERNAME, NLC_PASSWORD);
			this.nlcService.setEndPoint(NLC_ENDPOINT);
		}
		return this.nlcService;
	}

	/**
	 * NLC Classify
	 * 
	 * @param classifyText
	 */
	public List<Map<String, Object>> classify(String classifyText) {
		logger.info("NLC Classifier started!");

		List<Map<String, Object>> classfyResultList = new ArrayList<Map<String, Object>>();
		
		String[] splitTextArray = classifyText.split("\\.");
		for(String splitText :splitTextArray) {
			if(splitText.length() < 3) {
				continue;
			}
			ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
					.classifierId(NLC_CLASSIFIER_ID)
					.text(splitText)
					.build();
			Classification classification = this.getNlcClassifier().classify(classifyOptions).execute();
			System.out.println("확인");
			System.out.println(classification);
			
			double confidence = classification.getClasses().get(0).getConfidence(); 
			String className = classification.getTopClass();
			Map<String, Object> classifyResultMap = new HashMap<String, Object>();
			classifyResultMap.put("confidence", confidence);
			classifyResultMap.put("className", className);
			classifyResultMap.put("text", splitText);
			classfyResultList.add(classifyResultMap);
		}
		
		//여러개의 문장을 던지니까 그중에서 제일 높은 탑컨피던스인 문장을 뽑아내기위해서
		Collections.sort(classfyResultList, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o2, Map<String, Object> o1) {
				int compare = o1.get("className").toString().compareTo(o2.get("className").toString());
				if(compare < 0) {
					return 1;
				} else if(compare > 0) {
					return -1;
				} else {
					if((double)o1.get("confidence") > (double)o2.get("confidence")) {
						return 1;
					} else if((double)o1.get("confidence") < (double)o2.get("confidence")) {
						return -1;
					} else {
						return 0;
					}
				}
			}
		});
		
		int confidentCount = 0;
		for(Map<String, Object> classifyResultMap : classfyResultList) {
			if(NLC_HURDLE < (double)classifyResultMap.get("confidence")
					&& NLC_CLASS_NAME.equals(classifyResultMap.get("className").toString())) {
				confidentCount++;
			}
		}
		int length = 3;
		if(confidentCount == 0) {
			length = 1;
		} else if(confidentCount < 3) {
			length = confidentCount;
		}
		return classfyResultList.subList(0, length);
	}
	
}
