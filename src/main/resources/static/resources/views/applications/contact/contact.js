
var contact = angular.module('FitnessTracker.Contact', ['ngRoute', 'ngMaterial','Friend']);

contact.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/contact', {
        templateUrl: '/resources/views/applications/contact/contact.html',
        controller:'FitnessTracker.contact.controller'
    })
}]);

contact.directive('scrollToBottom', function($timeout, $window) {
    return {
        scope: {
            scrollToBottom: "="
        },
        restrict: 'A',
        link: function(scope, element, attr) {
            scope.$watchCollection('scrollToBottom', function(newVal) {
                if (newVal) {
                    $timeout(function() {
                        element[0].scrollTop =  element[0].scrollHeight;
                    }, 0);
                }

            });
        }
    };
});

contact.filter('checkDateTime', function () {
    function formatMsgTime (timespan) {
        var dateTime = new Date(timespan);
        var year = dateTime.getFullYear();
        var month = dateTime.getMonth() + 1;
        var day = dateTime.getDate();
        var hour = dateTime.getHours();
        var minute = dateTime.getMinutes();
        var second = dateTime.getSeconds();
        var now = new Date();
        var now_new = now.getTime();
        var milliseconds = 0;
        var timeSpanStr;
        milliseconds = now_new - timespan;
        if (milliseconds <= 1000 * 60 * 1) {
            timeSpanStr = 'just now';
        }
        else if (1000 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60) {
            timeSpanStr = Math.round((milliseconds / (1000 * 60))) + ' mins';
        }
        else if (1000 * 60 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24) {
            timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60)) + ' hr';
        }
        else if (1000 * 60 * 60 * 24 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24 * 15) {
            timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60 * 24)) + ' day';
        }
        else if (milliseconds > 1000 * 60 * 60 * 24 * 15 && year == now.getFullYear()) {
            timeSpanStr = month + '-' + day + '@' + hour + ':' + minute;
        } else {
            timeSpanStr = year + '-' + month + '-' + day + '@' + hour + ':' + minute;
        }
        return timeSpanStr;
    }
    return function (commentDate) {
        return formatMsgTime(commentDate);
    }
});



contact.controller('FitnessTracker.contact.controller',['$location', '$scope', '$http', '$resource', 'Session.Service','FriendService', 'ChatSocket', '$rootScope', '$route',function($location, $scope, $http, $resource, Session, FriendHandler, chatSocket, $rootScope, $route, init) {
    $scope.inputValue = {
        content: '',
        receiverUsername : null
    };
    $scope.messages = [];
    // $scope.privateMessages = {};
    $scope.friendsTypeTabs = [];
    $scope.friendsDetails = {};
    // $scope.currentChatObject = null;
    var ChatMessage = $resource('/fitnesstracker/api/v1/messages/user/:uid/group/:gid/friend/:fid', {}, {query : {method: 'GET', isArray : true,
        transformResponse: function(response) {
            var newData = JSON.parse(response);
            newData.map(function(data) {
                data._length = data.length;
                return data;
            });
            return newData;
        }
    }});

    var ReminderMessage = $resource('/fitnesstracker/api/v1/message/user/:uid/group/:gid/reminder',{}, {query : {method : 'GET', isArray : true,
        transformResponse: function(response) {
            var newData = JSON.parse(response);
            newData.map(function(data) {
                data._length = data.length;
                return data;
            });
            return newData;
        }
    }, delete : {method : 'DELETE', isArray : true}});

    var Friend = $resource('/fitnesstracker/api/v1/user/:uid/friends/:fid', {}, {delete : {method : 'DELETE'}});
    $scope.user = Session.user();

    var GroupFriends = $resource('/fitnesstracker/api/v1/user/:uid/group/:gid/friend/:fid', {}, {delete : {method : 'DELETE'}});

    //----------------------group friends ------------
    var getOrderdFriendInfo = function ( groupOfFriends ) {
        var groupFriends = groupOfFriends.groupUsers;
        // console.log("group users : ", groupFriends);
        $scope.friendsTypeTabs = [];
        $scope.friendsDetails = {};
        for( i = 0; i < groupFriends.length; i ++){
            var username = groupFriends[i].username;
            if(!$scope.friendsTypeTabs.includes(username.charAt(0).toUpperCase())){
                $scope.friendsTypeTabs.push(username.charAt(0).toUpperCase());
                $scope.friendsDetails[username.charAt(0).toUpperCase()] = [];
            }
            $scope.friendsDetails[username.charAt(0).toUpperCase()].push(groupFriends[i]);
        }
    };
    // ----------------------------------------------
    $scope.optionalShowForFriend = true;
     $rootScope.init = function () {
         var type = FriendHandler.getType();
         // console.log("contact type :: ", type);
        if(type == 'friend'){
            $scope.contactIsFriend = true;
            $scope.optionalShowForFriend = true;
            $scope.friend = FriendHandler.getter('friend');
            // console.log("get friends out", $scope.friend);
            setReceiver($scope.friend.friend.username);
        }else{
            $scope.contactIsFriend = false;
            $scope.optionalShowForFriend = false;
            $scope.group = FriendHandler.getter('group');
            $scope.roomId = $scope.group.id;
            setReceiver($scope.roomId);
            // console.log("further information friends group :: " , $scope.group);
            getOrderdFriendInfo( $scope.group );
        }

        // console.log("root scope init", $scope.friend);
        $scope.inputValue = {
            content: '',
            receiverUsername : null
        };
    };

    $scope.showDeleteButton = false;
    $scope.showButton = function(){
        $scope.showDeleteButton = !$scope.showDeleteButton;
    };
    $scope.deleteFriend = function(friend){
        console.log("delete friend ", friend);
        $scope.backToPost();
        Friend.delete({uid : $scope.user.id, fid : $scope.friend.id},
            function (value) {
                // console.log("delete friend success");
                console.log("delete friends");
                $rootScope.initialFriends();
                chatSocket.send("/app/user/" + $scope.user.id + "/delete/friends/" + friend.friend.id, {}, true);
            },
            function (error) {
                // console.log("error", error);
            }
        );
    };

    $scope.rollOutGroup = function () {
        var user = $scope.user;
        var group = $scope.group;
        if(user == null || group == null){
            return;
        }
        GroupFriends.delete({uid : user.id, gid : group.id, fid : user.id},
            function (value) {
                $rootScope.initialFriends();
                // console.log("value", value);
                chatSocket.send("/app/user/" + $scope.user.id + "/delete/group/" + group.id, {}, true);
            },
            function (error) {
                // console.log("error", error);
            }
        );
        delete sessionStorage.group;
        $scope.backToPost();
    };

    $scope.backToPost = function(){
        $location.url("/sharings");
    };

    $scope.sendMessage = function () {
        if($scope.inputValue.content == ''){
            return;
        }
        if($scope.contactIsFriend){
            sendMessageToFriend();
        }else{
            sendMessageToGroup();
        }
        $scope.inputValue.content = '';
    };

    var sendMessageToGroup = function () {
        var roomId = $scope.group.id;
        if(roomId == null){
            return;
        }
        var message = {
            sender : $scope.user,
            roomId : roomId,
            content : $scope.inputValue.content
        };
        if(!$rootScope.privateMessages[message.roomId]){
            $rootScope.privateMessages[message.roomId] = [];
        }
        chatSocket.send("/app/user/"+$scope.user.id+"/group/"+message.roomId, {}, JSON.stringify(message));
    };


    var sendMessageToFriend = function () {
        var message = {
            content : $scope.inputValue.content,
            receiverUsername : $scope.friend.friend.username
        };
        var chatMessage = {
            sender : $scope.user,
            content : message.content,
            datetime : new Date().getTime()
        };
        if(!$rootScope.privateMessages[message.receiverUsername]){
            $rootScope.privateMessages[message.receiverUsername] = [];
        }
        $rootScope.privateMessages[message.receiverUsername].push(chatMessage);
        chatSocket.send("/app/chat", {}, JSON.stringify(message));
    };




    var messageWrapper= $('.chat');
    $scope.scrollToBottom=function(){
        messageWrapper.scrollTop(messageWrapper[0].scrollHeight);
    };

    var setReceiver = function(username){
        $rootScope.currentChatObject = username;
        if(!$rootScope.privateMessages[username]){
            $rootScope.privateMessages[username] = [];
        }
        if(!$rootScope.newReceiverMessages[username]){
            $rootScope.newReceiverMessages[username] = {};
        }
        $rootScope.newReceiverMessages[username].num = 0;
        if($scope.contactIsFriend){
            ChatMessage.query({uid : $scope.user.id, fid : $scope.friend.friend.id},
                function (data) {
                    console.log("friend messages datas", data);
                    if(data.length > 0){
                        setViewMessageFromFriend(data[data.length - 1].id);
                    }


                    $rootScope.privateMessages[username] = data;
                    $scope.messages = $rootScope.privateMessages[username];
                },
                function (error) {
                    $scope.messages = $rootScope.privateMessages[username];
                }
            );
        }else{
            $rootScope.newReceiverMessages[username].lastContent = '';
            ChatMessage.query({uid : $scope.user.id, gid : $scope.roomId},
                function (data) {
                    $rootScope.privateMessages[username] = data;
                    $scope.messages = $rootScope.privateMessages[username];
                },
                function (error) {
                    $scope.messages = $rootScope.privateMessages[username];
                }
            );
            ///fitnesstracker/api/v1/message/user/:uid/group/:gid/reminder
            ReminderMessage.delete({uid : $scope.user.id, gid : username},
                function (datas) {
                    console.log("success delete", datas);
                },
                function (error) {
                    console.log("error", error);
                }
            );

        }

    };

    var setViewMessageFromFriend = function (messageId) {
        $http({
            url: '/fitnesstracker/api/v1/messages/user/'+$scope.user.id+'/isRead/' + messageId,
            method: 'PUT'
        }).then(function (data) {
            console.log("update success", data);
        });
    };
    
    $scope.reloadMessage = function () {
        setReceiver($scope.friend.friend.username);
    };

    // var initStompClient = function() {
    //     chatSocket.init('/any-socket');
    //     chatSocket.connect(
    //         function(frame) {
    //             chatSocket.subscribe("/user/topic/chat",
    //                 function (message) {
    //                     var receiverMsg = JSON.parse(message.body);
    //                     console.log("receive message", receiverMsg);
    //                     if(!$scope.privateMessages[receiverMsg.sender.username]){
    //                         $scope.privateMessages[receiverMsg.sender.username] = [];
    //                     }
    //                     $scope.privateMessages[receiverMsg.sender.username].push(receiverMsg);
    //                     if(receiverMsg.sender.username != $scope.currentChatObject){
    //                         if($rootScope.newReceiverMessages[receiverMsg.sender.username] == null){
    //                             $rootScope.newReceiverMessages[receiverMsg.sender.username] = {};
    //                             $rootScope.newReceiverMessages[receiverMsg.sender.username].num = 0;
    //                         }
    //                         $rootScope.newReceiverMessages[receiverMsg.sender.username].num++;
    //                         console.log("reminder message from friend", $rootScope.newReceiverMessages);
    //                     }else{
    //                         $http({
    //                             url: '/fitnesstracker/api/v1/messages/user/'+$scope.user.id+'/isRead/' + receiverMsg.id,
    //                             method: 'PUT'
    //                         }).then(function (data) {
    //                             console.log("update success", data);
    //                         })
    //                     }
    //             });
    //
    //             chatSocket.subscribe("/user/topic/chat/group",
    //                 function (message) {
    //                     var receiverMsg = JSON.parse(message.body);
    //                     console.log("group chat room message", receiverMsg);
    //                     if(!$scope.privateMessages[receiverMsg.roomId]){
    //                         $scope.privateMessages[receiverMsg.roomId] = [];
    //                     }
    //                     $scope.privateMessages[receiverMsg.roomId].push(receiverMsg);
    //                     if(receiverMsg.roomId != $scope.currentChatObject){
    //                         if($rootScope.newReceiverMessages[receiverMsg.roomId] == null){
    //                             $rootScope.newReceiverMessages[receiverMsg.roomId] = {};
    //                             $rootScope.newReceiverMessages[receiverMsg.roomId].num = 0;
    //                             $rootScope.newReceiverMessages[receiverMsg.roomId].lastContent = '';
    //                         }
    //                         $rootScope.newReceiverMessages[receiverMsg.roomId].num ++;
    //                         $rootScope.newReceiverMessages[receiverMsg.roomId].lastContent = receiverMsg.content;
    //                         console.log("reminder message from one group room", receiverMsg.roomId,$rootScope.newReceiverMessages);
    //                     }else{
    //                         // $http({
    //                         //     url: '/fitnesstracker/api/v1/messages/user/'+$scope.user.id+'/isRead/' + receiverMsg.id,
    //                         //     method: 'PUT'
    //                         // }).then(function (data) {
    //                         //     console.log("update success", data);
    //                         // })
    //                         ReminderMessage.delete({uid : $scope.user.id, gid : receiverMsg.roomId},
    //                             function (datas) {
    //                                 console.log("success delete", datas);
    //                             },
    //                             function (error) {
    //                                 console.log("error", error);
    //                             }
    //                         );
    //                     }
    //                 }
    //             );
    //         }, function(error) {
    //             console.log("chat app error", error);
    //         }
    //     );
    // };

    // initStompClient();
    $rootScope.init();
}]);

contact.directive('chatMessage', ['$timeout', function($timeout) {
    return {
        restrict: 'E',
        templateUrl: '/resources/views/applications/contact/message.html',
        scope:{
            message:"=",
            scrolltothis:"&",
            username : "="
        },
        link:function(scope, elem, attrs){
            $timeout(scope.scrolltothis);
        }
    };
}]);

contact.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if (event.which === 13) {
                scope.$apply(function () {
                    scope.$eval(attrs.ngEnter);
                });
                event.preventDefault();
            }
        });
    };
});