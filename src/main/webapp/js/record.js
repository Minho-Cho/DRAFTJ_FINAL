var audio_context, recorder, volume, volumeLevel = 0;
var timerId;
function startUserMedia(stream) {
	var input = audio_context.createMediaStreamSource(stream);
	console.log('Media stream created.');

	volume = audio_context.createGain();
	volume.gain.value = volumeLevel;
	input.connect(volume);
	volume.connect(audio_context.destination);
	console.log('Input connected to audio context destination.');

	recorder = new Recorder(input);
	console.log('Recorder initialised.');
}

function startRecording(button) {
	recorder && recorder.record();
	console.log('Recording...');
	
	timerId = setInterval(function(){
		sendRecording();
	}, 1000);
}

function stopRecording(button) {
	recorder && recorder.stop();
	console.log('Stopped recording.');
	clearInterval(timerId);
	recorder.clear();
}

function sendRecording() {
	console.log("sendRecording...");
	recorder && recorder.exportWAV(function(blob){
		var formData = new FormData();
		formData.append('blob', blob, 'a.wav');
		$.ajax({
			url : "/test",
			data : formData,
			cache : false,
			contentType : false,
			processData : false,
			type : 'POST',
			success : function(response) {
				if(response) {
					$("#divStt").append(response);
				}
			}
		});
	});
	recorder.clear();
	recorder && recorder.record();
}

window.onload = function init() {
	try {
		// webkit shim
		window.AudioContext = window.AudioContext || window.webkitAudioContext
				|| window.mozAudioContext;
		navigator.getUserMedia = navigator.getUserMedia
				|| navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
		window.URL = window.URL || window.webkitURL || window.mozURL;

		audio_context = new AudioContext();
		console.log('Audio context set up.');
		console.log('navigator.getUserMedia '
				+ (navigator.getUserMedia ? 'available.' : 'not present!'));
	} catch (e) {
		console.warn('No web audio support in this browser!');
	}

	navigator.getUserMedia({
		audio : true
	}, startUserMedia, function(e) {
		console.warn('No live audio input: ' + e);
	});
};