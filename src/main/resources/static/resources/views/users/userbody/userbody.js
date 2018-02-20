var userbody = angular.module("FitnessTracker.userBody",['ngRoute','ngMaterial', 'Session']);
userbody.config(['$routeProvider', function($routeProvider){
    $routeProvider.when('/userbody', {
        templateUrl: "/resources/views/users/userbody/userbody.html",
        controller: "FitnessTracker.userbody.controller"
    })
}]);

userbody.controller("FitnessTracker.userbody.controller", ['$scope', '$rootScope', '$http', '$location', 'Session.Service', function($scope, $rootScope, $http, $location, Session) {
    var user = Session.user();
    $scope.checkDevice = function(val){
        return user.device == val;
    }

}]);