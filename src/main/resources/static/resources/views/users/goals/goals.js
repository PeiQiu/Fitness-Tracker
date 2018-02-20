var goal = angular.module('FitnessTracker.Goals',['ngRoute', 'ngMaterial', 'ngMessages', 'ui.bootstrap', 'Session']);
goal.config(['$routeProvider', '$mdIconProvider', function($routeProvider, $mdIconProvider){
    $routeProvider.when('/goals', {
        templateUrl: '/resources/views/users/goals/goals.html',
        controller: 'FitnessTracker.goals.controller'
    });
    
}]);

goal.filter('filterSleepTime', function () {
    return function (data) {
        if(typeof (data) === 'undefined'){
            return 0;
        }
        return data/3600;
    }
});

goal.filter('filterBodyWeight', function () {
    return function (data, key) {
        if(typeof (data) === 'undefined' || typeof (key) === 'undefined'){
            console.log("error", data, key);
            return data;
        }
        if(key == 'bls'){
            return(data*2.2046).toFixed(2);
        }
        return data;
    }
});

goal.controller('FitnessTracker.goals.controller', ['$scope', '$resource', '$rootScope', '$http', '$location', 'Session.Service', function($scope, $resource, $rootScope, $http, $location, Session){
    var user = Session.user();
    // if(Session.haveUser()){
    $scope.user = Session.user();
    // }else{
    //     Session.destroy();
    //     $location.path("/login");
    // }
    $scope.filterData = {
        sleep : 0,
        weight : 0
    };
    $scope.selectedItem = 'kg';
    $scope.deadLineDate = new Date();
    var GoalsData = $resource('/fitnesstracker/api/v1/user/:uid/goals', {}, {query: { method : 'GET', params: {start: new Date(), end:new Date()}}});
    $scope.goals = GoalsData.query({uid : user.id, device : user.device});
    $scope.$watch('goals.sleep', function (newValue) {
        $scope.filterData.sleep = newValue / 3600;
    });

    $scope.$watch('goals.body_weight', function (newValue) {
        if($scope.selectedItem == 'bls'){
            $scope.filterData.weight =  (data*2.2046).toFixed(2);
        }else{
            $scope.filterData.weight = newValue;
        }
    });

    $scope.$watch('selectedItem', function (newValue) {
        if(newValue == 'bls'){
            $scope.filterData.weight =  parseInt(($scope.goals.body_weight*2.2046).toFixed(2));
        }else{
            $scope.filterData.weight = $scope.goals.body_weight;
        }
    });


    $scope.filterWeightData = function (data, key) {
        if(key == 'bls'){
            return(data*2.2046).toFixed(2);
        }
        return data;
    };
    $scope.isDisabled = true;
    $scope.readonly = true;
    $scope.checkDevice = function(val){
        return user.device == val;
    };
}]);