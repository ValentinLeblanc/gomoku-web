
const initButtonListeners = () => {
	var addBoardButton = document.getElementById("add-board");
	addBoardButton.addEventListener("click", onAddBoardAction);
}

const onAddBoardAction = (event) => {
	event.stopPropagation();
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "http://localhost:8080/add-board", true);
	xhr.withCredentials = true;
	xhr.onreadystatechange = function() {
		if (xhr.readyState == XMLHttpRequest.DONE) {
			sendReload();
		}
	}
	xhr.send({});
}

const sendReload = () => {
	if (stompClient) {
		const webSocketMessage = {
			type: "RELOAD"
		}
		stompClient.send("/app/refresh", {}, JSON.stringify(webSocketMessage))
	}
}

const onReceive = (payload) => {
	const webSocketMessage = JSON.parse(payload.body);

	if (webSocketMessage.type == "RELOAD") {
		location.reload();
	}
}

const onConnected = () => {
	stompClient.subscribe('/web/public', onReceive)
}

const connectToWebSocket = () => {
	const socket = new SockJS('/online-boards')
	stompClient = Stomp.over(socket)
	stompClient.connect({}, onConnected)
}


var stompClient;

function main() {
	
	connectToWebSocket();

	initButtonListeners();
}


main();


