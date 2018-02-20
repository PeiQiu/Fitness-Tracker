var comments = angular.module('FitnessTracker.Application.Comments', ['ngRoute']);

comments.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/comments',
        {
            templateUrl: '/resources/views/users/comments/comments.html',
            controller: 'FitnessTracker.comments.controller'
        }
    )
}]);

comments.controller('FitnessTracker.comments.controller', ['$scope', '$rootScope', '$http', '$location', 'Session.Service',function($scope, $rootScope, $http, $location, Session){

}]);
