package com.meetingbot.main;

import java.rmi.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.meetingbot.util.NluAnalyzer;
import com.meetingbot.util.SttRecognizer;
import com.meetingbot.util.assistantUnit;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;

@Service
public class MainService {
	
	@Value("${mongo.ip}")
	private String MONGO_IP;
	
	private SttRecognizer sttRecognizer;
	private assistantUnit assistant;
	
	
	/**
	 * findMeetingLog 시작
	 * @return String
	 */
	public String findMeetingLog() throws UnknownHostException {
		MongoClient mongo = new MongoClient(MONGO_IP, 27017);
		DB db = mongo.getDB("meetingbot");
		DBCollection coll = db.getCollection("meetingbot");
		
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("date", "<b>2018-07-10 10:30</b>");
		DBCursor cursor = coll.find(whereQuery);
		
		return (String) cursor.next().get("content");
	}
	
	/**
	 * Assistant 시작
	 * @return
	 */
	public String startAssistant(String inputText) {
		this.assistant = new assistantUnit();
		return assistant.start(inputText);
	}


	private NluAnalyzer nluAnalyzer;
	/**
	 * STT 시작
	 * @return
	 */
	public String startRecording() {
		if(this.sttRecognizer != null) {
			return SttRecognizer.STT_RESULT_FAIL;
		}
		this.sttRecognizer = new SttRecognizer();
		return sttRecognizer.start();
	}
	
	/**
	 * STT 중지
	 */
	public void stopRecording() {
		this.sttRecognizer.stop();
		this.sttRecognizer = null;
	}
	
	/**
	 * STT 목록 조회
	 * @return
	 */
	public List<Map<String, String>> getRecorded() {
		return this.sttRecognizer.getSttList();
	}
	
	/**
	 * NLU 분석
	 * @return
	 */
	public Map<String, List<Map<String, String>>> startAnalyze(String analyzeText) {
		
		analyzeText = analyzeText.replace("analyzeText=", "");
		
		if(this.nluAnalyzer == null) {
			this.nluAnalyzer = new NluAnalyzer();
		}
		return this.nluAnalyzer.analyze(analyzeText);
	}
}