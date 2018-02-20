var myPosts = angular.module('FitnessTracker.mySharing', ['ngRoute', 'ngMaterial']);

myPosts.config(['$routeProvider', function($routeProvider){
    $routeProvider.when('/mysharing', {
        templateUrl: '/resources/views/applications/myposts.html',
        controller: 'FitnessTracker.eventCalendar.controller'
    })
}]);


myPosts.filter('imageUrl', function () {
    return function (id) {
        return '/fitness/api/v1/user/'+ id +'/avatars';
    }
});

myPosts.controller("FitnessTracker.eventCalendar.controller", ['$location', '$scope', '$http', 'MyPosts', '$resource', 'Session.Service', function($location, $scope, $http, MyPosts, $resource, Session){
    var Sharings = $resource('/fitnesstracker/api/v1/user/:uid/sharings/:sid', {}, { update : {method : 'PUT'} , delete : {method : 'DELETE'}, query : {method : 'GET', isArray: true,
        transformResponse: function(response) {
            var newData = JSON.parse(response);
            newData.map(function(data) {
                data._length = data.length;
                return data;
            });
            return newData;
        }
    }});
    var Comments = $resource('/fitnesstracker/api/v1/user/:uid/sharings/:sid/comments/:cid',{}, {save : {method : 'POST'}, delete : {method : 'DELETE'}, query : {method : 'GET', isArray : true}})

    $scope.commentForSharing = {
        content: '',
        replyFirstComment:'',
        replySecondComment:''
    };

    $scope.selectedCondition = {
        myDate : null,
        content : ''
    };
    $scope.myDate = new Date();
    $scope.user = Session.user();
    $scope.Posts = {
        posts : [],
    };
    // $scope.Posts = new MyPosts();
    //{ uid : user.id, page : this.page, selectDate : datetime, content : this.content}
    $scope.reLoadMyPost = function(){
        Sharings.query({uid : $scope.user.id},
            function (data) {
                console.log("initial return datas", data);
                addArray(data);
                console.log("after process datas", data);
                $scope.Posts.posts = data;
            },
            function (error) {
                console.log('error', error);
            }
        );
    };


    $scope.numberToDisplay = 5;
    $scope.loadMore = function() {
        console.log("number to display", $scope.numberToDisplay);
        if ($scope.numberToDisplay + 5 < $scope.Posts.posts.length) {
            $scope.numberToDisplay += 5;
        } else {
            $scope.numberToDisplay = $scope.Posts.posts.length;
        }
    };

    $scope.byCondition = function (selectedCondition) {
        var content = selectedCondition.content;
        var myDate = selectedCondition.myDate;
        var start = Number.MIN_VALUE;
        var end = Number.MAX_VALUE;
        // console.log("start", start, "end", end);
        if(myDate != null){
            start = myDate.getTime();
            end = myDate.getTime() + (1000*60*60*24);
        }
        return function predicateFunc(item) {
            // console.log("item", item);
            return item.content.indexOf(content) >= 0 && item.sharingDate >= start && item.sharingDate <= end;
        }
    };

    // $scope.$watch('selectedCondition.myDate', function() {
    //     console.log("change value ", $scope.selectedCondition.myDate);
    //     $scope.Posts = new MyPosts();
    // }, true);
    //
    // $scope.searchMyPost = function(){
    //     console.log("search sharing ", $scope.selectedCondition.content);
    //     $scope.Posts = new MyPosts();
    // };

    var addArray = function(value){
        for(var i =0; i < value.length; i ++){
            // console.log("values", value[i]);
            var temp = value[i];
            temp.nums = temp.follow.length;
            // console.log("=========follower=", temp, temp.follow, temp.folowers);
            for(var j =0; j < temp.follow.length; j ++){
                if(temp.follow[j] == $scope.user.id){
                    temp.color = 'red';
                }else{
                    temp.color = '';
                }
            }
        }
    };



    $scope.create = function () {
        $location.url('/createSharings');
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
                    $scope.Posts.posts[indexSharing].comments.unshift(response.data)
                }
            },
            function(error){
                console.log("error", error);
            }
        );
    };

    function makeUrlString(sid, cid) {
        if(cid){
            console.log("comtain cid", cid);
            return '/fitnesstracker/api/v1/user/'+ $scope.user.id +'/sharings/'+ sid +'/comments/'+ cid;
        }else{
            return '/fitnesstracker/api/v1/user/'+ $scope.user.id +'/sharings/'+ sid +'/comments';
        }
    }

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
    //'/fitnesstracker/api/v1/user/:uid/sharings/:sid'
    $scope.deleteSharing = function (post) {
        console.log("delete post", post);
        Sharings.delete({uid : $scope.user.id, sid : post.id},
            function (data) {
                // console.log("sharing data", data);
                $scope.reLoadMyPost();
            },
            function (error) {
                console.log("error", error);
            }
        );
    };

    ///fitnesstracker/api/v1/user/:uid/sharings/:sid/comments/:cid
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
    // $scope.getImage = function(uid){
    //     console.log("send uid", uid);
    //     $scope.imageUrl =  '/fitness/api/v1/user/'+ uid +'/avatars';
    // };
    var flag = true;
    if(flag && Session.isAuthenticated()){
        $scope.reLoadMyPost();
        flag = false;
    }

}]);

myPosts.factory('MyPosts', ['$http', '$resource', 'Session.Service', function($http, $resource, Session) {
    var AllRelativeSharings = $resource('/fitnesstracker/api/v1/user/:uid/sharings', {}, {query : {method : 'GET', isArray: true,
        transformResponse: function(response) {
            var newData = JSON.parse(response);
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
        this.myDate = null;
        this.content = "";
    };
    Posts.prototype.nextPage = function() {
        var addArray = function(value){
            if(value.length == 0){
                this.busy = true;
            }else{
                for(var i =0; i < value.length; i ++){
                    // console.log("values", value[i]);
                    var temp = value[i];
                    temp.nums = temp.follow.length;
                    // console.log("=========follower=", temp, temp.follow, temp.folowers);
                    for(var j =0; j < temp.follow.length; j ++){
                        if(temp.follow[j] == user.id){
                            temp.color = 'red';
                        }else{
                            temp.color = '';
                        }
                    }
                }
                // console.log("======= congif value", value);
                this.posts = this.posts.concat(value);
                this.busy = false;
                this.page ++;
            }
        }.bind(this);

        if (this.busy) return;
        this.busy = true;
        // console.log("condition = +++", $scope.selectedCondition.myDate, $scope.selectedCondition.content);
        var datetime = this.myDate == null ? 0 : this.myDate.getTime();
        AllRelativeSharings.query({ uid : user.id, page : this.page, selectDate : datetime, content : this.content},
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



/*
* $scope.gotoComment = function(){
 $location.url('/comments');
 }


 $scope.documentslist = this.todos;
 $scope.DocumentsItems = 18;
 $scope.currentDocumentsPage = 1;
 $scope.maxDocumentsSize = 5;
 $scope.itemsDocumentsPerPage = 10;
*
*
*
*
*
* */