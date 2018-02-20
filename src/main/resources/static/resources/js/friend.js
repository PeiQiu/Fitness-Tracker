angular.module('Friend', []).factory('FriendService', function () {

    var _setter = function (data, type) {
        destroy();
        sessionStorage[type] = JSON.stringify( data );
        sessionStorage['contactType'] = type;
        console.log("friend Factory -- setter : ", data, type);
    };
    
    var destroy = function () {
        delete sessionStorage.friend;
        delete sessionStorage.group;
    };

    var _getter = function ( type ) {
        if( sessionStorage[type] ) {
            return JSON.parse( sessionStorage[type] );
        } else {
            console.log("get friend null");
            return null;
        }
    };
    
    var _getType = function () {
        var type = sessionStorage['contactType'];
        return type;
    };
    return {
        setter : _setter,
        getter : _getter,
        getType : _getType
    }
});
