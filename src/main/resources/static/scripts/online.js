
const onChallengeAction = (event) => {
	event.stopPropagation();
	var username = event.target.id.replace("challenge-", "");

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
	
	if (connectedUsername == username) {
		return;
	}
	
	var connectedUsersContainer = document.getElementById("connectedUsersContainer");

	var newConnectedUserBlock = document.createElement("div");
	newConnectedUserBlock.classList.add("row", "align-items-center", "mb-2");

	var col1 = document.createElement("div");
	col1.classList.add("col");
	col1.textContent = connectedUsername;

	var col2 = document.createElement("div");
	col2.classList.add("col");
	var challengeButton = document.createElement("button");
	challengeButton.classList.add("btn", "btn-primary");
	challengeButton.textContent = "Challenge";
	challengeButton.id = "challenge-" + connectedUsername;
	col2.appendChild(challengeButton);

	newConnectedUserBlock.appendChild(col1);
	newConnectedUserBlock.appendChild(col2);

	connectedUsersContainer.appendChild(newConnectedUserBlock);
}

function addChallenger(challengerInfo) {
	
	var challengerName = challengerInfo.split("=>")[0];
	var targetName = challengerInfo.split("=>")[1];
	
	if (targetName != username) {
		return;
	}
	
	var challengesContainer = document.getElementById("challengesReceivedContainer");

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
	col2.appendChild(acceptButton);

	var col3 = document.createElement("div");
	col3.classList.add("col");
	var declineButton = document.createElement("button");
	declineButton.classList.add("btn", "btn-primary");
	declineButton.textContent = "Decline";
	declineButton.id = "decline-" + challengerName;
	col3.appendChild(declineButton);

	newChallengeBlock.appendChild(col1);
	newChallengeBlock.appendChild(col2);
	newChallengeBlock.appendChild(col3);

	challengesContainer.appendChild(newChallengeBlock);
}

function removeConnectedUser(disconnectedUsername) {
	var connectedUsersContainer = document.getElementById("connectedUsersContainer");
	var connectedUsersBlocks = connectedUsersContainer.getElementsByClassName("row","align-items-center", "mb-2");

	for (const element of connectedUsersBlocks) {
		var col1 = element.querySelector(".col:nth-child(1)");
		if (col1 && col1.innerText == disconnectedUsername) {
			connectedUsersContainer.removeChild(element);
			break; // Remove only the first match (if there are multiple challengers with the same name)
		}
	}
}

function removeChallenger(challengerName) {
	var challengesContainer = document.getElementById("challengesReceivedContainer");
	var challengerBlocks = challengesContainer.getElementsByClassName("row","align-items-center", "mb-2");

	for (const element of challengerBlocks) {
		var col1 = element.querySelector(".col:nth-child(1)");
		if (col1 && col1.innerText == challengerName) {
			challengesContainer.removeChild(element);
			break; // Remove only the first match (if there are multiple challengers with the same name)
		}
	}
}

function challengeAccepted(usernames) {
	if (usernames.split(",").includes(username)) {
		window.location.href = "/game/online";
	}
}

const initButtonListeners = () => {
	var challengeButtons = document.querySelectorAll('[id^="challenge-"]');
	for (const challengeButton of challengeButtons) {
		if (challengeButton.id.replace("challenge-", "") == username) {
			challengeButton.hidden = true;
		} else {
			challengeButton.addEventListener("click", onChallengeAction);
		}
	}
	var acceptButtons = document.querySelectorAll('[id^="accept-"]');
	for (const acceptButton of acceptButtons) {
		acceptButton.addEventListener("click", onAcceptAction);
	}
	var declineButtons = document.querySelectorAll('[id^="decline-"]');
	for (const declineButton of declineButtons) {
		declineButton.addEventListener("click", onDeclineAction);
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
	if (webSocketMessage.type == "NEW_CHALLENGER") {
		addChallenger(webSocketMessage.content);
		initButtonListeners();
	} else if (webSocketMessage.type == "REMOVE_CHALLENGER") {
		removeChallenger(webSocketMessage.content);
	} else if (webSocketMessage.type == "CHALLENGE_ACCEPTED") {
		challengeAccepted(webSocketMessage.content);
	} else if (webSocketMessage.type == "CONNECTED_USER") {
		addConnectedUser(webSocketMessage.content);
	} else if (webSocketMessage.type == "DISCONNECTED_USER") {
		removeConnectedUser(webSocketMessage.content);
	}
}

var stompClient;

function main() {
	connectToGameWebSocket();
	initButtonListeners();
}

main();


