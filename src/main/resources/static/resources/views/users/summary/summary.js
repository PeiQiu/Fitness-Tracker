
var summary = angular.module('FitnessTracker.Active.summary', ['ngRoute','ngMaterial', 'ngResource'])

summary.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/summary', {
        templateUrl: '/resources/views/users/summary/summary.html',
        controller: 'FitnessTracker.Summary.Controller',
    })
}]);

summary.filter('unity', function () {
    return function (n, type,key) {
        if(typeof (n) === 'undefined'){
            return 0;
        }
        if(type == 'distance'){
            if(key == 'misfit'){
                return (n *1.609).toFixed(2);
            }else{
                return (n / 1000.0).toFixed(2);
            }
        }
        return (n / 1.0).toFixed(0);
    }
});
summary.controller('FitnessTracker.Summary.Controller',['$resource', '$scope', '$rootScope', '$http', '$location', 'Session.Service', function($resource, $scope, $rootScope, $http, $location, Session) {
    var SleepSummary = $resource('/fitnesstracker/api/v1/user/:uid/sleeps', {}, {query : {method : 'GET'}})
    $scope.sleepInfo = {
        bestDay : 'no sleep data',
        worstDay : 'no sleep data'
    };

    $scope.user = Session.user();
    // if(Session.haveUser()){
    //     $scope.user = Session.user();
    // }else{
    //     Session.destroy();
    //     $location.path("/login");
    // }
    $scope.device = Session.user().device;
    $scope.maxDate = new Date();
    $scope.selecttime = {start : new Date(new Date().setDate(new Date().getDate() - 5)), end : new Date()};
    $scope.average = 0;
    $scope.info = {
        mincalories : 0 ,
        maxcalories : 0 ,
        minsteps : 0 ,
        maxsteps : 0 ,
        mindistance : 0 ,
        maxdistance : 0 ,
    }

    $scope.show = function(){
        $scope.customCalender = !$scope.customCalender;
    }


    $scope.search = function(val){
        if(val != 1){
            $scope.selecttime.end = new Date();
            var targetDate = new Date($scope.selecttime.end.getTime());
            $scope.selecttime.start = new Date(targetDate.setDate(targetDate.getDate() - val));
        }
        //-------------------------------------------------------------search all summary sum
        $http({
            method:'GET',
            url:'/fitnesstracker/api/v1/user/'+ $scope.user.id +'/summary',
            params:{'start': $scope.selecttime.start, 'end': $scope.selecttime.end, 'device' : $scope.user.device}
        }).then(
            function (response) {
                var responseData = response.data;
                $scope.summary = response.data;
                calculatAverage($scope.dataType);
            },
            function (error) {
                $location.url("/selectdevice?delete=true&id=" + $scope.user.id);
            }
        );

        //------------------------------------------------------------get each summary details

        $http({
            method:'GET',
            url:'/fitnesstracker/api/v1/user/'+ $scope.user.id +'/summary/detail',
            params:{'start': $scope.selecttime.start, 'end': $scope.selecttime.end, 'device' : $scope.user.device}
        }).then(
            function (response) {
                var responseData = response.data;
                drawnActiveSummaryChart(responseData.summary);
            }
        );

        //---------- drawn sleep summary----------
        searchSleepSummary();
    };

    function calculatAverage(val) {
        var one_day = 1000*60*60*24;
        var diff = new Date($scope.selecttime.end.getFullYear(), $scope.selecttime.end.getMonth(), $scope.selecttime.end.getDate()).getTime()
            - new Date($scope.selecttime.start.getFullYear(), $scope.selecttime.start.getMonth(), $scope.selecttime.start.getDate()).getTime();
        var diff_day = Math.round( diff/ one_day);
        $scope.average = ($scope.summary[val] / diff_day).toFixed(2);
    }
    
    $scope.changeDataType = function (val) {
        $scope.dataType = val;
        calculatAverage($scope.dataType);
    };

    function activityChart(activedata){
        google.charts.load("current", {packages: ["corechart"]});
        google.charts.setOnLoadCallback(drawActiveChart);
        function drawActiveChart() {
            var dataTable = new google.visualization.DataTable();
            dataTable.addColumn('date', 'day');
            dataTable.addColumn('number','steps');
            dataTable.addColumn('number','distance');
            dataTable.addRows(activedata);
            var options = {
                title : $scope.dataType+' Summary from'+ $scope.selecttime.start.yyyy_mm_dd() + ' to ' + $scope.selecttime.end.yyyy_mm_dd(),
                titleTextStyle: {
                    color: 'black',
                    fontSize: 16,
                    fontName: 'Arial',
                    bold: true,
                    italic: false
                },
                legend: {
                    position: 'top',
                },
                bar: {
                    groupWidth: '33%'
                },
                hAxis: {
                    minValue : new Date($scope.selecttime.start.getFullYear(), $scope.selecttime.start.getMonth(), $scope.selecttime.start.getDate()),
                    maxValue : new Date($scope.selecttime.end.getFullYear(), $scope.selecttime.end.getMonth(), $scope.selecttime.end.getDate()),
                    gridlines: {color: 'none'},
                    textStyle: {
                        color: 'black',
                        fontSize: 12,
                        fontName: 'Arial',
                        bold: true,
                        italic: false
                    },
                    viewWindowMode: 'pretty',
                    format: 'MMM dd, yyyy'
                },
                vAxis: {
                    format: 'short',
                    textStyle: {
                        color: 'black',
                        fontSize: 12,
                        fontName: 'Arial',
                        bold: true,
                        italic: false
                    }
                },
            };
            var view = new google.visualization.DataView(dataTable);
            var cols = [0];
            if($scope.dataType == 'steps'){
                cols.push(1);
            }else{
                cols.push(2);
            }
            view.setColumns(cols);
            var chart = new google.visualization.ColumnChart(document.getElementById('curve_chart'));
            chart.draw(view, options);

            $scope.changeDataType = function (val) {
                $scope.dataType = val;
                calculatAverage($scope.dataType);
                var cols = [0];
                if($scope.dataType == 'steps'){
                    cols.push(1);
                }else{
                    cols.push(2);
                }
                view.setColumns(cols);
                var chart = new google.visualization.ColumnChart(document.getElementById('curve_chart'));
                chart.draw(view, options);
            }
        }
    }
    
    function caloriesBarChart(caloriesData) {
        google.charts.load("current", {packages: ["corechart"]});
        google.charts.setOnLoadCallback(drawBarChart);
        function drawBarChart(){
            var dataTable = new google.visualization.DataTable();
            dataTable.addColumn('date', '');
            dataTable.addColumn('number','total-calories');
            dataTable.addColumn('number','activity-calories');
            dataTable.addRows(caloriesData);
            var options = {
                title:'Workout Summary from ' + $scope.selecttime.start.yyyy_mm_dd() + ' to ' + $scope.selecttime.end.yyyy_mm_dd(),
                titleTextStyle: {
                    color: 'black',
                    fontSize: 16,
                    fontName: 'Arial',
                    bold: true,
                    italic: false
                },
                bar: {
                    groupWidth: '40%'
                },
                hAxis: {
                    textStyle: {
                        color: 'black',
                        fontSize: 12,
                        fontName: 'Arial',
                        bold: true,
                        italic: false
                    },
                    minValue : new Date($scope.selecttime.start.getFullYear(), $scope.selecttime.start.getMonth(), $scope.selecttime.start.getDate()),
                    maxValue : new Date($scope.selecttime.end.getFullYear(), $scope.selecttime.end.getMonth(), $scope.selecttime.end.getDate()),
                    gridlines: {color: 'none'},
                    viewWindowMode: 'pretty',
                    format: 'MMM dd, yyyy'
                },
                vAxis: {
                    format: 'short',
                    textStyle: {
                        color: 'black',
                        fontSize: 12,
                        fontName: 'Arial',
                        bold: true,
                        italic: false
                    },
                },
            };
            var chart = new google.visualization.ColumnChart(document.getElementById('calories_chart'));
            chart.draw(dataTable, options);
        }

    }

    function drawnActiveSummaryChart(array){
        var caloriesData = [];
        var activeData = [];
        generateData(array, caloriesData, activeData);
        caloriesBarChart(caloriesData);
        activityChart(activeData);
    }

    function generateData(array, calories, actives) {
        if(array.length > 0) {
            var minCalories = Number.MAX_VALUE;
            var maxCalories = Number.MIN_VALUE;
            var minSteps = Number.MAX_VALUE;
            var maxSteps = Number.MIN_VALUE;
            var minDistance = Number.MAX_VALUE;
            var maxDistance = Number.MIN_VALUE;
            for (i = 0; i < array.length; i++) {
                var activetemp = [];
                var caloriestemp = [];
                activetemp.push(new Date(array[i].date));
                caloriestemp.push(new Date(array[i].date));
                activetemp.push({v: array[i].steps, f: array[i].steps + ''});
                maxSteps = Math.max(maxSteps, array[i].steps);
                minSteps = Math.min(minSteps, array[i].steps);
                caloriestemp.push(array[i].calories);
                caloriestemp.push(array[i].activityCalories);
                maxCalories = Math.max(maxCalories, array[i].activityCalories);
                minCalories = Math.min(minCalories, array[i].activityCalories);
                if ($scope.device == 'misfit') {
                    activetemp.push({v: (array[i].distance * 1609), f: (array[i].distance * 1.609).toFixed(2) + 'km'});
                } else {
                    activetemp.push({v: (array[i].distance), f: (array[i].distance / 1000.0).toFixed(2) + 'km'});
                }

                maxDistance = Math.max(maxDistance, array[i].distance);
                minDistance = Math.min(minDistance, array[i].distance);
                calories.push(caloriestemp);
                actives.push(activetemp);
            }
            $scope.info.maxcalories = maxCalories;
            $scope.info.mincalories = minCalories;
            $scope.info.maxdistance = maxDistance;
            $scope.info.mindistance = minDistance;
            $scope.info.maxsteps = maxSteps;
            $scope.info.minsteps = minSteps;
        }
    }



    Date.prototype.yyyy_mm_dd = function() {
        var mm = this.getMonth() + 1; // getMonth() is zero-based
        var dd = this.getDate();

        return [this.getFullYear(),
            (mm>9 ? '' : '0') + mm,
            (dd>9 ? '' : '0') + dd
        ].join('-');
    };


    function secondsToHms(d) {
        d = Number(d);
        var h = Math.floor(d / 3600);
        var m = Math.floor(d % 3600 / 60);
        var s = Math.floor(d % 3600 % 60);
        return ('0' + h).slice(-2) + ":" + ('0' + m).slice(-2) + ":" + ('0' + s).slice(-2);
    }



    //-----------------------------------------------------------------------------------search sleeps summary
    function searchSleepSummary() {
        SleepSummary.query(
            {uid : $scope.user.id, start : $scope.selecttime.start, end : $scope.selecttime.end, device : $scope.user.device},
            function (response) {
                var sleepDataSleeps = response.sleeps;
                drawnSleepSummaryData(sleepDataSleeps);
            },
            function (error) {
                console.log(error);
            }
        );
    }



    function drawnSleepSummaryData(sleepDataSleeps) {
        var data = [];
        if(sleepDataSleeps.length > 0){
            var lowestSleep = {time : Number.MAX_VALUE, Date : null};
            var highestSleep = {time : Number.MIN_VALUE, Date : null};
            for(i = 0; i < sleepDataSleeps.length; i ++){
                var temp = [];
                var durationTime = sleepDataSleeps[i].duration;
                while( ((i + 1) < sleepDataSleeps.length) && (new Date(sleepDataSleeps[i].startTime).getDate() == new Date(sleepDataSleeps[i+1].startTime).getDate())){
                    i++;
                    durationTime += sleepDataSleeps[i].duration;
                }
                var currentDateTime = new Date(sleepDataSleeps[i].startTime);
                temp.push(new Date(currentDateTime.getFullYear(), currentDateTime.getMonth(), currentDateTime.getDate()));
                temp.push({v : (durationTime/3600), f : secondsToHms(durationTime)});

                if(lowestSleep.time > durationTime){
                    lowestSleep.time = durationTime;
                    lowestSleep.Date = new Date(sleepDataSleeps[i].startTime);
                }
                if(highestSleep.time < durationTime){
                    highestSleep.time = durationTime;
                    highestSleep.Date = new Date(sleepDataSleeps[i].startTime);
                }
                data.push(temp);
            }
            $scope.sleepInfo.bestDay = highestSleep.Date.yyyy_mm_dd();
            $scope.sleepInfo.worstDay = lowestSleep.Date.yyyy_mm_dd();
        }

        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);
        function drawChart() {
            var dataTable = new google.visualization.DataTable();
            dataTable.addColumn('date', '');
            dataTable.addColumn('number', 'duration');
            dataTable.addRows(data.length == 0 ? [[new Date(),0]] : data);
            var options = {
                title: 'Sleep Summary from ' + $scope.selecttime.start.yyyy_mm_dd() + ' to ' + $scope.selecttime.end.yyyy_mm_dd(),
                titleTextStyle: {
                    color: 'black',
                    fontSize: 16,
                    fontName: 'Arial',
                    bold: true,
                    italic: false
                },
                bar: {
                    groupWidth: '33%'
                },

                hAxis: {
                    textStyle: {
                        color: 'black',
                        fontSize: 12,
                        fontName: 'Arial',
                        bold: true,
                        italic: false
                    },
                    minValue : new Date($scope.selecttime.start.getFullYear(), $scope.selecttime.start.getMonth(), $scope.selecttime.start.getDate()),
                    maxValue : new Date($scope.selecttime.end.getFullYear(), $scope.selecttime.end.getMonth(), $scope.selecttime.end.getDate()),
                    gridlines: {color: 'none'},
                    viewWindowMode: 'pretty',
                    format: 'MMM dd, yyyy'
                },
                vAxis: {
                    minValue : 0,
                    textStyle: {
                        color: 'black',
                        fontSize: 12,
                        fontName: 'Arial',
                        bold: true,
                        italic: false
                    },
                    viewWindow: {
                        min: 0
                    }

                },
                legend: 'none',
            };

            var chart = new google.visualization.ColumnChart(document.getElementById('line_top_x'));
            chart.draw(dataTable, options);

        }
    }

    var flag = true;
    if(flag && Session.isAuthenticated()){
        $scope.search(1);
        falg = false;
    }


}]);