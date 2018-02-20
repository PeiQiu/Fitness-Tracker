users = angular.module('Headhunter.Users', ['ngRoute', 'ngResource', 'Session']);

users.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/admin/users', {
    templateUrl: 'resources/views/admin/users/users.html',
    controller: 'Headhunter.Users.Controller'
  });
}]);

users.controller('Headhunter.Users.Controller', ['$scope', '$resource', 'Session.Service', function($scope, $resource, Session) {
   var Users = $resource('api/v1/admin/users/:uid', {});   
   $scope.users = Users.query( );
   console.log("all user", $scope.users);
   $scope.hasRole = function( user, role ) {
	   for( var i = 0; i < user.roles.length; i++) {
		   if( user.roles[i].name === role ) return true;
	   }
	   return false;
   }

}]);

// https://www.federalregister.gov/api/v1/documents.json?per_page=35&order=relevance&%26conditions%5Bnear%5D%5Blocation%5D=54601&conditions%5Btype%5D=PRORULE&conditions%5Bsignificant%5D=1

// https://www.federalregister.gov/api/v1/documents.json?per_page=35&order=relevance&conditions%5Bsignificant%5D=1&conditions%5Bnear%5D%5Blocation%5D=54601
