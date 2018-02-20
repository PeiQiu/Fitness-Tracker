var tmod = angular.module('FitnessTracker.Login', ['ngRoute']);

tmod.config(['$routeProvider', function($routeProvider) {
   var config = {
    templateUrl: '/resources/views/shared/login/login.html',
    controller: 'FitnessTracker.Login.Controller'
   };
   
   $routeProvider.when('/', config).when( '/login', config );
}]);

tmod.controller('FitnessTracker.Login.Controller', ['$scope', '$rootScope', '$http', '$location', 'Session.Service', function($scope, $rootScope, $http, $location, Session) {
   // $rootScope.setBackground = function(){
   //    return {
   //       'background-image':'url(https://cdn.yourstory.com/wp-content/uploads/2016/04/06-040026-how_to_track_your_fitness_progress-1.jpg)'
   //    }
   // }
   $scope.username = '';
   $scope.password = '';
   $scope.hasError = false;

   $scope.isLoggedIn = Session.isAuthenticated;
    if( Session.haveUser() && Session.isAuthenticated()) {
        var user = Session.user();
        console.log("turn to login page", Session.haveUser());
        // if (user.id) {
        //     $rootScope.initialFriends();
        // }
        if (user.device) {
            $location.url('/summary');
        } else {
            $location.url('/selectdevice');
        }
    }
    // }else{
    //     console.log("Session have user --- ", user, user.username, user.password);
    //     $scope.username = user.username;
    //     $scope.password = user.password;
    //     $scope.login();
    // }


   $scope.signup = function () {
      $location.url($location.path());
      $location.url('/register');
   }
   
   $scope.login = function() {
      var req = {
         method : 'POST',
         url : '/login',
         headers : {
            'Content-Type' : 'application/x-www-form-urlencoded'
         },
         data : 'username=' + $scope.username + '&password=' + $scope.password
      };

      $http( req ).then(
         function( response ) {
             $scope.hasError = false;
            console.log("login user data", response.data)
        	 Session.create( response.data );
             var user = Session.user();
            if(user.id){
               $rootScope.initialFriends();
            }
                if(user.device){
                   $location.url('/summary');
                }else{
                   $location.url('/selectdevice');
                }
         },
         
         function( error ) {
        	 $scope.hasError = true;
         }
      );
   }
}]);


