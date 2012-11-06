var WTSP = WTSP||{};

WTSP.divgenerator = (function() {
	
	var scrollPaneUtil =  WTSP.gui.scrollpane.util,
		tabRoomModifier, tabProfModifier, tabGrpModifier,
		guiUtil = WTSP.gui.util,
		lecturesList;
	return {
		// this should be called when the dom has loaded
		init: function() {
			tabRoomModifier = scrollPaneUtil.getScrollContainerModifier($("#tab-room")),
			tabProfModifier = scrollPaneUtil.getScrollContainerModifier($("#tab-prof")),
			tabGrpModifier = scrollPaneUtil.getScrollContainerModifier($("#tab-grp"));
		},
		// Loads and Creates the proflist in the left pane
		createProflist: function(container) {
			// callback function for wtcommunicator
			var callback = function callBack(profArray) {
				// empty all entries in the proflist
				container.empty();
				// create a new div element for every professor
				$.each(profArray,function(index, values){
					$item = $("<div class='scroll-content-item ui-widget-header' />");
					// attach a click event to the div element
					$item.click(function(event) {
						//WTSP.divgenerator.createScrollLectures(values);
						WTSP.divgenerator.fillProfTimetable(values, !event.altKey);
					});
					// fill the div element
					$content = $("<p class='prof-item'>"+ values.name + " " + values.vorname +"</p>").wrap($item).parent();
					// append the div element to the list
					container.append($content);
				});
				tabProfModifier.hideOrShowHandle();
			};
			WTSP.wtcommunicator.getProfs(callback);
		},

		// Loads and Creates the roomlist in the left pane
		createRoomlist: function(container) {
			// callback for wtcommunicator
			var callback = function callBack(roomArray) {
				// emtpy roomlist
				container.empty();
				// fill roomlist
				$.each(roomArray,function(index, values){
					$item = $("<div class='scroll-content-item ui-widget-header' />");
					// attach a click event to the div element
					$item.click(function(event) {
						//WTSP.divgenerator.createScrollLectures(values);
						WTSP.divgenerator.fillRoomTimetable(values, !event.altKey);
					});
					$content = $("<p class='room-item'>"+ values.name + " (" + values.size + ")" +"</p>").wrap($item).parent();
					container.append($content);
				});
				tabRoomModifier.hideOrShowHandle();
			};
			WTSP.wtcommunicator.getRooms(callback);
		},

		// Loads and Creates the grouplist in the left pane
		createGrouplist: function(container) {
			// callback for wtcommunicator
			var callback = function callBack(groupArray) {
				// emtpy grouplist
				container.empty();
				// fill grouplist
				$.each(groupArray,function(index, values){
					$item = $("<div class='scroll-content-item ui-widget-header' />");
					// attach a click event to the div element
					$item.click(function(event) {
						//WTSP.divgenerator.createScrollLectures(values);
						WTSP.divgenerator.fillGroupTimetable(values, !event.altKey);
					});
					$content = $("<p class='group-item'>" + "" + values.program + values.semester + values.groupCode + "</p>").wrap($item).parent();
					container.append($content);
				});
				tabGrpModifier.hideOrShowHandle();
			};
			WTSP.wtcommunicator.getStudyGroups(callback);
		},

		// Fill the Scroll Pane in the upper area with data
		createScrollLectures: function(profObj) {
			var callback = function callBack(lectureArray) {
				// container for the div elements
				var $container = $('#scroll-content-hor');

				// emtpy scroll pane
				$container.empty();

				// create div elements
				$.each(lectureArray,function(index, values){
					$item = $("<div id='draggable" + values.id + "' class='scroll-content-item ui-widget-header' />");
					$content = $("<p class='lecture-title'>"+ values.title + "\n(Raum: " + values.content + ")</p>").wrap($item).parent().
					append($("<p class='prof-name small'>" + profObj.name + " " + profObj.vorname + "</p>"));
					$container.append($content);
				});

				// Adjust the size of the Scrollpane
				/*var $citem = $container.find('.scroll-content-item'),
					margin = (parseInt($citem.css('margin-left')) || 0) +
							 (parseInt($citem.css('margin-right')) || 0),
					border = (parseInt($citem.css('border-left-width')) || 0) +
							 (parseInt($citem.css('border-right-width')) || 0);
					width = (parseInt($citem.css('width'))) || 0,
					resultingWidth = margin+border+width,
					containerWidth = count*resultingWidth;*/
				$container.css('width', containerWidth);

				// enable the elements to be draggable
				WTSP.dragAndDrop.initLecturesAsDraggable();
			};			

			WTSP.wtcommunicator.getProfPlan(profObj,callback);
		},

		// Loads a timetable for a given professor and fills the timetable
		fillProfTimetable: function(profObj, clearBefore) {
			// callback function for wtcommunicator
			var callback = function(lectureArray) {
				// store the loaded list in the cached list
				lecturesList = lectureArray;
				if(clearBefore) {
					$(".time-table-cell").empty();
				}
				// fill table cells
				$.each(lectureArray,function(index, values){
					$item = $("<div id='draggable" + values.id + "' class='scroll-content-item ui-widget-header' />");
					$content = $("<p class='class-name'>"+ guiUtil.hyphenate(values.title) + "\n(Raum: " + values.content + ")</p>").wrap($item).parent().
					append($("<p class='prof-name small'>" + "Prof: " + profObj.name + " " + profObj.vorname + "</p>"));

					// insert the div element into the table
					WTSP.timetable.insertLectureWithDiv(values, $content);
				});
			};			

			WTSP.wtcommunicator.getProfPlan(profObj, callback);
		},

		// Loads a timetable for a given StudyGroup and fills the timetable
		fillGroupTimetable: function(groupObj) {
			// callback function for wtcommunicator
			var callback = function callBack(lectureArray) {
				// store the loaded list in the cached list
				lecturesList = lectureArray;
				// empty timetable
				$(".time-table-cell").empty();
				// fill table cells
				$.each(lectureArray,function(index, values){
					$item = $("<div id='draggable" + values.id + "' class='scroll-content-item ui-widget-header' />");
					$content = $("<p class='class-name'>"+ guiUtil.hyphenate(values.title) + "\n(Raum: " + values.content + ")</p>").wrap($item).parent().
					append($("<p class='prof-name small'>" + "Group: " + groupObj.program + groupObj.semester + groupObj.groupCode + "</p>"));

					// insert the div element into the table
					WTSP.timetable.insertLectureWithDiv(values, $content);
				});
			};			

			WTSP.wtcommunicator.getGroupPlan(groupObj,callback);
		},

		// Loads a timetable for a given Room and fills the timetable
		fillRoomTimetable: function(roomObj) {
			// callback function for wtcommunicator
			var callback = function callBack(lectureArray) {
				// store the loaded list in the cached list
				lecturesList = lectureArray;
				// empty timetable
				$(".time-table-cell").empty();
				// fill table cells
				$.each(lectureArray,function(index, values){
					$item = $("<div id='draggable" + values.id + "' class='scroll-content-item ui-widget-header' />");
					$content = $("<p class='class-name'>"+ guiUtil.hyphenate(values.title) + "\n(Raum: " + values.content + ")</p>").wrap($item).parent().
					append($("<p class='prof-name small'>" + "</p>"));

					// insert the div element into the table
					WTSP.timetable.insertLectureWithDiv(values, $content);
				});
			};			

			WTSP.wtcommunicator.getRoomPlan(roomObj,callback);
		},
		
		// Returns the currently loaded Timetable (is used to move lectures)
		getLecturesInTimetable: function() {
			return lecturesList;
		}
	};
})();

$(document).ready(function() {
	WTSP.divgenerator.init();
});