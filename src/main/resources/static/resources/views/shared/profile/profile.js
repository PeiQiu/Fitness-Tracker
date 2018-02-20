var profile = angular.module('FitnessTracker.Profile', ['ngRoute', 'ngResource', 'ui.bootstrap', 'Session']);

profile.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/profile', {
        templateUrl: '/resources/views/shared/profile/profile.html',
        controller: 'FitnessTracker.Profile.Controller'
    });

}]);

profile.controller('FitnessTracker.Profile.Controller', ['$scope', '$rootScope', '$resource', '$uibModal', 'Session.Service', 'fileReader', '$http', '$location', function ($scope, $rootScope, $resource, $modal, Session, fileReader, $http, $location) {

    $scope.user = Session.user();

    $scope.updateProfile = function () {
        if ($scope.userUpdate.password || $scope.userUpdate.password2) {
            if (!($scope.userUpdate.password && $scope.userUpdate.password2 && $scope.userUpdate.password2 == $scope.userUpdate.password)) {
                alert('Password mismatch.');
                return;
            }
        }

        $http({
            method: 'PUT',
            url: '/api/v1/profile/' + $scope.user.id,
            data: $scope.userUpdate,
            headers: {
                'Content-Type': 'application/json'
            },
        }).then(function successCallback(req) {
            Session.create(req.data);
            $scope.user = Session.user();
            // console.log($scope.user);
        }, function errorCallback(response) {
            
        });
    }



    $scope.img_src = '/fitness/api/v1/user/'+ $scope.user.id +'/avatars';//"/api/v1/profile/avatars/" + Session.user().id;
    $scope.loadImage = function (img) {
        fileReader.readAsDataUrl(img[0], $scope)
            .then(function (result) {
                $scope.img_src = result;
                //uploader.uploadAll();
                $http({
                    method: 'POST',
                    url: '/fitness/api/v1/user/'+ $scope.user.id +'/avatars',
                    //'/api/v1/profile/avatars/' + $scope.user.id,
                    headers: {'Content-Type': undefined},
                    data: {
                        file: img[0],
                    },
                    transformRequest: (data) => {
                        let formData = new FormData();
                        angular.forEach(data, function (value, key) {
                            formData.append(key, value);
                        });
                        return formData;
                    }
                }).success(() => {
                    // alert('Upload Successfully');

                }).error(() => {
                    alert('Fail to upload, please upload again');
                });

            });
    };
}]);

/*
*
* image.service('LoadImage.Service', ['$resource', function($resource){
 var image = $resource('/fitness/api/v1/user/:uid/avatars', {}, {query : {method : 'GET'}})
 }]);
*
* */
