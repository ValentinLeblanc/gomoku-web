
const onChallengeAction = (event) => {
	event.stopPropagation();
	var challengeButton = event.target;
	challengeButton.disabled = true;
	var username = challengeButton.id.replace("challenge-", "");
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/challenge/" + username, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.withCredentials = true;
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.send("");
}

const onAcceptAction = (event) => {
	event.stopPropagation();
	var username = event.target.id.replace("accept-", "");
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/accept/" + username, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.withCredentials = true;
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.send("");
}

const onDeclineAction = (event) => {
	event.stopPropagation();
	var username = event.target.id.replace("decline-", "");
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "/decline/" + username, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.withCredentials = true;
	var header = this._csrf.headerName;
	var token = this._csrf.token;
	xhr.setRequestHeader(header, token);
	xhr.send("");
}

function addConnectedUser(connectedUsername) {
	
	var connectedUsersContainer = document.getElementById("connectedUsersContainer");

	var newConnectedUserBlock = document.createElement("div");
	newConnectedUserBlock.classList.add("row", "align-items-center", "mb-2");

	var col1 = document.createElement("div");
	col1.classList.add("col");
	col1.textContent = connectedUsername;
	
	newConnectedUserBlock.appendChild(col1);
	
	if (connectedUsername != username) {
		var col2 = document.createElement("div");
		col2.classList.add("col");
		var challengeButton = document.createElement("button");
		challengeButton.classList.add("btn", "btn-primary");
		challengeButton.textContent = "Challenge";
		challengeButton.id = "challenge-" + connectedUsername;
		col2.appendChild(challengeButton);
		challengeButton.addEventListener("click", onChallengeAction);
		if (challengeTargets.includes(connectedUsername)) {
			challengeButton.disabled = true;
		}
		newConnectedUserBlock.appendChild(col2);
	}

	connectedUsersContainer.appendChild(newConnectedUserBlock);
}

function addChallenger(challengerName) {
	
	var challengesContainer = document.getElementById("challengesContainer");

	var newChallengeBlock = document.createElement("div");
	newChallengeBlock.classList.add("row", "align-items-center", "mb-2");

	var col1 = document.createElement("div");
	col1.classList.add("col");
	col1.textContent = challengerName;

	var col2 = document.createElement("div");
	col2.classList.add("col");
	var acceptButton = document.createElement("button");
	acceptButton.classList.add("btn", "btn-primary");
	acceptButton.textContent = "Accept";
	acceptButton.id = "accept-" + challengerName;
	acceptButton.addEventListener("click", onAcceptAction);
	col2.appendChild(acceptButton);

	var col3 = document.createElement("div");
	col3.classList.add("col");
	var declineButton = document.createElement("button");
	declineButton.classList.add("btn", "btn-primary");
	declineButton.textContent = "Decline";
	declineButton.id = "decline-" + challengerName;
	declineButton.addEventListener("click", onDeclineAction);
	col3.appendChild(declineButton);

	newChallengeBlock.appendChild(col1);
	newChallengeBlock.appendChild(col2);
	newChallengeBlock.appendChild(col3);

	challengesContainer.appendChild(newChallengeBlock);
}

function enableChallengeButton(targetUserName) {
	var connectedUsersContainer = document.getElementById("connectedUsersContainer");
	var connectedUsersBlocks = connectedUsersContainer.getElementsByClassName("row","align-items-center", "mb-2");
	for (const element of connectedUsersBlocks) {
		var col1 = element.querySelector(".col:nth-child(1)");
		if (col1 && col1.innerText == targetUserName) {
			var col2 = element.querySelector(".col:nth-child(2)");
			var challengeButton = col2.children[0];
			challengeButton.disabled = false;
			return;
		}
	}
}

function removeConnectedUser(disconnectedUsername) {
	var connectedUsersContainer = document.getElementById("connectedUsersContainer");
	var connectedUsersBlocks = connectedUsersContainer.getElementsByClassName("row","align-items-center", "mb-2");

	for (const element of connectedUsersBlocks) {
		var col1 = element.querySelector(".col:nth-child(1)");
		if (col1 && col1.innerText == disconnectedUsername) {
			connectedUsersContainer.removeChild(element);
			return;
		}
	}
}

function removeChallenger(challenger) {
	var challengesContainer = document.getElementById("challengesContainer");
	var challengerBlocks = challengesContainer.getElementsByClassName("row","align-items-center", "mb-2");
	for (const element of challengerBlocks) {
		var col1 = element.querySelector(".col:nth-child(1)");
		if (col1 && col1.innerText == challenger) {
			challengesContainer.removeChild(element);
			return;
		}
	}
}
const connectToGameWebSocket = () => {
	const socket = new SockJS('/gameMessages')
	stompClient = Stomp.over(socket)
	stompClient.connect({}, onConnected)
}

const onConnected = () => {
	stompClient.subscribe('/web/public', onReceive)
}

const onReceive = (payload) => {
	const webSocketMessage = JSON.parse(payload.body);
	
	if (webSocketMessage.type == "USER_CONNECTED") {
		var connectedUser = webSocketMessage.content;
		if (connectedUser != username) {
			addConnectedUser(connectedUser);
		}
	} else if (webSocketMessage.type == "USER_DISCONNECTED") {
		var disconnectedUser = webSocketMessage.content;
		removeConnectedUser(disconnectedUser);
 	} else if (webSocketMessage.type == "NEW_CHALLENGER") {
		var newChallengerInfo = webSocketMessage.content;
		var challenger = newChallengerInfo.split(">")[0];
		var challengeTarget = newChallengerInfo.split(">")[1];
		if (challengeTarget == username) {
			addChallenger(challenger);
		}
	} else if (webSocketMessage.type == "CHALLENGE_ACCEPTED") {
		var acceptChallengeInfo = webSocketMessage.content;
		if (acceptChallengeInfo.split(",").includes(username)) {
			window.location.href = "/game/online";
		}
	} else if (webSocketMessage.type == "CHALLENGE_DECLINED") {
		var declineChallengeInfo = webSocketMessage.content;
		var challenger = declineChallengeInfo.split(">")[0];
		var challengeTarget = declineChallengeInfo.split(">")[1];
		if (challengeTarget == username) {
			removeChallenger(challenger);
		} else if (challenger == username) {
			enableChallengeButton(challengeTarget);
		}
	}
}

var stompClient;

function main() {
	connectToGameWebSocket();
	addConnectedUser(username);
	for (var i in connectedUsers) {
		var connectedUser = connectedUsers[i];
		addConnectedUser(connectedUser);
	}
	for (var j in challengers) {
		var challenger = challengers[j];
		addChallenger(challenger);
	}
}

main();


