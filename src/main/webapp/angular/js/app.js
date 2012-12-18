'use strict';

var app = angular.module('stundenplan', [ 'ui', 'professorsServices',
		'roomServices', 'groupServices' ]);

/* Controllers */
function ensureBackupConsistency(item, f) {
	if (!item.backup) {
		item.backup = item.availability;
	}
	f();
	if (item.backup === item.availability) {
		item.backup = null;
	}
}

app
		.run(function($rootScope) {
			$rootScope.days = [ {
				id : 0,
				text : 'Montag'
			}, {
				id : 1,
				text : 'Dienstag'
			}, {
				id : 2,
				text : 'Mittwoch'
			}, {
				id : 3,
				text : 'Donnerstag'
			}, {
				id : 4,
				text : 'Freitag'
			} ];
			$rootScope.slots = [ {
				id : 0,
				text : '08:15 - 09:45'
			}, {
				id : 1,
				text : '10:00 - 11:30'
			}, {
				id : 2,
				text : '11:45 - 13:15'
			}, {
				id : 3,
				text : '13:30 - 15:00'
			}, {
				id : 4,
				text : '15:15 - 16:45'
			}, {
				id : 5,
				text : '17:00 - 18:30'
			}, {
				id : 6,
				text : '18:45 - 20:15'
			} ];

			$rootScope.allClearValue = "";
			$rootScope.allSetValue = "";
			$.each($rootScope.slots, function(index, slot) {
				$.each($rootScope.days, function(index, day) {
					$rootScope.allClearValue += "1";
					$rootScope.allSetValue += "0";
				});
			});

			$rootScope.index = function(day, slot) {
				return day.id * $rootScope.slots.length + slot.id;
			};

			$rootScope.getAvailability = function(item, day, slot, invert) {
				var index = $rootScope.index(day, slot);
				return invert ? (item.availability[index] === "0" ? "1" : "0")
						: item.availability[index];
			};

			$rootScope.ttcontent = function() {
				var element = $(this), a, res;
				if (element.is("[title]")) {
					a = element.attr("title");

					res = "<table class='tttable'>";
					$.each($rootScope.slots, function(index, slot) {
						res += "<tr>";
						$.each($rootScope.days, function(index, day) {
							index = $rootScope.index(day, slot);
							res += "<td class='ttmark" + a[index] + "'> </td>";
						});
						res += "</tr>";
					});
					res += "</table>";

					return res;
				}
			};

			$rootScope.undo = function(item) {
				ensureBackupConsistency(item, function() {
					item.availability = item.backup;
				});
			};

			$rootScope.isClear = function(item) {
				return item !== null ? item.availability === $rootScope.allClearValue
						: false;
			};

			$rootScope.isSet = function(item) {
				return item.availability === $rootScope.allSetValue;
			};

			$rootScope.clear = function(item) {
				if (!$rootScope.isClear(item)) {
					ensureBackupConsistency(item, function() {
						item.availability = $rootScope.allClearValue;
					});
				}
			};

			var editing = false;
			$rootScope.toggle = function(item, day, slot, inside) {
				if (!slot) {
					if (editing && !inside) {
						return;
					}
					editing = true;
					$.each($rootScope.slots, function(index, slot) {
						$rootScope.toggle(item, day, slot, true);
					});
					if (!inside) {
						editing = false;
					}
					return;
				}
				if (!day) {
					if (editing && !inside) {
						return;
					}
					editing = true;
					$.each($rootScope.days, function(index, day) {
						$rootScope.toggle(item, day, slot, true);
					});
					if (!inside) {
						editing = false;
					}
					return;
				}

				var f = function() {
					var index = $rootScope.index(day, slot), value = $rootScope
							.getAvailability(item, day, slot, true);
					item.availability = item.availability.substr(0, index)
							+ value + item.availability.substr(index + 1);
				};
				ensureBackupConsistency(item, f);
			};

			// Selection by mouse drag
			$rootScope.mouseStart = null;
			$rootScope.mouseEnd = null;
			$rootScope.mouseNormalizedStart = function() {
				return {
					d : Math.min($rootScope.mouseStart['d'],
							$rootScope.mouseEnd['d']),
					s : Math.min($rootScope.mouseStart['s'],
							$rootScope.mouseEnd['s'])
				};
			};
			$rootScope.mouseNormalizedEnd = function() {
				return {
					d : Math.max($rootScope.mouseStart['d'],
							$rootScope.mouseEnd['d']),
					s : Math.max($rootScope.mouseStart['s'],
							$rootScope.mouseEnd['s'])
				};
			};
			$rootScope.mouseMode = "0";

			$rootScope.mouseDown = function(item, day, slot) {
				$rootScope.mouseStart = {
					d : day.id,
					s : slot.id
				};
				$rootScope.mouseEnd = $rootScope.mouseStart;
				$rootScope.mouseMode = $rootScope.getAvailability(item, day,
						slot, true);
			};

			$rootScope.mouseEnter = function(item, day, slot) {
				$rootScope.mouseEnd = {
					d : day.id,
					s : slot.id
				};
			};

			$rootScope.mouseUp = function(item, day, slot) {
				if ($rootScope.mouseStart !== null) {
					var d, s, start = $rootScope.mouseNormalizedStart(), end = $rootScope
							.mouseNormalizedEnd();

					for (d = start['d']; d <= end['d']; d++) {
						for (s = start['s']; s <= end['s']; s++) {
							if ($rootScope.getAvailability(item,
									$rootScope.days[d], $rootScope.slots[s]) !== $rootScope.mouseMode) {
								$rootScope.toggle(item, $rootScope.days[d],
										$rootScope.slots[s]);
							}
						}
					}
					$rootScope.mouseStart = null;
				}
			};

			$rootScope.isMouseRect = function(day, slot) {
				if ($rootScope.mouseStart !== null) {
					var start = $rootScope.mouseNormalizedStart(), end = $rootScope
							.mouseNormalizedEnd();

					return ((start['d'] <= day.id && start['s'] <= slot.id) && (end['d'] >= day.id && end['s'] >= slot.id));
				}
				return false;
			};

			$rootScope.active = 0;
		});