var createSharings = angular.module('FitnessTracker.createShare', ['ngRoute','ngMaterial', 'ngResource']);

createSharings.config(['$routeProvider', function($routeProvider){
    $routeProvider.when("/createSharings", {
        templateUrl: '/resources/views/applications/sharings/createSharings.html',
        controller :'FitnessTracker.createShare.controller'
    })
}]);

createSharings.filter('checkNum', function () {
    return function (n) {
        if(n > 100){
            return (n/100.0).toFixed(1) + 'k';
        }else{
            return (n/1.0).toFixed(0);
        }
    }
});


createSharings.filter('checkUnit', function () {
    return function (n, user) {
        if(typeof (n) === 'undefined'){
            return 0;
        }
        //console.log("filter device", n, user.device);
        if(user.device == 'misfit'){
            return (n *1.609).toFixed(1) + 'km';
        }else{
            return (n / 1000.0).toFixed(1) + 'km';
        }
    }
});
createSharings.filter('getEndTime', function () {
    return function (datenums, duration) {
        if(typeof (datenums) === 'undefined'){
            return 0;
        }
        console.log("filter time", datenums, duration);
        var date = new Date(datenums + duration * 1000);
        return date.toTimeString().split(' ')[0];
    }
});

createSharings.controller('FitnessTracker.createShare.controller',['$resource', '$scope', '$rootScope', '$http', '$location', 'Session.Service', function($resource, $scope, $rootScope, $http, $location, Session) {
    var Sessions = $resource('/fitnesstracker/api/v1/session/:uid', {}, {query : {method : 'GET'}});
    var SessionSummary = $resource('/fitnesstracker/api/v1/summary/:uid', {}, {query : {method : 'GET'}});
    var PostActiveData = $resource('/fitnesstracker/api/v1/user/:uid/activedata/:aid', {}, {qury : {method: 'GET'}, post : {method : 'POST'}})
    var CreateNewSharing = $resource('/fitnesstracker/api/v1/user/:uid/sharings/:sid', {}, {post : {method : 'POST'}});
    var flag = true;
    $scope.close = function(){
        $location.url("/sharings")
    };
    $scope.showTimeItemlist = true;
    $scope.user = Session.user(); // selectDate
    // if(Session.haveUser()){
    //     $scope.user = Session.user();
    // }else{
    //     Session.destroy();
    //     $location.path("/login");
    // }
    $scope.maxDate = new Date();
    $scope.selectSharingDate = new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate());
    $scope.activityNote = false;
    $scope.signatrue = {
        noteSign : false
    }
    //------------------

    $scope.searchSharingData = function(value){
        console.log("selected Date", $scope.selectSharingDate);
        //------------get select time ------
        $scope.selectSharingDate = new Date($scope.selectSharingDate.setDate($scope.selectSharingDate.getDate() + value));
        console.log("after add value", value, $scope.selectSharingDate);
        //------------get sessions data------------
        Sessions.query({uid : $scope.user.id, start: $scope.selectSharingDate, end: $scope.selectSharingDate, device : $scope.user.device},
            function(data){
                console.log("sessions", data, data.sessions.length);
                if(data.sessions.length > 0){
                    $scope.signatrue.noteSign = false;
                }else{
                    $scope.signatrue.noteSign = true;
                }
                console.log("$scope.signatrue", $scope.signatrue.noteSign);
                $scope.todos = data.sessions;
                $scope.documentslist = $scope.todos;
                $scope.DocumentsItems = data.sessions.length;
                $scope.currentDocumentsPage = 1;
                $scope.maxDocumentsSize = 5;
                $scope.itemsDocumentsPerPage = 7;
                $scope.showTimeItemlist = ($scope.todos.length > 0);
                console.log("showTimeItemList", $scope.showTimeItemlist);
            },
            function (error) {
                console.log("error", error);
            }
        );
        //------------get one day summary-------------
        $scope.summaryData = SessionSummary.query({uid : $scope.user.id, start: $scope.selectSharingDate, end: $scope.selectSharingDate, device : $scope.user.device});
        console.log("summary data", $scope.summaryData);
    }


    $scope.ShowButton = function () {
        return $scope.selectSharingDate == undefined;
    };


    $scope.chooseActiveType = function(value){
        //console.log("user", value);
        $scope.selectValue = value;
        var title = generateTitile(value);
        $scope.title = title;
        // var culomnData = [];
        // generateColumnData(value, culomnData);
        // console.log("culomn data", culomnData);
        // drawColumnChart(value ,culomnData);
    };
    function generateTitile(value) {
        var title = 'summary in ' + $scope.selectSharingDate.yyyy_mm_dd();
        if(value.activityType != undefined){
            title = value.activityType + ' from ' + new Date(value.startTime).toTimeString().split(' ')[0] + ' to ' + new Date(value.startTime + value.duration * 1000).toTimeString().split(' ')[0];
        }
        return title;
    }
    function drawColumnChart( value, culomnData) {
        var title = 'summary in ' + $scope.selectSharingDate.yyyy_mm_dd();
        if(value.activityType != undefined){
            title = value.activityType + ' from ' + new Date(value.startTime).toTimeString().split(' ')[0] + ' to ' + new Date(value.startTime + value.duration * 1000).toTimeString().split(' ')[0];
        }
        google.charts.load("current", {packages: ["corechart"]});
        google.charts.setOnLoadCallback(drawStuff);
        function drawStuff() {
            var data = new google.visualization.DataTable();
            data.addColumn('string', 'type');
            data.addColumn('number', 'value');
            data.addColumn({type:'string', role:'style'});
            data.addRows(culomnData);
            var options = {
                title : title,
                width: 500,
                height : 300,
                bar : {groupWidth:'15%'},
                legend: { position: "none" },
                vAxis : {
                    minValue : 0,
                    viewWindow: { min : 0 }
                }
            }
            var chart = new google.visualization.ColumnChart(document.getElementById('myColumnChart'));
            chart.draw(data, options);
        }
    }
    
    function generateColumnData(value, array) {
        console.log("generate culomn value", value);
        var keys = ['steps' , 'calories', 'distance'];
        var colors = ['#F44336', '#E91E63', '#9C27B0'];
        for(var k in keys){
            var item = [];
            item.push(keys[k]);
            if(keys[k] == 'distance'){
                if($scope.user.device == 'misfit'){
                    item.push( { v : value[keys[k]] * 1609, f : (value[keys[k]] * 1.609).toFixed(2) + 'km'});
                }else {
                    item.push( { v : value[keys[k]], f : (value[keys[k]] / 1000.0).toFixed(2) + 'km'});
                }
            }else {
                item.push(value[keys[k]]);
            }
            item.push(colors[k]);
            array.push(item);
        }
    }

    if(flag){
        $scope.searchSharingData(0);
        flag = false;
    }
    Date.prototype.yyyy_mm_dd = function() {
        var mm = this.getMonth() + 1; // getMonth() is zero-based
        var dd = this.getDate();

        return [this.getFullYear(),
            (mm>9 ? '' : '0') + mm,
            (dd>9 ? '' : '0') + dd
        ].join('-');
    };

    $scope.postData = function(selectValue){
        console.log("post value", selectValue);
        if(selectValue.id != null){
            delete selectValue.id;
            selectValue.type = true;
        }else{
            selectValue.activityType = 'summary of ' + $scope.selectSharingDate.yyyy_mm_dd();
            selectValue.startTime = $scope.selectSharingDate.getTime();
            selectValue.duration = 86400;
            selectValue.type = false;
        }
        if($scope.user.device == 'misfit'){
            selectValue.distance = (selectValue.distance * 1.609).toFixed(2);
        }else {
            selectValue.distance = (selectValue.distance / 1000.0).toFixed(2);
        }
        console.log("post value after edit", selectValue);
        PostActiveData.post({uid : $scope.user.id}, selectValue,
            function (data) {
                console.log("data", data, data.id);
                createSharingData(data.id);
            },
            function (error) {
                console.log(error)
            }
        );
    }
    
    function createSharingData(activeId) {
        var sharingItem = {};
        sharingItem.activeDataId = activeId;
        sharingItem.content = $scope.commentContent;
        sharingItem.sharingDate = new Date().getTime();
        sharingItem.autherId = $scope.user.id;
        sharingItem.state = 1;
        CreateNewSharing.post({uid : $scope.user.id}, sharingItem,
            function(data){
                console.log("create sharing data", data);
                $scope.commentContent = '';
                $('#myModal').modal('hide');
                // alert("create new Sharing success!!");
            },
            function (error) {
                console.log("error", error);
                alert("create new Sharing fails!!");
            }
        )

    }
}]);