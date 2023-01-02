
const initializeView = (settings) => {
	
	if (settings.boardSize == 11) {
		document.getElementById("boardSize11").checked = true;
		document.getElementById("boardSize13").checked = false;
		document.getElementById("boardSize15").checked = false;
	} else if (settings.boardSize == 13) {
		document.getElementById("boardSize11").checked = false;
		document.getElementById("boardSize13").checked = true;
		document.getElementById("boardSize15").checked = false;
	} else if (settings.boardSize == 15) {
		document.getElementById("boardSize11").checked = false;
		document.getElementById("boardSize13").checked = false;
		document.getElementById("boardSize15").checked = true;
	}
	
	if (settings.strikeTimeout == 10) {
		document.getElementById("strikeTimeout10").checked = true;
		document.getElementById("strikeTimeout20").checked = false;
		document.getElementById("strikeTimeout30").checked = false;
		document.getElementById("strikeTimeoutInf").checked = false;
	} else if (settings.strikeTimeout == 20) {
		document.getElementById("strikeTimeout10").checked = false;
		document.getElementById("strikeTimeout20").checked = true;
		document.getElementById("strikeTimeout30").checked = false;
		document.getElementById("strikeTimeoutInf").checked = false;
	} else if (settings.strikeTimeout == 30) {
		document.getElementById("strikeTimeout10").checked = false;
		document.getElementById("strikeTimeout20").checked = false;
		document.getElementById("strikeTimeout30").checked = true;
		document.getElementById("strikeTimeoutInf").checked = false;
	} else if (settings.strikeTimeout == -1) {
		document.getElementById("strikeTimeout10").checked = false;
		document.getElementById("strikeTimeout20").checked = false;
		document.getElementById("strikeTimeout30").checked = false;
		document.getElementById("strikeTimeoutInf").checked = true;
	}
	
	if (settings.strikeDepth == 1) {
		document.getElementById("strikeDepth1").checked = true;
		document.getElementById("strikeDepth2").checked = false;
		document.getElementById("strikeDepth3").checked = false;
		document.getElementById("strikeDepth4").checked = false;
	} else if (settings.strikeDepth == 2) {
		document.getElementById("strikeDepth1").checked = false;
		document.getElementById("strikeDepth2").checked = true;
		document.getElementById("strikeDepth3").checked = false;
		document.getElementById("strikeDepth4").checked = false;
	} else if (settings.strikeDepth == 3) {
		document.getElementById("strikeDepth1").checked = false;
		document.getElementById("strikeDepth2").checked = false;
		document.getElementById("strikeDepth3").checked = true;
		document.getElementById("strikeDepth4").checked = false;
	} else if (settings.strikeDepth == 4) {
		document.getElementById("strikeDepth1").checked = false;
		document.getElementById("strikeDepth2").checked = false;
		document.getElementById("strikeDepth3").checked = false;
		document.getElementById("strikeDepth4").checked = true;
	}
	
	if (settings.minMaxDepth == 1) {
		document.getElementById("minMaxDepth1").checked = true;
		document.getElementById("minMaxDepth2").checked = false;
		document.getElementById("minMaxDepth3").checked = false;
		document.getElementById("minMaxDepth4").checked = false;
	} else if (settings.minMaxDepth == 2) {
		document.getElementById("minMaxDepth1").checked = false;
		document.getElementById("minMaxDepth2").checked = true;
		document.getElementById("minMaxDepth3").checked = false;
		document.getElementById("minMaxDepth4").checked = false;
	} else if (settings.minMaxDepth == 3) {
		document.getElementById("minMaxDepth1").checked = false;
		document.getElementById("minMaxDepth2").checked = false;
		document.getElementById("minMaxDepth3").checked = true;
		document.getElementById("minMaxDepth4").checked = false;
	} else if (settings.minMaxDepth == 4) {
		document.getElementById("minMaxDepth1").checked = false;
		document.getElementById("minMaxDepth2").checked = false;
		document.getElementById("minMaxDepth3").checked = false;
		document.getElementById("minMaxDepth4").checked = true;
	}
	
	if (settings.evaluationDepth == 1) {
		document.getElementById("evaluationDepth1").checked = true;
		document.getElementById("evaluationDepth2").checked = false;
		document.getElementById("evaluationDepth3").checked = false;
		document.getElementById("evaluationDepth4").checked = false;
	} else if (settings.evaluationDepth == 2) {
		document.getElementById("evaluationDepth1").checked = false;
		document.getElementById("evaluationDepth2").checked = true;
		document.getElementById("evaluationDepth3").checked = false;
		document.getElementById("evaluationDepth4").checked = false;
	} else if (settings.evaluationDepth == 3) {
		document.getElementById("evaluationDepth1").checked = false;
		document.getElementById("evaluationDepth2").checked = false;
		document.getElementById("evaluationDepth3").checked = true;
		document.getElementById("evaluationDepth4").checked = false;
	} else if (settings.evaluationDepth == 4) {
		document.getElementById("evaluationDepth1").checked = false;
		document.getElementById("evaluationDepth2").checked = false;
		document.getElementById("evaluationDepth3").checked = false;
		document.getElementById("evaluationDepth4").checked = true;
	}
	
	if (settings.displayAnalysis == true) {
		document.getElementById("displayAnalysis").checked = true;
	} else {
		document.getElementById("displayAnalysis").checked = false;
	}
	
	if (settings.strikeEnabled == true) {
		document.getElementById("strikeEnabled").checked = true;
	} else {
		document.getElementById("strikeEnabled").checked = false;
	}
	
}

const initializeListeners = () => {
	document.getElementById("boardSize11").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("boardSize13").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("boardSize15").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("displayAnalysis").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("strikeEnabled").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("strikeDepth1").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("strikeDepth2").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("strikeDepth3").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("strikeDepth4").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("minMaxDepth1").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("minMaxDepth2").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("minMaxDepth3").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("minMaxDepth4").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("evaluationDepth1").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("evaluationDepth2").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("evaluationDepth3").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("evaluationDepth4").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("strikeTimeout10").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("strikeTimeout20").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("strikeTimeout30").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("strikeTimeoutInf").addEventListener("click", onUpdateSettingsClick);
}

const onUpdateSettingsClick = (event) => {
	var xhr = new XMLHttpRequest();
		xhr.open("POST", "http://localhost:8080/settings", true);
		xhr.withCredentials = true;
		var header = this._csrf.headerName;
		var token = this._csrf.token;
		xhr.setRequestHeader(header, token);
	if (event.srcElement == document.getElementById("boardSize11")) {
		document.getElementById("boardSize13").checked = false;
		document.getElementById("boardSize15").checked = false;
		settings.boardSize = 11;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("boardSize13")) {
		document.getElementById("boardSize11").checked = false;
		document.getElementById("boardSize15").checked = false;
		settings.boardSize = 13;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("boardSize15")) {
		document.getElementById("boardSize11").checked = false;
		document.getElementById("boardSize13").checked = false;
		settings.boardSize = 15;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("displayAnalysis")) {
		settings.displayAnalysis = document.getElementById("displayAnalysis").checked;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("strikeEnabled")) {
		settings.strikeEnabled = document.getElementById("strikeEnabled").checked;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("strikeDepth1")) {
		document.getElementById("strikeDepth2").checked = false;
		document.getElementById("strikeDepth3").checked = false;
		document.getElementById("strikeDepth4").checked = false;
		settings.strikeDepth = 1;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("strikeDepth2")) {
		document.getElementById("strikeDepth1").checked = false;
		document.getElementById("strikeDepth3").checked = false;
		document.getElementById("strikeDepth4").checked = false;
		settings.strikeDepth = 2;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("strikeDepth3")) {
		document.getElementById("strikeDepth1").checked = false;
		document.getElementById("strikeDepth2").checked = false;
		document.getElementById("strikeDepth4").checked = false;
		settings.strikeDepth = 3;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("strikeDepth4")) {
		document.getElementById("strikeDepth1").checked = false;
		document.getElementById("strikeDepth2").checked = false;
		document.getElementById("strikeDepth3").checked = false;
		settings.strikeDepth = 4;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("minMaxDepth1")) {
		document.getElementById("minMaxDepth2").checked = false;
		document.getElementById("minMaxDepth3").checked = false;
		document.getElementById("minMaxDepth4").checked = false;
		settings.minMaxDepth = 1;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("minMaxDepth2")) {
		document.getElementById("minMaxDepth1").checked = false;
		document.getElementById("minMaxDepth3").checked = false;
		document.getElementById("minMaxDepth4").checked = false;
		settings.minMaxDepth = 2;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("minMaxDepth3")) {
		document.getElementById("minMaxDepth1").checked = false;
		document.getElementById("minMaxDepth2").checked = false;
		document.getElementById("minMaxDepth4").checked = false;
		settings.minMaxDepth = 3;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("minMaxDepth4")) {
		document.getElementById("minMaxDepth1").checked = false;
		document.getElementById("minMaxDepth2").checked = false;
		document.getElementById("minMaxDepth3").checked = false;
		settings.minMaxDepth = 4;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("evaluationDepth1")) {
		document.getElementById("evaluationDepth2").checked = false;
		document.getElementById("evaluationDepth3").checked = false;
		document.getElementById("evaluationDepth4").checked = false;
		settings.evaluationDepth = 1;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("evaluationDepth2")) {
		document.getElementById("evaluationDepth1").checked = false;
		document.getElementById("evaluationDepth3").checked = false;
		document.getElementById("evaluationDepth4").checked = false;
		settings.evaluationDepth = 2;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("evaluationDepth3")) {
		document.getElementById("evaluationDepth1").checked = false;
		document.getElementById("evaluationDepth2").checked = false;
		document.getElementById("evaluationDepth4").checked = false;
		settings.evaluationDepth = 3;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("evaluationDepth4")) {
		document.getElementById("evaluationDepth1").checked = false;
		document.getElementById("evaluationDepth2").checked = false;
		document.getElementById("evaluationDepth3").checked = false;
		settings.evaluationDepth = 4;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("strikeTimeout10")) {
		document.getElementById("strikeTimeout20").checked = false;
		document.getElementById("strikeTimeout30").checked = false;
		document.getElementById("strikeTimeoutInf").checked = false;
		settings.strikeTimeout = 10;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("strikeTimeout20")) {
		document.getElementById("strikeTimeout10").checked = false;
		document.getElementById("strikeTimeout30").checked = false;
		document.getElementById("strikeTimeoutInf").checked = false;
		settings.strikeTimeout = 20;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("strikeTimeout30")) {
		document.getElementById("strikeTimeout10").checked = false;
		document.getElementById("strikeTimeout20").checked = false;
		document.getElementById("strikeTimeoutInf").checked = false;
		settings.strikeTimeout = 30;
		xhr.send(JSON.stringify(settings));
	} else if (event.srcElement == document.getElementById("strikeTimeoutInf")) {
		document.getElementById("strikeTimeout10").checked = false;
		document.getElementById("strikeTimeout20").checked = false;
		document.getElementById("strikeTimeout30").checked = false;
		settings.strikeTimeout = -1;
		xhr.send(JSON.stringify(settings));
	}
}

function main() {
	
	initializeView(settings);
	initializeListeners();
}

main();