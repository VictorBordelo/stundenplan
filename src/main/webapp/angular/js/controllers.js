'use strict';

/* Controllers */

function ProfessorListCtrl($scope, Professor, Room, Group) {
	$scope.professors = Professor.query();
	$scope.rooms = Room.query();
	$scope.groups = Group.query();
}
