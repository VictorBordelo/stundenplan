'use strict';

/* Services */

angular.module('professorsServices', ['ngResource']).
	factory('Professor', function($resource)
	{
		return $resource('/rest/json/profList/get', {}, { query: {method:'GET', params:{}, isArray:true }});
	});

angular.module('roomServices', ['ngResource']).
	factory('Room', function($resource)
	{
		return $resource('/rest/json/rooms/roomList/get', {}, { query: {method:'GET', params:{}, isArray:true }
	});
});

angular.module('groupServices', ['ngResource']).
	factory('Group', function($resource)
	{
		return $resource('/rest/json/studygroup/studygrouplist/get', {}, { query: {method:'GET', params:{}, isArray:true }
	});
});
