angular.module('FitnessTracker.Services', ['ngResource']).service('FitnessTracker.Services.FilteredList', ['$resource', function($resource ) {
	var filterSchema = [];
	var lastFilterApplied = {};
	var Items = null; // An Angular resource
	
	var defaultFilter = function( ) {
		var result = {};
		return _.object( filterSchema.map( function( schema ) {
			return [ schema.name, schema.defaultValue ];
		} ) );		
	}
	
	var filteredListService = {
		filter : {},		
	}	
	
	filteredListService.dirty = function() {
		return ! _.isEqual( filteredListService.filter, lastFilterApplied );
	};
			
	filteredListService.hasFilters = function() {
		return _.some( filteredListService.filter );
	};

	filteredListService.apply = function() {
		var flags = _.where( filterSchema, { type : 'flag' } );
		var strings = _.where( filterSchema, { type : 'string' } );

		var filterFlags = _.pluck( _.filter( flags, function( val ) { return filteredListService.filter[ val.name ]; } ), "name" );
		var namedFilterStrings = _.filter( strings, function( val ) { return filteredListService.filter[ val.name ] } );
												
		var params = {
				filter : filterFlags.join(',')
		}
		
		namedFilterStrings.forEach( function( val ) {
			params[ val.name ]  = filteredListService.filter[ val.name ];
		} );

		filteredListService.items = Items.query( params , function( result ) {
			lastFilterApplied = _.clone( filteredListService.filter );
		} );
	};

	filteredListService.clear  = function() {
		filteredListService.filter = defaultFilter();
	};
	
	// The config object is used to 
	// initialize the ListService prior to use
	// includes route and filter properties
	// { route : 'some route', filter : [ { name : <string>, type : <string>, defaultValue : value } ... ] }
	filteredListService.config = function( config ) {
		Items = $resource( config.route, { }, { 'update' : { method : 'PUT' } } );
		
		filterSchema = config.filter;
		filteredListService.filter = defaultFilter();		
		lastFilterApplied = defaultFilter();				
		filteredListService.items = Items.query( );
	}
	
	return filteredListService;
} ] );
