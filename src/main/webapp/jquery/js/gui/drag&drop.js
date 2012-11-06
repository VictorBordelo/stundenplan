var WTSP = WTSP || {};


WTSP.dragAndDrop = (function () {

    /**
     * Gets the wtLecture object for a lecture div element with an identical ID as the wtLecture object.
     * @param htmlElementIDAttribute {string} ID attribute of the lecture div element which should contain a lecture ID.
     */
    var getLectureForDivElement = function (htmlElementIDAttribute) {
            var lecturesArray = WTSP.divgenerator.getLecturesInTimetable(),
                lecture, pattern = /[0-9]+/g;

            for (var i = 0; i < lecturesArray.length; i++) {
                if (lecturesArray[i].id === parseInt(htmlElementIDAttribute.match(pattern))) {
                    lecture = lecturesArray[i];
                    break;
                }
            }

            return lecture;
        };


    /**
     * Makes lecture elements in the scroll-content-area and in the timetable draggable.
     * Adds event handlers, that when an item begins to be dragged, the blocked cells for the dragged
     * lecture are shown in the timetable.
     * @param $citem As a parameter the div element, that represents the lecture,
     * has to be used in the function.
     */
    var setScrollContentItemTableItemAsDraggable = function ($citem) {
            var pattern = /[0-9]+/g,
                timetable = WTSP.timetable;

            $citem.draggable({
                cursor: "move",
                snap: ".time-table-cell",
                /*snapMode: "inner",
			snapTolerance: 100,*/
                revert: "invalid",
                //			stack: "#"+$citem.attr('id'),
                zIndex: 2500,
                //			appendTo: "body",
                helper: "clone",
                start: function (event, ui) {
                    timetable.initTimetableWithBlockedCells(getLectureForDivElement($(this).attr('id')));
                },
                stop: function (event, ui) {
                    timetable.clearTimetableWithBlockedCells();
                }
            });
        };


    /**
     * Initializes all lectures in the scroll content area as draggable.
     */
    var initLecturesAsDraggable = function () {
            var $container = $('.scroll-content-hor'),
                $scrollContentItems = $container.find('.scroll-content-item');

            if ($scrollContentItems.length > 1) {
                for (var i = 0; i < $scrollContentItems.length; i++) {
                    setScrollContentItemTableItemAsDraggable($($scrollContentItems[i]));
                }
            } else if ($scrollContentItems.length == 1) {
                setScrollContentItemTableItemAsDraggable($scrollContentItems);
            }
        };


    var calculateScrollContentHorizontalLength = function (newNumberItems) {
            var $container = $('.scroll-content-hor'),
                $scrollContentItem = $container.find('.scroll-content-item'),
                margin = (parseInt($scrollContentItem.css('margin-left')) || 0) + (parseInt($scrollContentItem.css('margin-right')) || 0),
                border = (parseInt($scrollContentItem.css('border-left-width')) || 0) + (parseInt($scrollContentItem.css('border-right-width')) || 0),
                width = (parseInt($scrollContentItem.css('width')) || 0),
                resultingWidth = margin + border + width,
                containerWidth = ($($container).find(".scroll-content-item").length + newNumberItems) * resultingWidth;

            return containerWidth;
        };


    /** drop on horizontal bar (?) */
    $(function () {
        $('#scroll-content-hor').droppable({
            stack: '#scroll-content-hor',
            tolerance: "pointer",
            zIndex: 2501,
            hoverClass: "ui-state-active",
            drop: function (event, ui) {
                var draggableID = "#" + ui.draggable.attr('id'),
                    $draggableItem = $(draggableID),
                    $scrollContentItem,
                    lectureObject;

                if ($(this).find(draggableID).length === 0) {
                    $draggableItem.draggable("option", "revert", false);
                    $draggableItem.removeClass('table-content-item');
                    $draggableItem.css({
                        "width": "",
                        "height": ""
                    });
                    $draggableItem.addClass("scroll-content-item");

                    $draggableItem.appendTo(this);//in die bar rein
                    lectureObject = getLectureForDivElement(ui.draggable.attr('id'));
                    lectureObject.tag = 0;
                    lectureObject.zeit = 0;
                    WTSP.wtcommunicator.setLecture(lectureObject);

                    $(this).css('width', calculateScrollContentHorizontalLength(1));
                } else {
                    $draggableItem.draggable("option", "revert", true);
                }
            },
            out: function (event, ui) {
                /*$(".scroll-content-hor").removeClass('ui-state-highlight');*/
                /*$(".workday").droppable( "destroy" );*/
            }
        });
    });
    
    var isCellBlocked = function(cell) {
    	var i;
    	for(i = 1; i <=7; i++) {
    		if(cell.hasClass("ui-state-cell-blocked-" + i)) {
    			return true;
    		}
    	}
    	return false;
    };


    $(function () {
        $(".time-table-cell").droppable({
            hoverClass: "ui-state-cell-active",
            tolerance: "pointer",
            drop: function (event, ui) {
                var $draggableItem = $("#" + ui.draggable.attr('id')),
                    $tableContentItem, 
                    $container = $('#scroll-content-hor'),
                    lectureObject, day, time;

                if ($(this).find(".table-content-item").length === 0 && !isCellBlocked($(this))) {
                    $draggableItem.draggable("option", "revert", false);

                    $(this).addClass("ui-state-highlight");

                    day = $(this).index();
                    time = $(this).closest('tr').index() + 1;

                    lectureObject = getLectureForDivElement(ui.draggable.attr('id'));
                    lectureObject.tag = day;
                    lectureObject.zeit = time;

                    WTSP.wtcommunicator.setLecture(lectureObject);

                    WTSP.timetable.insertLectureWithDraggableItem($draggableItem, this);

                    // prevent, that length of scroll-content-hor is set to zero
                    if (calculateScrollContentHorizontalLength(0) !== 0) {
                        $container.css('width', calculateScrollContentHorizontalLength(0));
                    }
                } else {
                    $draggableItem.draggable("option", "revert", true);
                }
            },
            out: function (event, ui) {
                $(this).removeClass('ui-state-highlight');
            },
            activate: function (event, ui) {
                var $tcItem = $('.table-content-item');
                if (ui.helper.hasClass("table-content-item")) {
                    ui.helper.width($(this).width() - parseInt($tcItem.css('margin-left')) - parseInt($tcItem.css('margin-right')) - 2);
                }
            }
        });
    });

    return {
        initLecturesAsDraggable: initLecturesAsDraggable,
        setScrollContentItemTableItemAsDraggable: setScrollContentItemTableItemAsDraggable
    };
})();