'use strict';

/* Controllers */

function ProfessorListCtrl($scope, Professor, Room, Group) {
	alert(Group);
	$scope.professors = Professor.query();
	$scope.rooms = Room.query();
	$scope.groups = Group.query();
}
