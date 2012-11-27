'use strict';

var app = angular.module('stundenplan', ['ui', 'professorsServices','roomServices', 'groupServices']);

/* Controllers */
function ensureBackupConsistency(item, f) {
	if(!item.backup)
		item.backup = item.availability;
	f();
	if(item.backup == item.availability)
		item.backup = null;
}

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
	
	$rootScope.getAvailability = function(item, day, slot)
    {
		var index = $rootScope.index(day, slot);
		return item.availability[index];
    }

	$rootScope.undo = function(item)
    {
		ensureBackupConsistency(item, function(){
			item.availability = item.backup;
		});
    }
	
	$rootScope.isClear = function(item)
    {
		return item.availability.indexOf("0") == -1;
    }
	
	$rootScope.clear = function(item)
    {
		if(!$rootScope.isClear(item))
		{
			ensureBackupConsistency(item, function(){
				item.availability = item.availability.replace(/0/g,"1");
			});
		}
    }
	
	$rootScope.toggle = function(item, day, slot)
    {
		if(!slot)
		{
	    	$.each($rootScope.slots, function(index, slot) {$rootScope.toggle(item, day, slot);});
	    	return;
		}
		if(!day)
		{
	    	$.each($rootScope.days, function(index, day) {$rootScope.toggle(item, day, slot);});
	    	return;
		}

		ensureBackupConsistency(item, function() {
			var index = $rootScope.index(day, slot);
			var value = $rootScope.getAvailability(item, day, slot) == "0" ? "1" : "0";
			item.availability = item.availability.substr(0, index) + value + item.availability.substr(index+1);
		});
    }

	$rootScope.active = 0;
});