'use strict'

const initCellListeners = () => {
	var cells = document.querySelectorAll(".emptyCell");
	for (const element of cells) {
		var cell = element;
		if (cell.textContent == "" && cell.id != "-1/-1") {
			cell.addEventListener("click", onAddMoveAction);
		} else {
			cell.style.backgroundColor = "white";
			cell.style.border = "none";
		}
	}
}

const updateEvaluation = () => {
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/compute-evaluation/" + gameType, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE && xhr.response) {
			var evaluation = xhr.response;
			sendDisplayEvaluation(evaluation);
		}
	}
	xhr.send("");
}

const initButtonListeners = () => {
	var undoMoveButton = document.getElementById("undo-move");
	undoMoveButton.addEventListener("click", onUndoMoveAction);

	var resetGameButton = document.getElementById("reset-game");
	resetGameButton.addEventListener("click", onResetGameAction);

	var computeMoveButton = document.getElementById("compute-move");
	computeMoveButton.addEventListener("click", onComputeMoveAction);
	
	var stopButton = document.getElementById("stop");
	stopButton.addEventListener("click", onStopAction);
	
	var lastMoveButton = document.getElementById("lastMove");
	lastMoveButton.addEventListener("click", onLastMoveAction);
}

const onAddMoveAction = (event) => {
	
	if (isComputerRunning) {
		return;	
	}
	
	event.stopPropagation();
	var cell = event.srcElement;
	var column = parseInt(cell.id.split("/")[0]);
	var row = parseInt(cell.id.split("/")[1]);

	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/add-move/" + gameType, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.withCredentials = true;
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE && xhr.response) {
			moves = JSON.parse(xhr.response);
			for (const element of moves) {
				sendDisplayMove(element);
			}
			updateEvaluation();
			
			if (gameType == "AI") {
				onComputeMoveAction();
			}
		}
	}
	xhr.send(JSON.stringify({
		columnIndex: column,
		rowIndex: row
	}));
}

const onUndoMoveAction = (event) => {
	event.stopPropagation();
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/undo-move/" + gameType, true);
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE && xhr.response) {
			sendReload();
		}
	}
	xhr.send("");
}

const onResetGameAction = (event) => {
	event.stopPropagation();
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/reset-game/" + gameType, true);
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE) {
			sendReload();
		}
	}
	xhr.send("");
}

const onComputeMoveAction = (event) => {
	
	if (isComputerRunning) {
		return;
	}
	
	isComputerRunning = true;
	
	displayComputeProgress(1, 0);
	if (event) {
		event.stopPropagation();
	}
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/compute-move/" + gameType, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE && xhr.response) {
			moves = JSON.parse(xhr.response);
			for (const element of moves) {
				sendDisplayMove(element);
			}
			updateEvaluation();
			
			isComputerRunning = false;
		}
	}

	xhr.send("");
}

const onStopAction = (event) => {
	event.stopPropagation();
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/stop", true);
	xhr.setRequestHeader("Content-Type", "application/json");
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		var cells = document.querySelectorAll(".analysis");
		for (const element of cells) {
			element.classList.remove("analysis");
			element.classList.remove("black");
			element.classList.remove("white");
		}
		
		isComputerRunning = false;
		setTimeout(() => displayComputeProgress(1, 0), 100);
	}

	xhr.send("");
}

const onLastMoveAction = (event) => {
	event.stopPropagation();
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "/lastMove/" + gameType, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE && xhr.response) {
			var lastMove = JSON.parse(xhr.response);
			displayLastMove(lastMove);
		}
	}

	xhr.send("");
}

const displayLastMove = (move) => {

	const cells = document.querySelectorAll(".emptyCell");

	for (const element of cells) {
		var cell = element;
		var column = parseInt(cell.id.split("/")[0]);
		var row = parseInt(cell.id.split("/")[1]);

		if (column == move.columnIndex && row == move.rowIndex) {
			cell.classList.add('green');
			setTimeout(() => removeGreenClass(cell), 1000);
			break;
		}
	}
}

const removeGreenClass = (cell) => {
	cell.classList.remove('green');
}

const displayMove = (move) => {

	if (move) {
		const cells = document.querySelectorAll(".emptyCell");
	
		for (const element of cells) {
			var cell = element;
			var column = parseInt(cell.id.split("/")[0]);
			var row = parseInt(cell.id.split("/")[1]);
	
			if (column == move.columnIndex && row == move.rowIndex) {
				cell.classList.add('stone');
				cell.classList.remove('white');
				cell.classList.remove('black');
				if (move.color == 1) {
					cell.classList.add('black');
				} else if (move.color == -1) {
					cell.classList.add('white');
				} else if (move.color == 2) {
					cell.classList.add('green');
				}
			}
		}
	}
}

const displayAnalysisMove = (move) => {
	const cells = document.querySelectorAll(".emptyCell");

	for (const element of cells) {
		var cell = element;
		var column = parseInt(cell.id.split("/")[0]);
		var row = parseInt(cell.id.split("/")[1]);

		if (column == move.columnIndex && row == move.rowIndex) {
			cell.classList.remove('white');
			cell.classList.remove('black');
			cell.classList.remove('analysis');
			cell.classList.remove('stone');
			if (move.color == 1) {
				cell.classList.add('stone');
				cell.classList.add('analysis');
				cell.classList.add('black');
			} else if (move.color == -1) {
				cell.classList.add('stone');
				cell.classList.add('analysis');
				cell.classList.add('white');
			}
		}
	}
}

const displayComputeProgress = (index, progress) => {
	
	const progressBar = document.getElementById("progressBar" + index);

	progressBar.style.width = (progress + "%");
}

const displayEvaluation = (evaluation) => {

	const evaluationValue = document.getElementById("evaluationValue");

	evaluationValue.innerText = evaluation;
}

const updateComputeIcon = (isRunning) => {

	const computeIcon = document.getElementById("computeIcon");
	
	if (isRunning) {
		computeIcon.classList.add("fa-spin");
	} else {
		computeIcon.classList.remove("fa-spin");
	}
}

const refreshGame = (moves) => {
	for (const element of moves) {
		displayMove(element);
	}
}

const sendReload = () => {
	if (stompClient) {
		const webSocketMessage = {
			type: "RELOAD"
		}
		stompClient.send("/app/refresh", {}, JSON.stringify(webSocketMessage))
	}
}

const sendDisplayMove = (move) => {
	if (stompClient) {
		var webSocketMessage = {
			content: move,
			type: "REFRESH_MOVE"
		};
		stompClient.send("/app/refresh", {}, JSON.stringify(webSocketMessage))
	}
}

const sendDisplayEvaluation = (evaluation) => {
	if (stompClient) {
		var webSocketMessage = {
			content: evaluation,
			type: "REFRESH_EVALUATION"
		};
		stompClient.send("/app/refresh", {}, JSON.stringify(webSocketMessage))
	}
}

const connectToWebSocket = () => {
	const socket = new SockJS('/board-moves')
	stompClient = Stomp.over(socket)
	stompClient.connect({}, onConnected)
}

const connectToEngineSocket = () => {
	const socket = new SockJS('http://localhost:8081/engineMessages')
	stompClientEngine = Stomp.over(socket)
	stompClientEngine.connect({}, onEngineConnected)
}

const onConnected = () => {
	stompClient.subscribe('/web/public', onReceive)
}

const onEngineConnected = () => {
	stompClientEngine.subscribe('/engine/public', onReceive)
}

const onReceive = (payload) => {
	const webSocketMessage = JSON.parse(payload.body);

	if (webSocketMessage.type == "RELOAD") {
		location.reload();
	} else if (webSocketMessage.type == "REFRESH_MOVE") {
		displayMove(webSocketMessage.content);
	} else if (webSocketMessage.type == "REFRESH_EVALUATION") {
		displayEvaluation(webSocketMessage.content);
	} else if (webSocketMessage.type == "COMPUTE_PROGRESS") {
		const contentJson = webSocketMessage.content;
		displayComputeProgress(contentJson.index, contentJson.percent);
	} else if (webSocketMessage.type == "ANALYSIS_MOVE" ) {
		if (userSettings.displayAnalysis) {
			displayAnalysisMove(webSocketMessage.content);
		}
		updateComputeIcon(true);
	} else if (webSocketMessage.type == "IS_RUNNING") {
		updateComputeIcon(webSocketMessage.content);
	}
}

var stompClient;

var stompClientEngine;

function main() {

	connectToWebSocket();
	
	connectToEngineSocket();

	initCellListeners();

	initButtonListeners();

	refreshGame(moves);
	
	displayEvaluation(evaluation);
	
	updateComputeIcon(false);
}

var isComputerRunning = false;

main();


