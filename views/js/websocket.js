/*
 webSocket Secure Library v0.1.0 - Modified
 http://windweller.info

 Copyright 2012, Allen Nie
 Licensed under the MIT license.

 Requires jQuery
 Compatible with IE9+, Chrome, Firefox, Safari.

 Warning: this library has been modified to better
 serve the localhost enviornment

 Usage
 1. webSocket.initiate("URL address")
 2. webSocket.send()
 3. webSocket.close()
 */

var webSocket = (function() {
    'use strict';

    var socketURL = "";

    function initiate(url) {
        socketURL = url;
    }

    var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
    var scriptSocket = new WS(socketURL);

    scriptSocket.onopen = function() {
        console.log("WebSocket server is ready.");
    };

    scriptSocket.onmessage = function(event) {
        console.log(event.data);
    };

    scriptSocket.onerror = function(event) {
        console.log("There is an error occurring.");
    };

    function close() {
        scriptSocket.close();
    }

    function send(mesg) {
        if (typeof mesg === "string") {
            scriptSocket.send(mesg);
        }
    }

    return {
        initiate: initiate,
        socket: scriptSocket, //return to give user customizing option
        send: send,
        close: close
    };

}());