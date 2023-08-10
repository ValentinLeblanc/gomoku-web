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

const hideButtonIfOnline = (button) => {
	if (gameType == "ONLINE") {
		button.parentNode.parentNode.parentNode.parentNode.removeChild(button.parentNode.parentNode.parentNode);
	}
}

const initButtonListeners = () => {
	var undoMoveButton = document.getElementById("undo-move");
	undoMoveButton.addEventListener("click", onUndoMoveAction);
	hideButtonIfOnline(undoMoveButton);
	
	var redoMoveButton = document.getElementById("redo-move");
	redoMoveButton.addEventListener("click", onRedoMoveAction);
	hideButtonIfOnline(redoMoveButton);

	var resetGameButton = document.getElementById("reset-game");
	resetGameButton.addEventListener("click", onResetGameAction);

	var computeMoveButton = document.getElementById("compute-move");
	computeMoveButton.addEventListener("click", onComputeMoveAction);
	hideButtonIfOnline(computeMoveButton);
	
	var stopButton = document.getElementById("stop");
	stopButton.addEventListener("click", onStopAction);
	hideButtonIfOnline(stopButton);
	
	var lastMoveButton = document.getElementById("lastMove");
	lastMoveButton.addEventListener("click", onLastMoveAction);
	
	var downloadGameButton = document.getElementById("downloadGame");
	downloadGameButton.addEventListener("click", onDownloadGameAction);
	
	var uploadGameButton = document.getElementById("uploadGame");
	uploadGameButton.addEventListener("click", onUploadGameAction);
	hideButtonIfOnline(uploadGameButton);
}

const onAddMoveAction = (event) => {
	if (event) {
		event.stopPropagation();
	}
	if (isComputing) {
		return;	
	}
	if (winningMoves != null && winningMoves.length > 0) {
		return;
	}
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
	xhr.send(JSON.stringify({
		columnIndex: column,
		rowIndex: row
	}));
}

const onUndoMoveAction = (event) => {
	if (event) {
		event.stopPropagation();
	}
	onStopAction();
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/undo-move/" + gameType, true);
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE && xhr.response) {
			sendRefreshBoard();
		}
	}
	xhr.send("");
}

const onRedoMoveAction = (event) => {
	if (event) {
		event.stopPropagation();
	}
	if (winningMoves != null && winningMoves.length > 0) {
		return;
	}
	onStopAction();
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/redo-move/" + gameType, true);
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE && xhr.response) {
			sendRefreshBoard();
		}
	}
	xhr.send("");
}

const onResetGameAction = (event) => {
	if (event) {
		event.stopPropagation();
	}
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/reset-game/" + gameType, true);
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE) {
			sendRefreshBoard();
		}
	}
	xhr.send("");
}

const onComputeMoveAction = (event) => {
	if (event) {
		event.stopPropagation();
	}
	if (isComputing) {
		return;
	}
	if (winningMoves != null && winningMoves.length > 0) {
		return;
	}
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/compute-move/" + gameType, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.send("");
}

const onStopAction = (event) => {
	if (event) {
		event.stopPropagation();
	}
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/stop/" + gameId, true);
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
		
		setTimeout(() => displayMinMaxProgress(0), 100);
	}

	xhr.send("");
}

const requestLastMove = (propagate) => {
	var xhr = new XMLHttpRequest();
	xhr.open("GET", "/lastMove/" + gameType + "/" + propagate, true);
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

const onLastMoveAction = (event) => {
	if (event) {
		event.stopPropagation();
	}
	requestLastMove(false);
}

const onDownloadGameAction = (event) => {
	if (event) {
		event.stopPropagation();
	}
	var xhr = new XMLHttpRequest();
	xhr.responseType = 'blob';
	xhr.open("GET", "/downloadGame/" + gameType, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			handleSaveImg(xhr.response);
		}
	}

	xhr.send("");
}

async function handleSaveImg(response) {
	if (window.showSaveFilePicker) {
		const handle = await showSaveFilePicker();
		const writable = await handle.createWritable();
		await writable.write(response);
		writable.close();
	} else {
		var downloadUrl = window.URL.createObjectURL(response);
		var a = document.createElement("a");
		document.body.appendChild(a);
		a.style = "display: none";
		a.href = downloadUrl;
		a.download = "game.json";
		a.click();
		window.URL.revokeObjectURL(url);
		a.remove();
	}
}

const onUploadGameAction = (event) => {
	if (event) {
		event.stopPropagation();
	}
	let input = document.createElement('input');
	input.type = 'file';
	input.onchange = _ => {
		var files = Array.from(input.files);
		
		if (files.length > 0) {
			var file = files[0];
			
			var xhr = new XMLHttpRequest();
			xhr.open("POST", "/uploadGame/" + gameType, true);
			xhr.setRequestHeader("Content-Type", "application/json");
			var header = this._csrf.headerName;
			var token = this._csrf.token;
			xhr.setRequestHeader(header, token);
			xhr.withCredentials = true;
			xhr.onreadystatechange = function() {
				if (xhr.readyState == XMLHttpRequest.DONE) {
					sendRefreshBoard();
				}
			}
		
			xhr.send(file);
		}
		
	};
	input.click();
  
}

const displayLastMove = (move) => {

	const cells = document.querySelectorAll(".emptyCell");
	const lasts = document.querySelectorAll(".last");

	for (const last of lasts) {
		last.classList.remove('last');
	}
	
	for (const element of cells) {
		var cell = element;
		var column = parseInt(cell.id.split("/")[0]);
		var row = parseInt(cell.id.split("/")[1]);

		if (column == move.columnIndex && row == move.rowIndex) {
			cell.classList.add('last');
			setTimeout(() => removeLastClass(cell), 1000);
			break;
		}
	}
}

const removeLastClass = (cell) => {
	cell.classList.remove('last');
}

const displayMove = (move) => {

	if (move) {
		const cells = document.querySelectorAll(".emptyCell");
	
		for (const element of cells) {
			var cell = element;
			var column = parseInt(cell.id.split("/")[0]);
			var row = parseInt(cell.id.split("/")[1]);
	
			if (column == move.columnIndex && row == move.rowIndex) {
				if (move.color == 2) {
					cell.classList.add('win');
				} else {
					cell.classList.add('stone');
					cell.classList.remove('white');
					cell.classList.remove('black');
					if (move.color == 1) {
						cell.classList.add('black');
					} else if (move.color == -1) {
						cell.classList.add('white');
					}
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

const displayMinMaxProgress = (progress) => {
	const progressBar = document.getElementById("progressBar1");
	progressBar.style.width = (progress + "%");
}

const displayStrikeProgress = (progress) => {
	const progressBar = document.getElementById("progressBar1");
	if (progress) {
		progressBar.style.width = "100%";
		progressBar.classList.remove("minMaxProgressBar");
		progressBar.classList.add("strikeProgressBar");
	} else {
		progressBar.style.width = "0%";
		progressBar.classList.remove("strikeProgressBar");
		progressBar.classList.add("minMaxProgressBar");
	}
}

const displayEvaluation = (evaluation) => {
	const evaluationValue = document.getElementById("evaluationValue");
	evaluationValue.innerText = evaluation;
}

const updateComputeIcon = () => {
	const computeIcon = document.getElementById("computeIcon");
	if (isComputing) {
		computeIcon.classList.add("fa-spin");
	} else {
		computeIcon.classList.remove("fa-spin");
	}
}

const displayMoves = (moves) => {
	if (moves) {
		for (const element of moves) {
			displayMove(element);
		}
	}
}

const sendRefreshBoard = () => {
	if (stompClient) {
		const webSocketMessage = {
			gameId: gameId,
			type: "REFRESH_BOARD"
		}
		stompClient.send("/app/refreshBoard", {}, JSON.stringify(webSocketMessage))
	}
}

const connectToGameWebSocket = () => {
	const socket = new SockJS('/gameMessages')
	stompClient = Stomp.over(socket)
	stompClient.connect({}, onConnected)
}

const connectToEngineWebSocket = () => {
	const socket = new SockJS(webSocketEngineUrl)
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

	if (webSocketMessage.gameId == gameId) {
		if (webSocketMessage.type == "REFRESH_BOARD") {
			location.reload();
		} else if (webSocketMessage.type == "MOVE") {
			displayMove(webSocketMessage.content);
			requestLastMove(true);
		} else if (webSocketMessage.type == "EVALUATION") {
			displayEvaluation(webSocketMessage.content);
		} else if (webSocketMessage.type == "MINMAX_PROGRESS") {
			const progress = webSocketMessage.content;
			displayMinMaxProgress(progress);
		} else if (webSocketMessage.type == "STRIKE_PROGRESS") {
			const progress = webSocketMessage.content;
			displayStrikeProgress(progress);
		} else if (webSocketMessage.type == "ANALYSIS_MOVE" ) {
			displayAnalysisMove(webSocketMessage.content);
		} else if (webSocketMessage.type == "IS_COMPUTING") {
			isComputing = webSocketMessage.content;
			updateComputeIcon();
		} else if (webSocketMessage.type == "LAST_MOVE") {
			displayLastMove(webSocketMessage.content);
		} else if (webSocketMessage.type == "IS_WIN") {
			winningMoves = webSocketMessage.content;
			displayMoves(winningMoves);
		} else if (webSocketMessage.type == "ONLINE_GAME_ABORTED") {
			window.location.href = "/online";
		}
	}
}

var stompClient;
var stompClientEngine;

function main() {
	connectToGameWebSocket();
	connectToEngineWebSocket();
	initCellListeners();
	initButtonListeners();
	displayMoves(moves);
	displayMoves(winningMoves);
	displayEvaluation(evaluation);
	updateComputeIcon();
	requestLastMove(true);
}

main();


