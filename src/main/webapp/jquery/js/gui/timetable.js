var WTSP = WTSP || {};


WTSP.timetable = (function () {

    /**
     * Initializes timetable with blocked cells. Queries the server to get the blocked cells and sets the
     * color in the timetable for the blocked cells.
     */
    var initializeTimetableWithBlockedCells = function (object) {
            if (object instanceof WTlecture) {
                WTSP.wtcommunicator.getOccu(object, initializeTimetableWithBlockedCellsCallback);
            }
        };


    var initializeTimetableWithBlockedCellsCallback = function (data) {
            var $tr = $("table.time-table > tbody tr");

            for (var i = 0; i < data.occup.length - 1; i++) {
                for (var k = 0; k < data.occup[i].length - 1; k++) {
                    $tr.eq(i).find("td").eq(k).addClass("ui-state-cell-blocked-" + data.occup[i + 1][k + 1]);
                }
            }
        };


    /**
     * Removes the colors for the blocked cells in the timetable and sets the default colors for
     * timetable cells.
     */
    var clearTimetableWithBlockedCells = function () {
            var $timeTable = $(".time-table"),
                $tableContentItems = $timeTable.find(".time-table-cell"),
                i, j;
                for (var i = 0; i < $tableContentItems.length; i++) {
                	for(j = 1; j <= 7; j++) {
                		$($tableContentItems[i]).removeClass("ui-state-cell-blocked-" + j);
                	}
                }
        };
        
    /**
     * Inserts a lecture (div-element) in a timetable cell with proper shape and style, to fit
     * the best possible way in the timetable. This function is used for example with the drag and drop
     * mechanism, to insert the lecture in the timetable cell, when the item is dropped. The function
     * also can be used to initialize the timetable with lectures, when loading a timetable.
     * @param $citem Div-element that represents the lecture.
     * @param $timeTableCell Element, that represents the time-table-cell.
     */
    var insertLectureWithDraggableItem = function ($citem, $timeTableCell) {
            var $draggableItem = $citem,
                $tcItem;

            $draggableItem.removeClass("scroll-content-item");
            $draggableItem.addClass("table-content-item");

            $tcItem = $('.table-content-item');
            $draggableItem.height(parseInt($('.time-table-cell').css('height')) - parseInt($tcItem.css('margin-top')) - parseInt($tcItem.css('margin-bottom')) - 2 - parseInt($tcItem.css('box-shadow-top')) - parseInt($tcItem.css('box-shadow-bottom')));
            $draggableItem.width("auto");

            $draggableItem.appendTo($timeTableCell);

            // check if .table-contet-item elements already in DOM, if not, call function
            // again. Otherwise .css() method cannot be called to get margins.
            if ($tcItem.length === 0) {
                $tcItem = $('.table-content-item');
                $draggableItem.height(parseInt($('.time-table-cell').css('height')) - parseInt($tcItem.css('margin-top')) - parseInt($tcItem.css('margin-bottom')) - 2 - parseInt($tcItem.css('box-shadow-top')) - parseInt($tcItem.css('box-shadow-bottom')));
                $draggableItem.width("auto");
            }
        };


    /**
     * Can be used, when the timetable is initialized with lectures. The function should revised, as
     * the algorithm is not as efficent. A better way would be to hand over arrays as parameters in the
     * function call. That way one could optimize the queries for the time-table-cells.
     */
    var insertLectureWithDiv = function (wtLecture, divElement) {
            if (wtLecture instanceof WTlecture) {
                var lecture = wtLecture,
                    $timeTableCell,
                    $tr = $("table.time-table > tbody > tr"),
                    $bar = $('#scroll-content-hor');
                WTSP.dragAndDrop.setScrollContentItemTableItemAsDraggable(divElement);
                if(wtLecture.tag !== 0 && wtLecture.zeit !== 0) {
	                $timeTableCell = $tr.eq(lecture.zeit - 1).find('td').eq(lecture.tag - 1);
	                insertLectureWithDraggableItem(divElement, $timeTableCell);
                } else { // out of plan: to bottom bar
                	insertLectureWithDraggableItem(divElement, $bar);
                }
            }
        };


    return {
        initTimetableWithBlockedCells: initializeTimetableWithBlockedCells,
        clearTimetableWithBlockedCells: clearTimetableWithBlockedCells,
        insertLectureWithDraggableItem: insertLectureWithDraggableItem,
        insertLectureWithDiv: insertLectureWithDiv
    };
})();