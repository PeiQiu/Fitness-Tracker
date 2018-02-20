var selectdevice = angular.module('FitnessTracker.selectDevice', ['ngRoute', 'ngMaterial', 'Session', 'ngResource']);

selectdevice.config(['$routeProvider', function ($rootProvider) {
    $rootProvider.when('/selectdevice',{
        templateUrl: '/resources/views/shared/selectdevice/selectdevice.html',
        controller: 'FitnessTracker.selectDevice.controller'
    }
    )

}]);

selectdevice.controller('FitnessTracker.selectDevice.controller', ['$resource', '$window', '$scope', '$routeParams', '$rootScope', '$http', '$location', 'Session.Service',function($resource, $window, $scope, $routeParams, $rootScope, $http, $location, Session){
    $scope.imagePathJawbone = 'https://images-na.ssl-images-amazon.com/images/G/01/aplus/detail-page/Jawbone-logo.jpg';
    $scope.imagePathMisfit = 'https://images-na.ssl-images-amazon.com/images/G/01/vince/boost/detailpages/misfitshinee1._V289285107_.jpg';
    // var UserConnect = $resource('/fitnesstracker/api/v1/user/:uid/device/user/:uid', {}, {update : {method : 'PUT'}});
    // var UserDisConnect = $resource('/fitnesstracker/api/v1/device/disconnect/:uid', {}, {query : {method : 'GET'}});
    var MisfitDeviceInfo = $resource('/fitnesstracker/api/v1/user/:uid/misfit/device', {}, {query :{method : 'GET'}});
    // $scope.user = Session.user();
    if(Session.isAuthenticated()){
        $scope.user = Session.user();
    }else{
        Session.destroy();
        $location.path("/login");
    }
    $scope.device = {};
    if($scope.user.device){
        getMisfitInfo();
    }
    //------------------------------------------------------------connectJawbone
    $scope.connectJawbone = function(){
        var req = {
            method : 'GET',
            url : '/fitnesstracker/api/v1/user/'+ $scope.user.id +'/jawbone',
            };
        $http(req).then(
            function (response) {
                window.location=response.data;
            },
            function (error) {
                console.log(error);
            }
        )
    };
    //------------------------------------------------------------connectMisfit
    $scope.connectMisfit = function (val) {
        $scope.device.name = val;
        var req = {
            method : 'GET',
            url : '/fitnesstracker/api/v1/user/'+ $scope.user.id + '/misfit',
        };
        $http(req).then(
            function(response){
                window.location=response.data;
            },
            function (error) {
                console.log(error);
            }
        )
    };

    //----------------------------------------------------requestForMisfit
    function getMisfitInfo() {
        if($scope.user.device == 'misfit') {
            $scope.device = MisfitDeviceInfo.query({uid: $scope.user.id});
        }
    }

    $scope.disconnect = function(){
        $scope.device = {};
        //var deletUser = UserDisConnect.query({uid : $scope.user.id});
        var req = {
            method : 'DELETE',
            url: '/fitnesstracker/api/v1/user/' + $scope.user.id + '/device'
        }
        $http(req).then(
            function (response) {
                // console.log("delete", response.data);
                Session.create(response.data);
                $scope.user = Session.user();
            },
            function (error) {
                console.log(error);
            }
        )
    };

    $scope.haveDevice = function () {
        if($scope.user.device==null){
            return false;
        }else{
            return true;
        }
    }

    //----------------------------------------------------requestForwardBack
    if( $routeParams['success'] ) {
        // console.log($routeParams['device'], $routeParams['accessCode']);
        // var newuser = UserConnect.update({uid : $scope.user.id}, {device : $routeParams['device'], accessCode : $routeParams['accessCode']});
        var req = {
            method:'PUT',
            url: '/fitnesstracker/api/v1/user/'+$scope.user.id +'/device',
            data: {device : $routeParams['device'], accessCode : $routeParams['accessCode']}
        }
        $http(req).then(
            function (response) {
                // console.log("newuser",response.data)
                Session.create(response.data);
                $scope.user = Session.user();
                getMisfitInfo()
            },
            function (error) {
                console.log(error)
            }
        )
    }

    if($routeParams['delete']) {
        // console.log($routeParams['delete'], $routeParams['id']);
        if($routeParams['id'] != $scope.user.id){
            return;
        }
        $scope.disconnect();
    }

}]);