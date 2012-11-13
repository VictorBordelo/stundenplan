'use strict';

/* Services */

angular.module('professorsServices', ['ngResource']).
	factory('Professor', function($resource)
	{
		return $resource('/rest/json/profList/get', {}, { query: {method:'GET', params:{}, isArray:true }
	});
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

/*
angular.module('phonecatServices', ['ngResource']).
    factory('Phone', function($resource){
  return $resource('phones/:phoneId.json', {}, {
    query: {method:'GET', params:{phoneId:'phones'}, isArray:true}
  });
});
*/