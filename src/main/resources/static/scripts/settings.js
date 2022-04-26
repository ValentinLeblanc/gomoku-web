
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
	
}

const initializeListeners = () => {
	document.getElementById("boardSize11").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("boardSize13").addEventListener("click", onUpdateSettingsClick);
	document.getElementById("boardSize15").addEventListener("click", onUpdateSettingsClick);
}

const onUpdateSettingsClick = (event) => {
	var xhr = new XMLHttpRequest();
		xhr.open("POST", "http://localhost:8080/settings", true);
		xhr.withCredentials = true;
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
	}
}

function main() {
	
	initializeView(settings);
	initializeListeners();
}

main();