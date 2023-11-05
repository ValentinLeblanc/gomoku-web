
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
	
	if (webSocketMessage.type == "USER_CONNECTED" || webSocketMessage.type == "USER_DISCONNECTED") {
		connectedUsersTable.ajax.reload();
 	} else if (webSocketMessage.type == "NEW_CHALLENGER" || webSocketMessage.type == "CHALLENGE_DECLINED") {
		challengersTable.ajax.reload();
		connectedUsersTable.ajax.reload();
	} else if (webSocketMessage.type == "CHALLENGE_ACCEPTED") {
		var acceptChallengeInfo = webSocketMessage.content;
		if (acceptChallengeInfo.split(",").includes(username)) {
			window.location.href = "/game/online";
		}
	}
}

var stompClient;

function main() {
	connectToGameWebSocket();
}

main();


