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

	$rootScope.allClearValue = "";
	$rootScope.allSetValue = "";
	$.each($rootScope.slots, function(index, slot) {
		$.each($rootScope.days, function(index, day) {
			$rootScope.allClearValue += "1";
			$rootScope.allSetValue += "0";
		});
	});
	
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
		return item.availability == $rootScope.allClearValue;
    }
	
	$rootScope.isSet = function(item)
    {
		return item.availability == $rootScope.allSetValue;
    }
	
	$rootScope.clear = function(item)
    {
		if(!$rootScope.isClear(item))
		{
			ensureBackupConsistency(item, function(){
				item.availability = $rootScope.allClearValue;
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

	$rootScope.mouseStart = null;
	$rootScope.mouseEnd = null;
	$rootScope.mousedown = function(item, day, slot)
    {
		$rootScope.mouseStart = {d: day.id, s: slot.id};
		$rootScope.mouseEnd = {d: day.id, s: slot.id};
		$rootScope.mouseMode = $rootScope.getAvailability(item, day, slot) == "0" ? "1" : "0";
		
		return false;
    }

	$rootScope.mouseenter = function(item, day, slot)
    {
		$rootScope.mouseEnd = {d: day.id, s: slot.id};
    }

	$rootScope.mouseup = function(item, day, slot)
    {
		var start = $rootScope.mouseStart;
		var end = {d: day.id, s: slot.id};

		var start = {d: Math.min($rootScope.mouseStart['d'], $rootScope.mouseEnd['d']),
				     s: Math.min($rootScope.mouseStart['s'], $rootScope.mouseEnd['s'])};
		var end =   {d: Math.max($rootScope.mouseStart['d'], $rootScope.mouseEnd['d']),
			         s: Math.max($rootScope.mouseStart['s'], $rootScope.mouseEnd['s'])};

		$.each($rootScope.slots, function(index, slot) {
			$.each($rootScope.days, function(index, day) {
				if($rootScope.isMouseRect(day, slot))
					if($rootScope.getAvailability(item, day, slot) != $rootScope.mouseMode)
						$rootScope.toggle(item, day, slot);
			});
		});

		$rootScope.mouseStart = null;
		return false;
    }
	
	$rootScope.isMouseRect = function(day, slot)
    {
		if($rootScope.mouseStart != null)
		{
			var start = {d: Math.min($rootScope.mouseStart['d'], $rootScope.mouseEnd['d']),
					     s: Math.min($rootScope.mouseStart['s'], $rootScope.mouseEnd['s'])};
			var end =   {d: Math.max($rootScope.mouseStart['d'], $rootScope.mouseEnd['d']),
				         s: Math.max($rootScope.mouseStart['s'], $rootScope.mouseEnd['s'])};

			var res = (start['d'] <= day.id && start['s'] <= slot.id) &&
					  (end['d']   >= day.id && end['s']   >= slot.id);
			
			return res;
		}
		else return false;
    }
	
	$rootScope.active = 0;
});