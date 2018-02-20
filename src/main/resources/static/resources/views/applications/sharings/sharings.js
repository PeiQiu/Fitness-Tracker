var sharings = angular.module("FitnessTracker.sharings", ['ngRoute','ngMaterial', 'ngResource', 'infinite-scroll']);

sharings.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when("/sharings", {
        templateUrl: '/resources/views/applications/sharings/sharings.html',
        controller: 'FitnessTracker.sharings.controller'
    });
}]);

sharings.filter('imageUrl', function () {
    return function (id) {
        return '/fitness/api/v1/user/'+ id +'/avatars';
    }
});


sharings.filter('checkNum', function () {
    return function (n) {
        if(n > 100){
            return (n/100.0).toFixed(1) + 'k';
        }else{
            return (n/1.0).toFixed(0);
        }
    }
});

sharings.filter('checkUnit', function () {
    return function (n, user) {
        if(typeof (n) === 'undefined'){
            return 0;
        }
        if(user.device == 'misfit'){
            return (n *1.609).toFixed(1) + 'km';
        }else{
            return (n / 1000.0).toFixed(1) + 'km';
        }
    }
});
sharings.filter('getTime', function () {
    return function (datenums, duration) {
        if(typeof (datenums) === 'undefined'){
            return 0;
        }
        var date = new Date(datenums + duration * 1000);
        return date.toTimeString().split(' ')[0];
    }
});


sharings.filter('compareTime', function(){
    function formatMsgTime (timespan) {
        var dateTime = new Date(timespan);
        var year = dateTime.getFullYear();
        var month = dateTime.getMonth() + 1;
        var day = dateTime.getDate();
        var hour = dateTime.getHours();
        var minute = dateTime.getMinutes();
        var second = dateTime.getSeconds();
        var now = new Date();
        var now_new = now.getTime();
        var milliseconds = 0;
        var timeSpanStr;
        milliseconds = now_new - timespan;
        if (milliseconds <= 1000 * 60 * 1) {
            timeSpanStr = 'just now';
        }
        else if (1000 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60) {
            timeSpanStr = Math.round((milliseconds / (1000 * 60))) + ' mins';
        }
        else if (1000 * 60 * 60 * 1 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24) {
            timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60)) + ' hr';
        }
        else if (1000 * 60 * 60 * 24 < milliseconds && milliseconds <= 1000 * 60 * 60 * 24 * 15) {
            timeSpanStr = Math.round(milliseconds / (1000 * 60 * 60 * 24)) + ' day';
        }
        else if (milliseconds > 1000 * 60 * 60 * 24 * 15 && year == now.getFullYear()) {
            timeSpanStr = month + '-' + day + '@' + hour + ':' + minute;
        } else {
            timeSpanStr = year + '-' + month + '-' + day + '@' + hour + ':' + minute;
        }
        return timeSpanStr;
    };
    return function (commentDate) {
        return formatMsgTime(commentDate);
    }
});

// sharings.directive('repeatData',function(){
//     return {
//         link: function(scope,element,attr){
//             console.log("direction type", scope, element, attr);
//             console.log('direction index', scope.$index);
//             scope.$eval( attr.repeatData );
//         }
//     }
// });

// sharings.directive("SharingComment", function() {
//     return {
//         restrict: "E",
//         scope: {
//             currentFolder: '='
//         },
//         // templateUrl: 'template/tree.html'
//     };
// });
sharings.directive('focusMe', function($timeout) {
    return {
        scope: { trigger: '=focusMe' },
        link: function(scope, element) {
            scope.$watch('trigger', function(value) {
                if(value === true) {
                    //console.log('trigger',value);
                    //$timeout(function() {
                    element[0].focus();
                    scope.trigger = false;
                    //});
                }
            });
        }
    };
});


sharings.controller('FitnessTracker.sharings.controller',[ '$resource','$mdPanel', '$scope', '$rootScope', '$http', '$location', 'Session.Service', 'Posts', 'Ranks', function($resource, $mdPanel, $scope, $rootScope, $http, $location, Session, Posts, Ranks) {
    var Sharings = $resource('/fitnesstracker/api/v1/user/:uid/sharings/:sid', {}, { update : {method : 'PUT'} , delete : {method : 'DELETE'}});
    var Rank = $resource('/fitnesstracker/api/v1/user/:uid/ranks/:rid', {}, {query : {method : 'GET'}, update : {method : 'PUT'}});
    var Comments = $resource('/fitnesstracker/api/v1/user/:uid/sharings/:sid/comments/:cid',{}, {save : {method : 'POST'}, delete : {method : 'DELETE'}, query : {method : 'GET', isArray : true}})
    var RankInitial = $resource('/fitnesstracker/api/v1/user/:uid/ranks', {}, {query : {method : 'GET', isArray : true,
        transformResponse: function(response) {
            var newData = JSON.parse(response);
            newData.map(function(data) {
                data._length = data.length;
                return data;
            });
            return newData;
        }
    }});
    $scope.user = Session.user();

    console.log("$scope.user", $scope.user);

    $scope.Posts = new Posts();
    $scope.Ranks = {//new Ranks();
        ranks : [],
    };

    $scope.reLoadRanks = function () {
        RankInitial.query({uid : $scope.user.id},
            function (data) {
                addNewData(data);
                $scope.Ranks.ranks = data;
            },
            function (error) {
                console.log("error", error);
            }
        )
    };
    var addNewData = function (datas) {
        if(datas.length == 0){
            return;
        }
        for(var i =0; i < datas.length; i ++){
            if(datas[i].like){
                datas[i].color = '#f00';
            }
            datas[i].num = datas[i].followers.length;
        }
    };

    $scope.showViewMoreComments = true;
    $scope.commentForSharing = {
        content: '',
        replyFirstComment:'',
        replySecondComment:''
    };

    $scope.numberToDisplay = 10;
    $scope.loadMore = function() {
        console.log("number to display", $scope.numberToDisplay);
        if ($scope.numberToDisplay + 10 < $scope.Posts.posts.length) {
            $scope.numberToDisplay += 10;
        } else {
            $scope.numberToDisplay = $scope.Posts.posts.length;
        }
    };

    function getCommentFromType(commentType) {
        var content = '';
        if(commentType == 0){
            content = $scope.commentForSharing.content;
            $scope.commentForSharing.content = '';
        }else if(commentType == 1){
            content = $scope.commentForSharing.replyFirstComment;
            $scope.commentForSharing.replyFirstComment = '';
        }else{
            content = $scope.commentForSharing.replySecondComment;
            $scope.commentForSharing.replySecondComment = '';
        }
        console.log("----", content, 'index', commentType);
        return content;
    }

    $scope.createNewComment = function (sharingId, indexSharing, comment, indexComment, commentType, cid) {
        console.log("create new comments", sharingId, indexSharing, comment, commentType);
        var CommentContent = getCommentFromType(commentType);
        if(CommentContent == ''){
            return;
        }
        var newComment = {
            customerUsername : $scope.user.username,
            content : CommentContent,
        };
        if(comment){
            newComment.parentCommentId = comment.id;
        }else{
            newComment.sharingId = sharingId;
        }
        var urlResourceValue = makeUrlString(sharingId, cid);
        console.log("------ index of comments", $scope.Posts.posts[indexSharing].comments, $scope.Posts.posts[indexSharing].comments[indexComment]);
        $http({
            url : urlResourceValue,
            method : 'POST',
            data : newComment
        }).then(
            function (response) {
                console.log("create comments response data", response.data);
                if(comment){
                    $scope.Posts.posts[indexSharing].comments[indexComment] = response.data;
                }else{
                    $scope.Posts.posts[indexSharing].comments.unshift(response.data);
                    $scope.Posts.posts[indexSharing].totalComments ++;
                }
            },
            function(error){
                console.log("error", error);
            }
        );
    };

    $scope.deleteComment = function(comment, index, id){
        console.log("delete comment", comment, index, id);
        Comments.delete({uid : $scope.user.id, sid : id, cid : comment.id},
            function (data) {
                console.log("success data", data);
                $http({
                    url : '/fitnesstracker/api/v1/user/'+$scope.user.id+'/sharings/' + id,
                    method : 'GET'
                }).then(
                    function(data){
                        // console.log("data", data, data.data);
                        $scope.Posts.posts[index] = data.data;
                    },
                    function (error) {
                        console.log("error", error);
                    }
                );
            },
            function (error) {
                console.log("error", error);
            }
        )
    };
    
    function makeUrlString(sid, cid) {
        if(cid){
            console.log("comtain cid", cid);
            return '/fitnesstracker/api/v1/user/'+ $scope.user.id +'/sharings/'+ sid +'/comments/'+ cid;
        }else{
            return '/fitnesstracker/api/v1/user/'+ $scope.user.id +'/sharings/'+ sid +'/comments';
        }
    }

    $scope.likeThisRank = function (rank) {
        console.log("rank value", rank);
        if(rank.color && rank.color == 'red'){
            rank.color = '';
            rank.num --;
        }else{
            rank.color = 'red';
            rank.num ++;
        }
        $http({
            url : '/fitnesstracker/api/v1/user/'+ $scope.user.id +'/ranks/' + rank.id,
            method : 'PUT'
        }).then(
            function (data) {
                console.log("data", data);
            },
            function (error) {
                console.log("error", error);
            }
        )
    };

    $scope.likeThisPost = function(post){
        console.log("post value", post);
        if(post.color && post.color == 'red'){
            post.color = '';
            post.nums --;
        }else{
            post.color = 'red';
            post.nums ++;
        }

        $http({
            url : '/fitnesstracker/api/v1/user/' + $scope.user.id + '/sharings/' + post.id,
            method : 'PUT'
        }).then(
            function (data) {
                console.log("data", data);
            },
            function (error) {
                console.log("error", error);
            }
        );
    };


    $scope.checkRank = function (value) {
        // console.log("+++++++rank", value);
        if(value < 5){
            return true;
        }else{
            return false;
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
    $scope.create = function () {
        $location.url('/createSharings');
    };
    // $scope.replyComment = function (value) {
    //     console.log("value", value);
    // };

    $scope.selectRank = function (rank) {
        $scope.selectRankValue = rank;
        Rank.query({uid : $scope.user.id, rid : rank.id},
            function (data) {
                drawnActiveSummaryChart(data.summarys.summary);
            },
            function (error) {
                console.log("error", error);
            }
        )
    };

    function drawnActiveSummaryChart(summarys) {
        var actives = [];
        generateData(summarys, actives);
        activityChart(actives);
    }
    function generateData(array, actives) {
        for( i =0; i < array.length; i ++){
            var activetemp = [];
            activetemp.push(new Date(array[i].date));
            activetemp.push(array[i].steps);
            actives.push(activetemp);
        }
    }
    function activityChart(activedata){
        google.charts.load("current", {packages: ["corechart"]});
        google.charts.setOnLoadCallback(drawActiveChart);
        function drawActiveChart() {
            var dataTable = new google.visualization.DataTable();
            dataTable.addColumn('date', 'day');
            dataTable.addColumn('number','steps');
            dataTable.addRows(activedata);
            var options = {
                legend: 'none',
                width: 270,
                height : 200,
                colors: ['white'],
                lineWidth : 4,
                chartArea : {
                    left: 25,
                    top : 5,
                    width: 240,
                    height: 170,
                    backgroundColor : '#1E88E5'
                },
                curveType: 'function',
                pointSize: 5,
                dataOpacity: 0.7,
                hAxis: {
                    minValue : new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate() - 7),
                    maxValue : new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate()),
                    gridlines: {color: 'none'},
                    viewWindowMode: 'pretty',
                    format: 'MM.dd'
                },
                vAxis: {
                    gridlines : {color : 'none'},
                    format: 'short',
                },
            };
            var chart = new google.visualization.AreaChart(document.getElementById('ranklineChart'));
            chart.draw(dataTable, options);
        }
    }
    
    $scope.viewMoreComments = function (post) {
        post.commentStartIndex += 5;
    };

    $scope.viewPreviousComments = function (post) {
        post.commentStartIndex -= 5;
    };

    var flag = true;
    if(flag && Session.isAuthenticated()){
        $scope.reLoadRanks();
        flag = false;
    }
}]);



sharings.factory('Posts', ['$http', '$resource', 'Session.Service',function($http, $resource, Session) {
    var AllRelativeSharings = $resource('/fitnesstracker/api/v1/user/:uid/sharings/friends', {}, {query : {method : 'GET', isArray: true,
        transformResponse: function(response) {
            var newData = JSON.parse(response);
            if(newData == null) return [];
            newData.map(function(data) {
                data._length = data.length;
                return data;
            });
            return newData;
        }
    }});
    var user = Session.user();
    var Posts = function() {
        this.posts = [];
        this.page = 0;
        this.busy = false;
    };
    Posts.prototype.nextPage = function() {
        var addArray = function(value){
            if(value.length == 0){
                this.busy = true;
            }else{
                for(var i =0; i < value.length; i ++){
                    console.log("values for one post", value[i]);
                    var temp = value[i];
                    temp.nums = temp.follow.length;
                    temp.commentStartIndex = 0;
                    temp.totalComments = temp.comments.length;
                    // console.log("=========follower=", temp, temp.follow, temp.folowers);
                    for(var j =0; j < temp.follow.length; j ++){
                        if(temp.follow[j] == user.id){
                            temp.color = 'red';
                        }else{
                            temp.color = '';
                        }
                    }
                }
                console.log("======= congif value", value);
                this.posts = this.posts.concat(value);
                this.busy = false;
                this.page ++;
            }
        }.bind(this);
        if (this.busy) return;
        this.busy = true;
        AllRelativeSharings.query({ uid : user.id, page : this.page},
            function (data) {
                console.log("++++++++++_________)))))))) posts response data",data, data.length);
                addArray(data);
            },
            function (error) {
                console.log("sharing response error", error);
                this.busy = true;
            }
        );
    };
    return Posts;
}]);


sharings.factory("Ranks", ['$http', '$resource', 'Session.Service',function($http, $resource, Session) {
    var user = Session.user();
    var Rank = $resource('/fitnesstracker/api/v1/user/:uid/ranks', {}, {query : {method : 'GET', isArray : true,
        transformResponse: function(response) {
            var newData = JSON.parse(response);
            newData.map(function(data) {
                data._length = data.length;
                return data;
            });
            return newData;
        }
    }});
    var Ranks = function(){
        this.ranks = [];
        this.page = 0;
        this.busy = false;
    };
    Ranks.prototype.next = function () {
        var addNewData = function (datas) {
            if(datas.length == 0){
                return;
            }
            for(var i =0; i < datas.length; i ++){
                if(datas[i].like){
                    datas[i].color = '#f00';
                }
                datas[i].num = datas[i].followers.length;
            }
            this.ranks = this.ranks.concat(datas);
            this.page ++;
            this.busy = false;
        }.bind(this);
        if(this.busy == true){
            return;
        }
        this.busy = true;
        Rank.query({ uid : user.id, page : this.page},
            function (datas) {
                addNewData(datas);
            },
            function (error) {
                console.log("error");
            }
        );
    };
    return Ranks;
}]);
