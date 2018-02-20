/* Services */

angular.module('ChatApp', []).factory('ChatSocket',
    [ '$rootScope', function($rootScope) {
        var stompClient;
        var wrappedSocket = {
            init : function(url) {
                console.log("chat url", url);
                var socket = new SockJS(url);
                stompClient = Stomp.over(socket);//new SockJS(url));
            },
            connect : function(successCallback, errorCallback) {
                stompClient.connect({}, function(frame) {
                    $rootScope.$apply(function() {
                        successCallback(frame);
                    });
                }, function(error) {
                    $rootScope.$apply(function() {
                        errorCallback(error);
                    });
                });
            },
            subscribe : function(destination, callback) {
                stompClient.subscribe(destination, function(message) {
                    $rootScope.$apply(function() {
                        callback(message);
                    });
                });
            },
            send : function(destination, headers, object) {
                stompClient.send(destination, headers, object);
            }
        }
        return wrappedSocket;
    }
    ]);