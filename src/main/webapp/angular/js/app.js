'use strict';

var app = angular.module('stundenplan', ['professorsServices','roomServices', 'groupServices']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/start', {templateUrl: 'partials/professor-list.html', controller: ProfessorListCtrl}).
      otherwise({redirectTo: '/start'});
}]);

app.run(function($rootScope) {
	$rootScope.days =
		[
		 {text:'Montag', id:0},
		 {text:'Dienstag', id:1},
		 {text:'Mittwoch', id:2},
		 {text:'Donnerstag', id:3},
		 {text:'Freitag', id:4},
		 {text:'Samstag', id:5},
		 {text:'Sonntag', id:6},
		];
	$rootScope.slots =
		[
		 {text:'08:15 - 09:45', id:0},
		 {text:'10:00 - 11:30', id:1},
		 {text:'11:45 - 13:15', id:2},
		 {text:'13:30 - 15:00', id:3},
		 {text:'15:15 - 16:45', id:4},
		 {text:'17:00 - 18:30', id:5},
		 {text:'18:45 - 20:15', id:6},
		];
});