'use strict';

var app = angular.module('stundenplan', ['ui', 'professorsServices','roomServices', 'groupServices']);

app.run(function($rootScope) {
	$rootScope.days =
		[
		 {id:0, text:'Montag'},
		 {id:1, text:'Dienstag'},
		 {id:2, text:'Mittwoch'},
		 {id:3, text:'Donnerstag'},
		 {id:4, text:'Freitag'},
		];
	$rootScope.slots =
		[
		 {id:0, text:'08:15 - 09:45'},
		 {id:1, text:'10:00 - 11:30'},
		 {id:2, text:'11:45 - 13:15'},
		 {id:3, text:'13:30 - 15:00'},
		 {id:4, text:'15:15 - 16:45'},
		 {id:5, text:'17:00 - 18:30'},
		 {id:6, text:'18:45 - 20:15'},
		];
	
	$rootScope.index = function(day, slot)
    {
		return day.id * $rootScope.slots.length + slot.id;
    }
	
	$rootScope.undo = function(item)
    {
		if(item.backup)
		{
			item.availability = item.backup;
			item.backup = null;
		}
    }
	
	$rootScope.toggle = function(item, day, slot)
    {
		if(!item.backup)
		{
			item.backup = item.availability;
		}

		if(!slot)
		{
	    	$.each($rootScope.slots, function(index, slot) {
	    		$rootScope.toggle(item, day, slot);
	    	});
	    	return;
		}
		if(!day)
		{
	    	$.each($rootScope.days, function(index, day) {
	    		$rootScope.toggle(item, day, slot);
	    	});
	    	return;
		}
		
		var index = $rootScope.index(day, slot);
		var char = item.availability[index] == "0" ? "1" : "0";
		item.availability = item.availability.substr(0, index) + char + item.availability.substr(index+char.length);

		if(item.backup == item.availability)
		{
			item.backup = null;
		}
    }

	$rootScope.active = 0;
});