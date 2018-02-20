var app = angular.module('FitnessTracker.App', [ 'ngRoute',
												'ui.bootstrap',
												'infinite-scroll',
											 'ngMaterial',
                                             'spring-security-csrf-token-interceptor', 
                                             'Session',
											 'Friend',
												'ChatApp',
												'FitnessTacker.LoadImage',
                                             'FitnessTracker.Login',
											 'FitnessTracker.Register',
											 'FitnessTracker.Profile',
											 'FitnessTracker.selectDevice',
											 'FitnessTracker.Application.Comments',
											'FitnessTracker.Active.summary',
											'FitnessTracker.Active',
											'FitnessTracker.Goals',
											'FitnessTracker.userBody',
											'FitnessTracker.sharings',
											'FitnessTracker.mySharing',
											'FitnessTracker.Contact',
											'FitnessTracker.createShare'
	]);

app.controller('FitnessTracker.Navigation.Controller', [ '$mdSidenav', '$log', '$rootScope', '$scope', '$http', '$location', 'Session.Service','FriendService', '$resource', '$http', '$sce', 'ChatSocket', function($mdSidenav, $log, $rootScope, $scope, $http, $location, Session, FriendHandler, $resource, $http, $sce, chatSocket) {
	var Friends = $resource('/fitnesstracker/api/v1/user/:uid/friends/:fid', {}, {query : {method: 'GET', isArray : true}, update : {method : 'PUT'}, delete : {method : 'DELETE'}, save : {method : 'POST'}});
	var Reminders = $resource('/fitnesstracker/api/v1/user/:uid/requests/:rid', {}, {query : {method : 'GET', isArray: true}, update : {method : 'PUT'}, save : {method : 'POST'}});
	var Users = $resource('/user/:uid/research', {}, {query : {method : 'GET', isArray : true}});
	var Groups = $resource('/fitnesstracker/api/v1/user/:uid/group', {}, {query : {method: 'GET', isArray : true}, save : {method : 'POST'}})
	var ChatMessage = $resource('/fitnesstracker/api/v1/messages/user/:uid/group/:gid/friend/:fid', {}, {query : {method: 'GET', isArray : true,
		transformResponse: function(response) {
			var newData = JSON.parse(response);
			if(newData == null) return [];
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
			if(newData == null) return [];
			newData.map(function(data) {
				data._length = data.length;
				return data;
			});
			return newData;
		}
	}, delete : {method : 'DELETE', isArray : true}});
	$scope.conditionPrompt = true;
	$scope.isAuthenticated = Session.isAuthenticated;
	$scope.username = function() {
		$scope.user = Session.user();
		var user = Session.user();
		return user && user.username;
	};

	$scope.group = {
		name : "",
		groupIds : []
	};
	$scope.ToProfile = function () {
		$location.url('/profile');
	}
	
	$scope.hasRole = Session.hasRole;
	
	$scope.logout = function() {
		var req = {
				method : 'POST',
				url : '/logout',
					};
			
		$http(req).then(function() {
			console.log("log out ;;; ");
			Session.destroy();
			$location.path('/');
		});
	};

	$scope.isOpenRight = function(){
		return $mdSidenav('right').isOpen();
	};

	$scope.showGroupsView = function () {
		$scope.toggleRight();
		$scope.changeViewToFriends(false);
		$scope.friendNewGroupReminder.num = 0;
	};
	$scope.showMessageView = function () {
		$scope.toggleRight();
		$scope.friendNewMessageReminder.num = 0;
	}

	$scope.toggleRight = function() {
		if(!$scope.isOpenRight()){
			// $rootScope.initialFriends();
		}
			$mdSidenav('right')
				.toggle()
				.then(function () {
					$log.debug("toggle is done");
				});
	};







	//---------------------------right Controller ------------
	var flag = true;
	$scope.queryParams = {
		value : ''
	};
	$scope.demo = {
		showTooltip: false,
		tipDirection: 'left'
	};
	$scope.view = {
		friendsTable : true,
		friendGroupsTable : false
	};
	$scope.showAllNotification = true;
	$scope.friends = [];
	$scope.userItem = [];
	$scope.temp = [];
	$scope.reminder = [];
	$scope.friendReminders = [];
	$scope.friendGroups = [];
	$scope.selected = [];
	$scope.showSearchQuery = false;
	$scope.isView = false;
	$rootScope.newReceiverMessages = {};
	$rootScope.privateMessages = {};
	$rootScope.currentChatObject = null;
	$scope.changeTypeQuery = function() {
		$scope.showSearchQuery = !$scope.showSearchQuery;
	};
	$scope.friendNewGroupReminder = {
		num : 0
	};

	$scope.friendNewMessageReminder = {
		num : 0
	};



	$rootScope.initialFriends = function(){
		$scope.friendNewGroupReminder = {
			num : 0
		};
		$scope.friendNewMessageReminder = {
			num : 0
		};
		if(!$scope.user){
			var user = Session.user();
			console.log("user", user);
			if(user){
				$scope.user = Session.user();
				console.log("initial friends information", $scope.user);
			}else{
				return;
			}
		}
		// --- find all frinds ----
		refreshFriendTable();

		// configNewReceiverMessagesByFriends($scope.friends);
		// ------ find all friends requests ........
		refreshFriendRequest();

		// ------- get all group friends-----
		refreshGroupOfFriends();
	};

	var refreshGroupOfFriends = function () {
		$scope.friendGroups = Groups.query({uid : $scope.user.id});
	};

	var refreshFriendRequest = function () {
		$scope.friendReminders = Reminders.query({uid : $scope.user.id});
	};

	var refreshFriendTable = function () {
		$scope.friends = Friends.query({uid : $scope.user.id});
	};


	// var ChatMessage = $resource('/fitnesstracker/api/v1/messages/user/:uid/group/:gid/friend/:fid', {}, {query : {method: 'GET', isArray : true}});

	function checkForNewMessages( datas ) {
		console.log("datas value", datas);
		var num = 0;
		for( i = datas.length - 1; i >= 0 && datas[i].read == false; i --){
			if(datas[i].sender.username != $scope.user.username){
				num ++;
			}
		}
		return num;
	}

	$scope.initialNewMessageFromFriend = function (friend) {
		ChatMessage.query({uid : $scope.user.id, fid : friend.id}, 
			function (datas) {
				var numberofNewMessages = checkForNewMessages(datas);
				if(!$rootScope.newReceiverMessages[friend.username]){
					$rootScope.newReceiverMessages[friend.username] = {};
				}
				console.log("initial friends reminder form", datas, numberofNewMessages);
				$rootScope.newReceiverMessages[friend.username].num = numberofNewMessages;
				$scope.friendNewMessageReminder.num += numberofNewMessages;
			},
			function (error) {
				console.log("error", error);
			}
		);
	};
	$scope.initialNewMessageFromGroup = function(gid){
		ReminderMessage.query({uid : $scope.user.id, gid : gid}, 
			function (datas) {
				var newIncomingNum = datas.length;
				if(!$rootScope.newReceiverMessages[gid]){
					$rootScope.newReceiverMessages[gid] = {};
				}
				$rootScope.newReceiverMessages[gid].num = newIncomingNum;
				$rootScope.newReceiverMessages[gid].lastContent = '';
				if(newIncomingNum > 0){
					$rootScope.newReceiverMessages[gid].lastContent = datas[datas.length - 1].content;
				}
				$scope.friendNewMessageReminder.num += newIncomingNum;
			},
			function (error) {
				console.log("error", error);
			}
		);
	};

	$scope.changeViewToFriends = function (val) {
		console.log("value change to friends", val);
		if(val){
			$scope.view.friendsTable = true;
			$scope.view.friendGroupsTable = false;
		}else{
			$scope.view.friendsTable = false;
			$scope.view.friendGroupsTable = true;
		}
	}
	$scope.showPart = function (value) {
		if(value == 'deletePart'){
			$scope.deletePart = true;
			$scope.requestPart = false;
		}else{
			$scope.isView = true;
			$scope.requestPart = true;
			$scope.deletePart = false;
		}
	};


	$scope.close = function () {
		$mdSidenav('right').close()
			.then(function () {
				$log.debug("close RIGHT is done");
			});
	};

	$rootScope.contactFriendChange = false;
	$scope.contact = function(value, type){
		console.log("select friend", value, type);
		FriendHandler.setter(value, type);
		$scope.close();
		console.log("contact type", type, type == 'friend');
		if($location.path() == '/contact'){
			// console.log("page is contact.html", $location.path(), "tringle init() function", $rootScope.contactIsFriend);
			$rootScope.init();
		}
		$location.url("/contact");
	};

	//------- query users -----
	$scope.queryUser = function () {
		if(!$scope.condition){
			$scope.conditionPrompt = true;
			return;
		}
		$scope.conditionPrompt = false;
		$scope.userItems = Users.query({uid : $scope.user.id,condition : $scope.condition});
		$scope.condition = null;
	};

	$scope.closeQueryPart = function () {
		$scope.userItems = [];
		$scope.conditionPrompt = true;
	}

	// ------- create new request ----- fitnesstracker/api/v1/user/:uid/requests/:rid
	$scope.createNewRequest = function(user){
		user.disabled=true;
		// console.log("add friend", user);
		$http({
			method :'POST',
			url : '/fitnesstracker/api/v1/user/'+ $scope.user.id +'/requests',
			data : user.id
		}).then(function (value) {
					console.log("return create new request to friend", value);
					sendReminderToFriend($scope.user.id, user.id);
				},
				function (error) {
					console.log("error", error);
				}
		)
	};

	var sendReminderToFriend = function (uid, fid) {
		console.log("send reminder add new friends to new group", uid, fid);
		chatSocket.send("/app/user/" + uid + "/reminder/friends/" + fid, {}, true);
	};

	var sendReminderToGroup = function (uid, gid) {
		console.log("send reminder add new group to diff users", uid, gid);
		chatSocket.send("/app/user/" + uid + "/reminder/group/" + gid, {}, true);
	};


	// ----- accept new friend ------- fitnesstracker/api/v1/user/:uid/requests/:rid
	$scope.acceptNewFriend = function(reminder){
		reminder.disabled = true;
		console.log("accept new friend request : ", reminder);
		$http({
			method : 'PUT',
			url : '/fitnesstracker/api/v1/user/'+ $scope.user.id +'/requests/' + reminder.id
		}).then(
			function (success) {
				// console.log("update friend success", success);
				// $rootScope.initialFriends();
				refreshFriendTable();
				console.log("send reminder to friends that accept new friends");
				chatSocket.send("/app/user/"+ $scope.user.id +"/accept/friends/" + reminder.fromCustomerUserId, {}, true);
			},
			function (error) {
				console.log("error", error);
			}
		)
	}

	$scope.ignoreNewFriend = function (reminder) {
		reminder.disabled = true;
	};
	$scope.search = function (friend) {
		// console.log("filter", $scope.queryParams.value ,"friend firstname ", friend.friend.firstname, friend.friend.username);
		if( !$scope.queryParams.value || (friend.friend.username.toLocaleLowerCase().indexOf($scope.queryParams.value.toLowerCase()) !== -1) ||
						(friend.friend.firstname.toLowerCase().indexOf($scope.queryParams.value.toLowerCase()) !== -1) ||
								(friend.friend.lastname.toLowerCase().indexOf($scope.queryParams.value.toLowerCase()) !== -1)){
				return true;
		}
		return false;
	};

	//--------------- selected user ------------
	$scope.selection = [];
	$scope.changeStatus = function (friend) {
			if(friend.selected){
				$scope.selection.push(friend);
			}else{
				var index = indexOfDeleteFriend(friend);
				if(index >= 0){
					$scope.selection.splice(index, 1);
				}
			}
	};

	function indexOfDeleteFriend(friend) {
		for(var i = 0; i < $scope.selection.length; i ++){
			if(friend.id == $scope.selection[i].id){
				return i;
			}
		}
		return -1;
	}

	$scope.createOneGroup = function(){
		console.log("selection friends", $scope.selection);
		var ids = [];
		if($scope.selection.length == 0){
			return;
		}else{
			ids.push($scope.user.id);
			for(var i =0; i < $scope.selection.length; i ++){
				$scope.selection[i].selected = false;
				ids.push($scope.selection[i].friend.id);
				console.log("push selection friend", $scope.selection[i].friend.id, $scope.selection[i].friend.username);
			}
		}
		console.log("get friends ids", ids);
		$scope.group.groupIds = ids;
		console.log("save group friends +++++++++", $scope.group);
		Groups.save({uid : $scope.user.id}, $scope.group,
			function (data) {
				console.log("data", data);
				// $rootScope.initialFriends();
				refreshGroupOfFriends();
				sendReminderToGroup($scope.user.id, data.id);
			},
			function (error) {
				console.log("error", error);
			}
		);

	};


	var initStompClient = function() {
		chatSocket.init('/any-socket');
		chatSocket.connect(
			function(frame) {
				chatSocket.subscribe("/user/topic/accept/friend", function (message) {
					console.log("accept new friend receive message", message, message.body);
					refreshFriendTable();
				});

				chatSocket.subscribe("/user/topic/reminder/delete", function (message) {
					console.log("delete friend reminder", message, message.body);
					var flag = message.body;
					if(flag == 'friend'){
						refreshFriendTable();
					}else{
						refreshGroupOfFriends();
					}
				});

				chatSocket.subscribe("/user/topic/reminder/friend", function (message) {
					var flag = message.body;
					console.log("reminder message", message, flag);
					if(flag == "friend"){
						refreshFriendRequest();
					}else{
						console.log("reminder from group :: ", $scope.friendNewMessageReminder.num);
						$scope.friendNewGroupReminder.num ++;
						refreshGroupOfFriends();
					}
				});

				chatSocket.subscribe("/user/topic/chat",
					function (message) {
						var receiverMsg = JSON.parse(message.body);
						console.log("receive message", receiverMsg);
						if(!$rootScope.privateMessages[receiverMsg.sender.username]){
							$rootScope.privateMessages[receiverMsg.sender.username] = [];
						}
						$rootScope.privateMessages[receiverMsg.sender.username].push(receiverMsg);
						if(receiverMsg.sender.username != $rootScope.currentChatObject){
							$scope.friendNewMessageReminder.num++;
							if($rootScope.newReceiverMessages[receiverMsg.sender.username] == null){
								$rootScope.newReceiverMessages[receiverMsg.sender.username] = {};
								$rootScope.newReceiverMessages[receiverMsg.sender.username].num = 0;
							}
							$rootScope.newReceiverMessages[receiverMsg.sender.username].num++;
							console.log("reminder message from friend", $rootScope.newReceiverMessages);
						}else{
							$http({
								url: '/fitnesstracker/api/v1/messages/user/'+$scope.user.id+'/isRead/' + receiverMsg.id,
								method: 'PUT'
							}).then(function (data) {
								console.log("update success", data);
							})
						}
					});

				chatSocket.subscribe("/user/topic/chat/group",
					function (message) {
						var receiverMsg = JSON.parse(message.body);
						console.log("group chat room message", receiverMsg);
						if(!$rootScope.privateMessages[receiverMsg.roomId]){
							$rootScope.privateMessages[receiverMsg.roomId] = [];
						}
						$rootScope.privateMessages[receiverMsg.roomId].push(receiverMsg);
						if(receiverMsg.roomId != $rootScope.currentChatObject){
							$scope.friendNewMessageReminder.num ++;
							if($rootScope.newReceiverMessages[receiverMsg.roomId] == null){
								$rootScope.newReceiverMessages[receiverMsg.roomId] = {};
								$rootScope.newReceiverMessages[receiverMsg.roomId].num = 0;
								$rootScope.newReceiverMessages[receiverMsg.roomId].lastContent = '';
							}
							$rootScope.newReceiverMessages[receiverMsg.roomId].num ++;
							$rootScope.newReceiverMessages[receiverMsg.roomId].lastContent = receiverMsg.content;
							console.log("reminder message from one group room", receiverMsg.roomId,$rootScope.newReceiverMessages);
						}else{
							ReminderMessage.delete({uid : $scope.user.id, gid : receiverMsg.roomId},
								function (datas) {
									console.log("success delete", datas);
								},
								function (error) {
									console.log("error", error);
								}
							);
						}
					}
				);
			}, function(error) {
				console.log("chat app error", error);
			}
		);
	};

	console.log("check the session valied", Session.isAuthenticated(), Session.haveUser());

	if(flag && Session.haveUser() ){
		$rootScope.initialFriends();
		initStompClient();
		flag = false;
	}else{
		Session.destroy();
		$location.url('/login');
	}
}]);

	
app.factory('AuthInterceptor', ['$q', '$location', 'Session.Service', function( $q , $location, Session ) {
	console.log("authetication interceptor factory");
   return {
      'responseError' : function( rejection  ) {
          Session.destroy();
          $location.path('/login');
          return $q.reject( rejection );
      }
   }
} ] );


app.config([ '$routeProvider', '$httpProvider',
		function($routeProvider, $httpProvider) {
			console.log("enter rooter provider cofig");
			$routeProvider.otherwise({
				redirectTo : '/login'
			});
		}
]);


