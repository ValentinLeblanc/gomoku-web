'use strict'

const initCellListeners = () => {
	var cells = document.querySelectorAll(".emptyCell");
	for (const element of cells) {
		var cell = element;
		if (cell.textContent == "" && cell.id != "-1/-1") {
			cell.addEventListener("click", onAddMoveAction);
		}
	}
}

const updateEvaluation = (moves) => {
	var jsonMoves = [];

	for (const element of moves) {
		jsonMoves.push({
			columnIndex: element.columnIndex,
			rowIndex: element.rowIndex,
			color: element.color == "BLACK" ? 1 : -1
		});
	}

	var jsonGame = {
		boardSize: boardSize,
		moves: jsonMoves
	};

	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/compute-evaluation", true);
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
	xhr.send(JSON.stringify(jsonGame));
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
}

const onAddMoveAction = (event) => {
	event.stopPropagation();
	var cell = event.srcElement;
	var column = parseInt(cell.id.split("/")[0]);
	var row = parseInt(cell.id.split("/")[1]);

	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/add-move", true);
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
			updateEvaluation(moves);
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
	xhr.open("POST", "/undo-move", true);
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE && xhr.response) {
			sendReload();
		}
	}
	xhr.send(JSON.stringify({
	}));
}

const onResetGameAction = (event) => {
	event.stopPropagation();
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/reset-game", true);
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE) {
			sendReload();
		}
	}
	xhr.send({});
}

const onComputeMoveAction = (event) => {
	displayComputeProgress(1);
	event.stopPropagation();
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/compute-move", true);
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
			updateEvaluation(moves);
		}
	}

	var jsonMoves = [];

	for (const element of moves) {
		jsonMoves.push({
			columnIndex: element.columnIndex,
			rowIndex: element.rowIndex,
			color: element.color == "BLACK" ? 1 : -1
		});
	}

	var jsonGame = {
		boardSize: boardSize,
		moves: jsonMoves
	};

	xhr.send(JSON.stringify(jsonGame));
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
		sendReload();
	}

	xhr.send("");
}

const displayMove = (move) => {

	const cells = document.querySelectorAll(".emptyCell");

	for (const element of cells) {
		var cell = element;
		var column = parseInt(cell.id.split("/")[0]);
		var row = parseInt(cell.id.split("/")[1]);

		if (column == move.columnIndex && row == move.rowIndex) {
			cell.classList.add('stone');
			cell.classList.remove('white');
			cell.classList.remove('black');
			if (move.color == "BLACK") {
				cell.classList.add('black');
			} else if (move.color == "WHITE") {
				cell.classList.add('white');
			} else if (move.color == "GREEN") {
				cell.classList.add('green');
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

const displayComputeProgress = (progress) => {

	const progressBar = document.getElementById("progressBar");

	progressBar.style.width = (progress + "%");
}

const displayEvaluation = (evaluation) => {

	const evaluationValue = document.getElementById("evaluationValue");

	evaluationValue.innerText = evaluation;
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
			content: JSON.stringify(move),
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
		displayMove(JSON.parse(webSocketMessage.content));
	} else if (webSocketMessage.type == "REFRESH_EVALUATION") {
		displayEvaluation(webSocketMessage.content);
	} else if (webSocketMessage.type == "COMPUTE_PROGRESS") {
		displayComputeProgress(webSocketMessage.content);
	} else if (webSocketMessage.type == "ANALYSIS_MOVE") {
		displayAnalysisMove(webSocketMessage.content);
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
}


main();


