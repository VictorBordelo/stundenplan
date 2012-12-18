'use strict';

var PROFESSOR = 0;
var GROUP = 1;
var ROOM = 2;

/* Controllers */
function select($scope, type, val) {
	if($scope.selected[type]) $scope.selected[type].selected = false;
	val.selected = true;
	$scope.selected[type] = val;
	if(!val.availability) val.availability = "00000000000000000000000000000000000";
}

function DataController($scope, Professor, Group, Room) {
	$scope.professors = Professor.query();
	$scope.groups = Group.query();
	$scope.rooms = Room.query();
	$scope.selected = {};
    
	$scope.unselect = function(type)
    {
		if($scope.selected[type]) $scope.selected[type].selected = false;
		$scope.selected[type] = null;
    }
	
	$scope.clearAll = function()
    {
		$.each($scope.professors, function(index, item) {$scope.$root.clear(item);});
		$.each($scope.groups, function(index, item) {$scope.$root.clear(item);});
		$.each($scope.rooms, function(index, item) {$scope.$root.clear(item);});
    }
	
	$scope.undoAll = function()
    {
		$.each($scope.professors, function(index, item) {$scope.$root.undo(item);});
		$.each($scope.groups, function(index, item) {$scope.$root.undo(item);});
		$.each($scope.rooms, function(index, item) {$scope.$root.undo(item);});
    }
	
    $scope.selectProfessor = function(val, $event)
    {
    	$('.ui-tooltip').hide();
    	select($scope, 0, val);
    }
    
    $scope.selectGroup = function(val)
    {
    	$('.ui-tooltip').hide();
    	select($scope, 1, val);
    }
    
    $scope.selectRoom = function(val)
    {
    	$('.ui-tooltip').hide();
    	select($scope, 2, val);
    }

    $scope.save = function(val)
    {	
    	if(!$scope.modalShown)
    	{
	    	$scope.changed = {
					professors: $.grep($scope.professors, function(value, index){ return value.backup;}),
					groups: $.grep($scope.groups, function(value, index){ return value.backup;}),
					rooms: $.grep($scope.rooms, function(value, index){ return value.backup;}),
			};
	    	$scope.modalShown = true;
    	}
    	else
    	{
    		var count =
    			$scope.changed['professors'].length +
    			$scope.changed['groups'].length +
    			$scope.changed['rooms'].length;

        	var success = function(value) {
        		value.backup = null;
        		count--;
        		if(count == 0)
        	    	$scope.modalShown = false;
        		$scope.$apply();
        	}
        	
    		$.each($scope.changed['professors'], function(index, value){
    			if(value.backup) {
    				$.ajax({
    					  type: 'PUT',
    					  url: "/rest/json/profList/availability/put/"+value.id,
    					  data: JSON.stringify({id: value.id, availability: value.availability}),
    					  success: function() { success(value); },
    					  contentType: "application/json",
    				});
    			}
    			else success(value);
    		});
    		$.each($scope.changed['groups'], function(index, value){
    			if(value.backup) {
    				$.ajax({
    					  type: 'PUT',
    					  url: "/rest/json/studygroup/availability/put/"+value.id,
    					  data: JSON.stringify({id: value.id, availability: value.availability}),
    					  success: function() { success(value); },
    					  contentType: "application/json",
    				});
    			}
    			else success(value);
    		});
    		$.each($scope.changed['rooms'], function(index, value){
    			if(value.backup) {
    				$.ajax({
    					  type: 'PUT',
    					  url: "/rest/json/rooms/availability/put/"+value.id,
    					  data: JSON.stringify({id: value.id, availability: value.availability}),
    					  success: function() { success(value); },
    					  contentType: "application/json",
    				});
    			}
    			else success(value);
    		});
    	}
    }
}

