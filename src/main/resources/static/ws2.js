var stompClient = null;

function connect() {
    stompClient = Stomp.client('ws://localhost:8888/ws');
    alert("Connection established: " +stompClient.toString());
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/messages/usuario2', function (response) {
            alert(response)
            showGreeting(JSON.parse(response.body).content);
        });
    });
}


function sendName() {
	stompClient.send("/app/chat/usuario1", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
	connect();
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#send" ).click(function() { sendName(); });
});