angular.module('Session', []).factory('Session.Service', function () {
   var service = {};

   service.user = function( ) {
      if( sessionStorage.user ) {
         return JSON.parse( sessionStorage.user );
      }

      else if( localStorage.user ){
          return JSON.parse( localStorage.user );
      }

      else{
         return null;
      }
   };
   
   service.hasRole = function( role ) {
	   var user = service.user();
       // console.log("hasrole", user)
	   if( user ) {
           // console.log("hase role user authority", user.authorities)
           if( user.authorities ){
               for( var i = 0; i < user.authorities.length; i++) {
                   if( user.authorities[i].name === role ) return true;
               }
           }
	   }
	   return false;
   }

   service.isAuthenticated = function() {
       return (sessionStorage.user && true) || (localStorage.user && true);
   };

   service.haveUser = function(){
       return localStorage.user && true;
   };
   
   service.create  = function ( user ) {
       // console.log("create", user);
       // console.log("transfer user", JSON.stringify( user ));
      sessionStorage.user = JSON.stringify( user );
      localStorage.user = JSON.stringify(user);
   };
   
   service.destroy = function () {
      delete sessionStorage.user;
       delete localStorage.user;
   };

   return service;
} );
