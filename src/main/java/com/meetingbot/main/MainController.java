package com.meetingbot.main;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.rmi.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	@Autowired
	MainService mainService;

	@RequestMapping("/")
	public String main() throws UnknownHostException {
		
//		mainService.mongoTest();
		
		return "main";
	}
	
	@RequestMapping("/popup")
	public String popup() {
		return "popup";
	}
	
	/**
	 * 회의불러오기
	 * @return
	 * @throws UnknownHostException 
	 */
	@RequestMapping(value = "/findMeetingLog", method = RequestMethod.GET)
	@ResponseBody
	public String findMeetingLog() throws UnknownHostException {
		return mainService.findMeetingLog();
	}	

	/**
	 * Assistant 시작
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/startAssistant", method = RequestMethod.POST)
	@ResponseBody
	public String startAssistant(@RequestBody String inputText) throws UnsupportedEncodingException {
		return mainService.startAssistant(URLDecoder.decode(inputText, "UTF-8"));
	}

	/**
	 * STT 시작
	 * @return
	 */
	@RequestMapping(value = "/startRecording", method = RequestMethod.GET)
	@ResponseBody
	public String startRecording() {
		return mainService.startRecording();
	}
	
	/**
	 * STT 중지
	 * @return
	 */
	@RequestMapping(value = "/stopRecording", method = RequestMethod.GET)
	@ResponseBody
	public String stopRecording() {
		mainService.stopRecording();
		return "S";
	}	
	
	/**
	 * STT 내용 조회
	 * @return
	 */
	@RequestMapping(value = "/getRecorded", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,String>> getRecorded() {
		return mainService.getRecorded();
	}	
	
	/**
	 * NLU 분석 수행
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/startAnalyze", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, List<Map<String, String>>> startAnalyze(@RequestBody String analyzeText) throws UnsupportedEncodingException {
		return mainService.startAnalyze(URLDecoder.decode(analyzeText, "UTF-8"));
	}
	
	/**
	 * NLC 분류 수행
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/startClassify", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> startClassify(@RequestBody String classifyText) throws UnsupportedEncodingException {
		return mainService.startClassify(URLDecoder.decode(classifyText, "UTF-8"));
	}	
}
