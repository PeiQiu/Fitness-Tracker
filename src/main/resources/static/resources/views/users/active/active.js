
var active = angular.module("FitnessTracker.Active", ['ngRoute','ngMaterial', 'ngResource']);

active.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when("/active", {
        templateUrl: '/resources/views/users/active/active.html',
        controller: 'FitnessTracker.active.controller'
    })
}]);


active.filter('checkUnit', function () {
    return function (n, user) {
        if(typeof (n) === 'undefined'){
            return 0;
        }
        if(user.device == 'misfit'){
            return (n *1.609).toFixed(2);
        }else{
            return (n / 1000.0).toFixed(2);
        }
    }
});



active.controller('FitnessTracker.active.controller',['$resource', '$scope', '$rootScope', '$http', '$location', 'Session.Service', function($resource, $scope, $rootScope, $http, $location, Session) {
    var CurrentSummary = $resource('/fitnesstracker/api/v1/user/:uid/summary', {}, {query : {method:'GET'}});
    var flag = true;
    $scope.showBarChart = false;
    $scope.user = Session.user();
    // if(Session.haveUser()){
    //     $scope.user = Session.user();
    // }else{
    //     Session.destroy();
    //     $location.path("/login");
    // }
    $scope.maxDate = new Date();
    $scope.specificTime = new Date();
    $scope.activeSessions = [];
    $scope.sleepTimeType = {'awake' : 0, 'sleep' : 0, 'deep sleep': 0};
    $scope.info = {
        awakeTime: '0',
        sleepTime:'0',
        remsleeptime:'0',
        deepsleepTime:'0'
    };

    $scope.ShowButton = function () {
        return $scope.specificTime == undefined;
    };

    $scope.search = function(value){

        var finishSessions = false;
        var activeSessions = [];
        var activeLastTime = $scope.specificTime;
        var finishSleeps = false;
        var sleepsSession = [];
        var sleepLastTime = $scope.specificTime;
        $scope.specificTime = new Date($scope.specificTime.setDate($scope.specificTime.getDate() + value));
        //--------------get session summary---------------
        $scope.sessions = CurrentSummary.query({uid : $scope.user.id, start: $scope.specificTime, end: $scope.specificTime, device : $scope.user.device});
        //----------get fitness sessions---------------------------
        $http({
            method:'GET',
            url:'/fitnesstracker/api/v1/user/' + $scope.user.id + '/session',
            params:{'start': $scope.specificTime, 'end': $scope.specificTime, 'device' : $scope.user.device}
        }).then(
            function (response) {
                var responseData = response.data;
                $scope.activeSessionsData = responseData.sessions;
                activeLastTime = generateData(responseData.sessions, activeSessions);
                finishSessions = true;
                doNextStep()
            }
        );

        //-----------------get fitness sleep data --------------------
        $http({
            method:'GET',
            url:'/fitnesstracker/api/v1/user/' + $scope.user.id + '/sleeps',
            params:{'start': $scope.specificTime, 'end': $scope.specificTime, 'device' : $scope.user.device}
        }).then(
            function (response) {
                $scope.sleepTimeType = {'awake' : 0, 'sleep' : 0, 'deep sleep': 0};
                $scope.info = {
                    awakeTime:'0',
                    sleepTime:'0',
                    remsleeptime:'0',
                    deepsleepTime:'0'
                };
                var responseData = response.data;
                sleepLastTime = makeSleepDatas(responseData.sleeps, sleepsSession);
                finishSleeps = true;
                doNextStep()
            },
            function(error){
                console.log(error);
            }
        );
        function doNextStep() {
            if(finishSessions && finishSleeps){
                var timeLineDatas = [];
                var endTime = Math.max(sleepLastTime, activeLastTime);
                combineActiveAndSessionsData(timeLineDatas, sleepsSession, activeSessions, endTime);
                // --------- make the 24h range of timeline --------
                var activeSessionsData = $scope.activeSessionsData;
                drawnTimeLineChart( timeLineDatas );//activeSessionsData
            }
        }

    };

    function combineActiveAndSessionsData(timeLineDatas, sleepsSession, activeSessions, endtime){
        drawnBarChart([[]], 0);
        combineTwoArrays(timeLineDatas, sleepsSession, activeSessions);
    }

    function combineTwoArrays(timeLineDatas, sleepSessions, activeSessions){
        for(var i =0; i < sleepSessions.length; i ++){
            timeLineDatas.push(sleepSessions[i]);
        }
        for(var i =0; i < activeSessions.length; i ++){
            timeLineDatas.push(activeSessions[i]);
        }
        timeLineDatas.sort(function(a, b){
            return a.start - b.start;
        });
        var starteTime = new Date($scope.specificTime.getFullYear(), $scope.specificTime.getMonth(), $scope.specificTime.getDate(), 0);
        var endTime = new Date($scope.specificTime.getFullYear(), $scope.specificTime.getMonth(), $scope.specificTime.getDate(), 24);
        var currentTime = new Date();
        if(endTime > currentTime){
            endTime = currentTime;
        }
        var len = timeLineDatas.length;
        for(var i =0; i < len; i ++){
            if(starteTime < timeLineDatas[i].start){
                var content = makeOneRangeContent(4, 'not active', starteTime.getTime());
                var item = {
                    'start': starteTime,
                    'end': timeLineDatas[i].start,
                    'content': content
                };
                timeLineDatas.push(item);
            }
            starteTime = timeLineDatas[i].end;
        }
        if(starteTime < endTime){
            var content = makeOneRangeContent(4, 'not active', starteTime.getTime());
            var item = {
                'start': starteTime,
                'end': endTime,
                'content': content
            };
            timeLineDatas.push(item);
        }
        timeLineDatas.sort(function(a, b){
            return a.start - b.start;
        });
    }


    function drawnTimeLineChart( timeLineDatas ){
        $scope.info.awakeTime = timeZoneToString($scope.sleepTimeType['awake'] / 1000);
        $scope.info.sleepTime = timeZoneToString($scope.sleepTimeType['sleep'] / 1000);
        $scope.info.deepsleepTime = timeZoneToString($scope.sleepTimeType['deep sleep'] / 1000);
        var defaultTime = $scope.specificTime;
        var options = {
            "width":  "100%",
            "height": "260px",
            "min": new Date(defaultTime.getFullYear(), defaultTime.getMonth(), defaultTime.getDate(), 0),// lower limit of visible range
            "max": new Date(defaultTime.getFullYear(), defaultTime.getMonth(), defaultTime.getDate(), 24),// upper limit of visible range
            "zoomMin": 1000 * 60 * 60 * 1,             // one day in milliseconds
            "zoomMax": 1000 * 60 * 60 * 24,     // about one day in milliseconds
            "style": "box",
            "stackEvents": false
        };

        timeline = new links.Timeline(document.getElementById('mytimeline'), options);
        timeline.draw( timeLineDatas );
        links.events.addListener(timeline, 'select', onselect);
        function onselect(){
            var sel = timeline.getSelection();
            if (sel.length) {
                if (sel[0].row != undefined) {
                    var row = sel[0].row;
                    var temp = timeLineDatas[row];
                    var id = $.parseHTML(temp['content'])[0].id;
                    drawnBarChart($scope.activeSessionsData, id);
                }
            }
        }
    }


    function makeSleepDatas(array, sleepDatas){
        var endDate = $scope.specificTime;
        for(k =0; k < array.length; k ++){
            endDate = makeOneRows(array[k], sleepDatas);
        }
        return endDate;
    }

    function makeOneRows(val, ans){
        var sleepId = val.id;
        var sleepStart = val.startTime;
        var sleepEnd = val.startTime + val.duration * 1000;
        var sleepDetais = val.sleepDetails;
        if(sleepDetais.length > 0){
            for(i =0; i < sleepDetais.length; i ++){
                var currentTime = new Date(sleepDetais[i].datetime);
                var currentEndTime = new Date(sleepEnd);
                var typeSleep = chooseSleepType(sleepDetais[i].value);
                if((i + 1) == sleepDetais.length){
                    currentEndTime = new Date(sleepEnd);
                }else{
                    currentEndTime = new Date(sleepDetais[i + 1].datetime);
                }
                $scope.sleepTimeType[typeSleep] += (currentEndTime - currentTime);
                var contentInfo = makeOneRangeContent(sleepDetais[i].value, "", sleepId + i);
                var item = {
                    'start': currentTime,
                    'end': currentEndTime,
                    'content': contentInfo
                };
                ans.push(item);
            }
        }else{
            $scope.sleepTimeType['sleep'] += val.duration * 1000;
            var content2 = makeOneRangeContent(2, '', sleepId);
            var item = {
                'start': new Date(sleepStart),
                'end': new Date(sleepEnd),
                'content': content2
            };
            ans.push(item);
        }
        var endDate = new Date(sleepEnd);
        return endDate;
    }

    function makeOneRangeContent(num, type, id) {
        var maxNum = 3;
        var color = '#D32F2F';
        switch (num){
            case 1:
                color = '#B39DDB';
                break;
            case 2:
                color = '#5E35B1';
                break;
            case 3:
                color = '#311B92';
                break;
            case 4:
                color = '#F3E5F5';
                break;
            default:
                break;
        }
        var height = 155;//Math.round(num / maxNum * 70 + 30); // a percentage, with a lower bound on 20%
        var style = 'height:' + height + 'px;' +
            'background-color: ' + color + ';' +
            'border-top: 2px solid ' + color + ';';
        var actual = '<div class="bar" id = "'+ id +'" style="' + style + '" ' + '>' + type + '</div>';
        return actual;
    }


    function chooseSleepType(val) {
        var text = '';
        switch (val){
            case 1:
                text = 'awake';
                break;
            case 2:
                text = 'light sleep';
                break;
            case 3:
                text = 'deep sleep';
                break;
        }
        return text;
    }

    function generateActiveDate(activeData){
        var keys = ['steps', 'distance' , 'calories'];
        var ans = [];
        for(var k in keys){
            var temp = [];
            if(keys[k] == 'distance'){
                temp.push(keys[k]);
                if($scope.user.device == 'jawbone'){
                    temp.push({v : activeData[keys[k]], f : (activeData[keys[k]]/1000.0).toFixed(2) + 'km'});
                }else{
                    //activetemp.push({ v : (array[i].distance*1609), f : (array[i].distance*1.609).toFixed(2) + 'km'});
                    temp.push({v : (activeData[keys[k]] * 1609), f : (activeData[keys[k]]*1.609).toFixed(2) + 'km'});
                }
            }else{
                temp.push(keys[k]);
                temp.push(activeData[keys[k]]);
            }

            ans.push(temp);
        }
        return ans;
    }

    function drawnBarChart(sessions, id){
        google.charts.load('current', {'packages':['bar']});
        var dataRows = [];
        var title = "Please Select Active Type in " + $scope.specificTime.yyyy_mm_dd();
        for(var i =0; i < sessions.length; i ++){
            if(sessions[i].id == id){
                dataRows = generateActiveDate(sessions[i]);
                title = sessions[i].activityType + ' from ' + new Date(sessions[i].startTime).toTimeString().split(' ')[0] + ' to ' + new Date(sessions[i].startTime + sessions[i].duration * 1000).toTimeString().split(' ')[0];
            }
        }
        if(dataRows.length == 0){
            dataRows = [['step', null], ['distance' , null], ['calories', null]];
        }
        // console.log("data rows", dataRows, 'title', title);
        google.charts.setOnLoadCallback(drawStuff);
        function drawStuff() {
            var dataTable = new google.visualization.DataTable();
            dataTable.addColumn('string', '');
            dataTable.addColumn('number', 'value');
            dataTable.addRows(dataRows);
            var options = {
                chart: {
                    title : title,
                },
                titleTextStyle : {
                    color: 'black',
                    fontSize: 18,
                    fontName: 'Arial',
                    bold: true,
                    italic: false
                },
                bars: 'horizontal',
                bar: { groupWidth: "90%" },
                legend: { position: "none" },
                vAxis: {
                    textStyle: {
                        color: 'black',
                        fontSize: 12,
                        fontName: 'Arial',
                        bold: true,
                        italic: false
                    }
                },
                hAxis: {
                    textStyle: {
                        color: 'black',
                        fontSize: 12,
                        fontName: 'Arial',
                        bold: true,
                        italic: false
                    }
                }
            };
            var chart = new google.charts.Bar(document.getElementById('dual_div'));
            chart.draw(dataTable, options);
        };
    }

    function generateData(val, activeSessions) {
        $scope.timeDuration = 0;
        var endTime = $scope.specificTime;
        for(var i = 0; i < val.length; i ++){
            var startTime = new Date(val[i].startTime);
            endTime = new Date(val[i].startTime + val[i].duration * 1000);
            var activeType = val[i].activityType;
            var activeId = val[i].id;
            $scope.timeDuration += val[i].duration;
            var activeContent = makeOneRangeContent( 5, activeType, activeId);
            var item = {
                'start': startTime,
                'end': endTime,
                'content': activeContent
            };
            activeSessions.push(item);
        }
        $scope.sessionsDuration = timeZoneToString($scope.timeDuration);
        return endTime;
    }

    timeZoneToString=(s)=>{
        f=Math.floor;
        g=(n)=>('00'+n).slice(-2);
        return f(s/3600)+'h'+g(f(s/60)%60)+'min'
    };

    $scope.checkDateTimeLine = function () {
        return $scope.specificTime.getDate() == new Date().getDate();
    };

    Date.prototype.yyyy_mm_dd = function() {
        var mm = this.getMonth() + 1; // getMonth() is zero-based
        var dd = this.getDate();

        return [this.getFullYear(),
            (mm>9 ? '' : '0') + mm,
            (dd>9 ? '' : '0') + dd
        ].join('-');
    };


    if(flag && Session.isAuthenticated()){
        $scope.search(0);
        flag = false;
    }

}]);