'use strict'

const initCellListeners = () => {
	var cells = document.querySelectorAll(".emptyCell");
	for (let i = 0; i < cells.length; i++) {
		var cell = cells[i];
		cell.addEventListener("click", onAddMoveAction);
	}
}

const initButtonListeners = () => {
	var undoMoveButton = document.getElementById("undo-move");
	undoMoveButton.addEventListener("click", onUndoMoveAction);

	var resetGameButton = document.getElementById("reset-game");
	resetGameButton.addEventListener("click", onResetGameAction);
	
	var computeMoveButton = document.getElementById("compute-move");
	computeMoveButton.addEventListener("click", onComputeMoveAction);
}

const onAddMoveAction = (event) => {
	event.stopPropagation();
	var cell = event.srcElement;
	var column = parseInt(cell.id.split("-")[0]);
	var row = parseInt(cell.id.split("-")[1]);

	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/add-move", true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.withCredentials = true;
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE && xhr.response) {
			var moves = JSON.parse(xhr.response);
			for (var i = 0; i < moves.length; i++) {
				sendDisplayMove(moves[i]);
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
	xhr.open("POST", "/undo-move", true);
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE && xhr.response) {
			var moves = JSON.parse(xhr.response);
			for (var i = 0; i < moves.length; i++) {
				sendDisplayMove(moves[i]);
			}
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
	event.stopPropagation();
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/compute-move", true);
	xhr.setRequestHeader("Content-Type", "application/json");
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE) {
			var move = JSON.parse(xhr.response);
			sendDisplayMove(move);
		}
	}
	
	var jsonMoves = [];
	
	for (var i = 0; i < moves.length; i++) {
		jsonMoves.push({
			columnIndex: moves[i].columnIndex,
			rowIndex: moves[i].rowIndex,
			color: moves[i].color == "BLACK" ? 0 : 1
		});
	}
	
	var jsonGame = {
		boardSize: boardSize,
		moves: jsonMoves
	};
	
	xhr.send(JSON.stringify(jsonGame));
}

const displayMove = (move) => {

	const cells = document.querySelectorAll(".emptyCell");

	for (const element of cells) {
		var cell = element;
		var column = parseInt(cell.id.split("-")[0]);
		var row = parseInt(cell.id.split("-")[1]);

		if (column == move.columnIndex && row == move.rowIndex) {
			cell.classList.remove('white-piece');
			cell.classList.remove('black-piece');
			if (move.color == "BLACK") {
				cell.classList.add('black-piece');
			} else if (move.color == "WHITE") {
				cell.classList.add('white-piece');
			} else if (move.color == "GREEN") {
				cell.classList.add('green-piece');
			}
		}
	}
}

const refreshGame = (moves) => {
	for (var i = 0; i < moves.length; i++) {
		displayMove(moves[i]);
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
			columnIndex: move.columnIndex,
			rowIndex: move.rowIndex,
			color: move.color,
			type: "DISPLAY_MOVES"
		};
		stompClient.send("/app/refresh", {}, JSON.stringify(webSocketMessage))
	}
}

const connectToWebSocket = () => {
	const socket = new SockJS('/board-moves')
	stompClient = Stomp.over(socket)
	stompClient.connect({}, onConnected)
}

const onConnected = () => {
	stompClient.subscribe('/topic/public', onReceive)
}

const onReceive = (payload) => {
	const webSocketMessage = JSON.parse(payload.body);

	if (webSocketMessage.type == "RELOAD") {
		location.reload();
	} else if (webSocketMessage.type == "DISPLAY_MOVES") {
		displayMove(webSocketMessage);
	}
}

var stompClient;

function main() {

	connectToWebSocket();

	initCellListeners();

	initButtonListeners();

	refreshGame(moves);
}


main();


