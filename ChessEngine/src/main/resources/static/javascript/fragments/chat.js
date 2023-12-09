(function() {
    'use strict';

    var stompClient = null;
    var username = null;
    var matchId = null;

var colors = ['#2196F3', '#32c787', '#00BCD4', '#ff5652', '#ffc107', '#ff85af', '#FF9800', '#39bbb0'];

function connect() {
    var socket = new SockJS('/online/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    matchId = window.location.pathname.split('/').pop();  // Set matchId after connecting
    stompClient.subscribe('/topic/match.' + matchId, onMessageReceived);
}

function onError(error) {
    console.error('Could not connect to WebSocket server:', error);
}

function send(event) {
    event.preventDefault();  // Prevent the default form submission behavior

    var messageInput = document.querySelector('#message');
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };

        stompClient.send("/app/chat.send." + matchId, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
}

    function onMessageReceived(payload) {
    var messageArea = document.getElementById('messageArea');
        var message = JSON.parse(payload.body);
        console.log(username)

        // Handle the received message, update the UI, etc.
        console.log('Received message:', message.content);

        // Add the message content to the chat area
        var messageElement = document.createElement('li');
        messageElement.classList.add('chat-message');

        var contentElement = document.createElement('p');
        var contentText = document.createTextNode(message.content);
        contentElement.appendChild(contentText);

        messageElement.appendChild(contentElement);
        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    }


// Initialize the connection when the page loads
connect();

// Attach the send function to the form submission
document.querySelector('#messageForm').addEventListener('submit', send);
})();