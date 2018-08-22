package com.meetingbot.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

@Component
public class SttRecognizer {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private SpeechToText sttService;
	private TargetDataLine line;
	
	private static String STT_USERNAME;
	private static String STT_PASSWORD;
	private static String STT_ENDPOINT;
	@Value("${stt.username}")
	public void setSTT_USERNAME(String sTT_USERNAME) {
		System.out.println("sTT_USERNAME : " + sTT_USERNAME);
		STT_USERNAME = sTT_USERNAME;
	}
	@Value("${stt.password}")
	public void setSTT_PASSWORD(String sTT_PASSWORD) {
		STT_PASSWORD = sTT_PASSWORD;
	}
	@Value("${stt.endpoint}")
	public void setSTT_ENDPOINT(String sTT_ENDPOINT) {
		STT_ENDPOINT = sTT_ENDPOINT;
	}	

	private static final int STT_SAMPLE_RATE = 16000;
	public static final String STT_RESULT_SUCCESS = "S";
	public static final String STT_RESULT_FAIL = "F";
	
	private List<Map<String, String>> sttList;
	
	
	/**
	 * STT Recognizer 생성
	 * @return
	 */
	private SpeechToText getSttRecognizer() {
		if(this.sttService == null) {
			this.sttService = new SpeechToText();
			this.sttService.setUsernameAndPassword(STT_USERNAME, STT_PASSWORD);
			this.sttService.setEndPoint(STT_ENDPOINT);
		}
		return this.sttService;
	}
	
	/**
	 * STT Record 시작
	 * @return
	 */
	public String start() {

		this.sttList = new ArrayList<Map<String, String>>();
		
		AudioFormat format = new AudioFormat(STT_SAMPLE_RATE, 16, 1, true, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		if (!AudioSystem.isLineSupported(info)) {
			logger.error("Line not supported");
			return STT_RESULT_FAIL;
		}

		try {
			this.line = (TargetDataLine) AudioSystem.getLine(info);
			this.line.open(format);
			this.line.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return STT_RESULT_FAIL;
		}

		AudioInputStream audio = new AudioInputStream(this.line);

		List<String> keywords = new ArrayList<String>();
		keywords.add("회의 시작");
		keywords.add("회의 종료");
		RecognizeOptions options = new RecognizeOptions.Builder()
				.audio(audio)
				.interimResults(true)
				.model("ko-KR_BroadbandModel")
				.contentType(HttpMediaType.AUDIO_RAW + ";rate=" + STT_SAMPLE_RATE)
				.keywords(keywords)
				.keywordsThreshold((float)0.6)
				.build();

		this.getSttRecognizer().recognizeUsingWebSocket(options, new BaseRecognizeCallback() {
			@Override
			public void onTranscription(SpeechRecognitionResults speechResults) {
				Map<String, String> sttMap = new HashMap<String, String>();
				sttMap.put("index", String.valueOf(speechResults.getResultIndex()));
				sttMap.put("isFinal", String.valueOf(speechResults.getResults().get(0).isFinalResults()));
				sttMap.put("transcript", speechResults.getResults().get(0).getAlternatives().get(0).getTranscript());
				if(speechResults.getResults().get(0).getKeywordsResult() != null) {
					sttMap.put("isStart", speechResults.getResults().get(0).getKeywordsResult().get("회의 시작") == null ? "N" : "Y");
					sttMap.put("isEnd", speechResults.getResults().get(0).getKeywordsResult().get("회의 종료") == null ? "N" : "Y");
				} else {
					sttMap.put("isStart", "N");
					sttMap.put("isEnd", "N");
				}
				sttList.add(sttMap);
			}
		});

		logger.info("Record Started!");
		return STT_RESULT_SUCCESS;
	}
	
	/**
	 * STT Record 중지
	 */
	public void stop() {
		if(line!=null) {
			this.line.stop();
			this.line.close();
			logger.info("Record Stopped!");
		}
	}
	
	/**
	 * STT Record 내용 가져오기
	 * @return
	 */
	public List<Map<String, String>> getSttList(){
		if(this.sttList == null) {
			this.sttList = new ArrayList<Map<String, String>>();
		}
		return this.sttList;
	}
}
