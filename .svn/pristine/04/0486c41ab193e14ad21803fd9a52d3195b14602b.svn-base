package com.meetingbot.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.CategoriesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.ConceptsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.ConceptsResult;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.EntitiesResult;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;

@Component
public class NluAnalyzer {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private NaturalLanguageUnderstanding nluService;
	
	private static  String NLU_VERSION;
	private static  String NLU_USERNAME;
	private static  String NLU_PASSWORD;
	private static  String NLU_ENDPOINT;
	@Value("${nlu.version}")
	public void setNLU_VERSION(String nLU_VERSION) {
		NLU_VERSION = nLU_VERSION;
	}
	@Value("${nlu.username}")
	public void setNLU_USERNAME(String nLU_USERNAME) {
		NLU_USERNAME = nLU_USERNAME;
	}
	@Value("${nlu.password}")
	public void setNLU_PASSWORD(String nLU_PASSWORD) {
		NLU_PASSWORD = nLU_PASSWORD;
	}
	@Value("${nlu.endpoint}")
	public void setNLU_ENDPOINT(String nLU_ENDPOINT) {
		NLU_ENDPOINT = nLU_ENDPOINT;
	}

	public static final String NLU_RESULT_SUCCESS = "S";
	public static final String NLU_RESULT_FAIL = "F";

	private Map<String, List<Map<String, String>>> nluMap;
	
	/**
	 * NLU Analyzer 생성
	 * 
	 * @return
	 */
	private NaturalLanguageUnderstanding getNluAnalyzer() {
		if (this.nluService == null) {
			this.nluService = new NaturalLanguageUnderstanding(NLU_VERSION, NLU_USERNAME, NLU_PASSWORD);
			this.nluService.setEndPoint(NLU_ENDPOINT);
		}
		return this.nluService;
	}

	/**
	 * NLU Analyze 수행
	 */
	public Map<String, List<Map<String, String>>> analyze(String analyzeText) {

		logger.info("NLU Analyzer started!");

		CategoriesOptions categories = new CategoriesOptions();

		ConceptsOptions concepts = new ConceptsOptions.Builder().limit(3).build();

		EntitiesOptions entitiesOptions = new EntitiesOptions.Builder().emotion(true).sentiment(true).limit(5).build();

		KeywordsOptions keywordsOptions = new KeywordsOptions.Builder().emotion(true).sentiment(true).limit(3).build();

//		SemanticRolesOptions semanticRoles = new SemanticRolesOptions.Builder().build();
//		Features features = new Features.Builder().categories(categories).entities(entitiesOptions)
//				.keywords(keywordsOptions).concepts(concepts).semanticRoles(semanticRoles).build();
		
		Features features = new Features.Builder().categories(categories).entities(entitiesOptions)
				.keywords(keywordsOptions).concepts(concepts).build();		

		AnalyzeOptions parameters = new AnalyzeOptions.Builder().language("ko").text(analyzeText).features(features).build();

		AnalysisResults response = this.getNluAnalyzer().analyze(parameters).execute();

		List<Map<String, String>> conceptList = new ArrayList<Map<String, String>>();
		for(ConceptsResult conceptResult : response.getConcepts()) {
			Map<String, String> conceptMap = new HashMap<String, String>();
			conceptMap.put("text", conceptResult.getText());
			conceptMap.put("dbpedia_resource", conceptResult.getDbpediaResource());
			conceptList.add(conceptMap);
		}
		
		List<Map<String, String>> entityList = new ArrayList<Map<String, String>>();
		for(EntitiesResult entityResult : response.getEntities()) {
			Map<String, String> entityMap = new HashMap<String, String>();
			entityMap.put("text", entityResult.getText());
			entityMap.put("type", entityResult.getType());
			entityList.add(entityMap);
		}	
		
		List<Map<String, String>> keywordList = new ArrayList<Map<String, String>>();
		for(KeywordsResult keywordResult : response.getKeywords()) {
			Map<String, String> keywordMap = new HashMap<String, String>();
			keywordMap.put("text", keywordResult.getText());
			keywordList.add(keywordMap);
		}		
		
//		List<Map<String, String>> semanticList = new ArrayList<Map<String, String>>();
//		for(SemanticRolesResult semanticResult : response.getSemanticRoles()) {
//			Map<String, String> semanticMap = new HashMap<String, String>();
//			semanticMap.put("action", semanticResult.getAction().getNormalized());
//			semanticMap.put("subject", semanticResult.getSubject() == null ? "" : semanticResult.getSubject().getText());
//			semanticMap.put("object", semanticResult.getObject() == null ? "" : semanticResult.getObject().getText());
//			semanticList.add(semanticMap);
//		}
		
		this.nluMap = new HashMap<String, List<Map<String, String>>>();
		this.nluMap.put("conceptList", conceptList);
		this.nluMap.put("entityList", entityList);
		this.nluMap.put("keywordList", keywordList);
//		this.nluMap.put("semanticList", semanticList);
		
		return this.nluMap;
	}

}
