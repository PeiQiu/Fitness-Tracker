/**
 * Created by peiqiutian on 13/09/2017.
 */
var register = angular.module('FitnessTracker.Register', ['ngRoute']);

register.config(['$routeProvider', function($routeProvider){
    var config = {
        templateUrl : '/resources/views/shared/register/register.html',
        controller :'FitnessTracker.Register.Controller'
    }
    $routeProvider.when('/register', config);
}]);

register.controller('FitnessTracker.Register.Controller',['$scope', '$rootScope', '$http', '$location', 'Session.Service',function($scope, $rootScope, $http, $location, Session){
    // $rootScope.setBackground = function(){
    //     return {
    //         'background-image':'url(https://static1.squarespace.com/static/586eb5ef5016e16cd309d987/t/59aeec69f43b55d3c374c456/1504636037883/background.png)'
    //     }
    // }
    $scope.hasError = false;
    $scope.username = '';
    $scope.firstname = '';
    $scope.lastname = '';
    $scope.password = '';
    $scope.email = '';
    $scope.back = function () {
        $location.url($location.path());
        $location.url('/login');
    }

    $scope.register = function () {
        // console.log('username=' + $scope.username + '&password=' + $scope.password + '&name=' + $scope.firstname, $scope.lastname + "&email=" + $scope.email);
        var req = {
            method : 'POST',
            url : '/signup',
            headers : {
                'Content-Type' : 'application/x-www-form-urlencoded'
            },
            data : 'username=' + $scope.username + '&password=' + $scope.password + '&firstname=' + $scope.firstname + '&lastname=' + $scope.lastname + "&email=" + $scope.email
        };
        $http(req).then(
            function (response) {
                console.log(response);
                if(response.data == 'OK'){
                    alert("Register Success!");
                    $location.url($location.path());
                    $location.url('/login');
                }else{
                    $scope.hasError = true;
                    $scope.message = response.data;
                }
            },
            function (error) {
                console.log(error);
                $scope.hasError = true;
                $scope.message = "Register not Success!!";
            }
        )

    }


}]);

