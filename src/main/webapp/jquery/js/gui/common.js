( function($) {
	$(document).ready(function() {
	var $newEntryDialog = $('<div></div>').
		html('Create a new lecture! (Not yet implemented!)').
		dialog({
			title: 'Neuen Eintrag erstellen',
			height: 200,
			width: 400,
			modal: true,
			autoOpen: false			
		});
	
	var $optionsDialog = $('<div></div>').
		html('Option (Not yet implemented!)').
		dialog({
			title: 'Option',
			height: 200,
			width: 400,
			modal: true,
			autoOpen: false			
		});
	
	var $filler = $('<div></div>').
		html('Not yet implemented! So help yourself!').
		dialog({
			title: 'Hilfe',
			height: 200,
			width: 400,
			modal: true,
			autoOpen: false			
		});
	
	$("#buttons-top button:first").button({
		icons: {
			primary: "ui-icon-plus"
		}
	}).click(function() { $newEntryDialog.dialog("open"); }). // new entry
	next().button().click(function() { $optionsDialog.dialog("open"); }). // options
	next().button().click(function() { $optionsDialog.dialog("open"); }). //dummy
	next().button().click(function() { 
		$("#time-table-container").flip({direction:'tb'}) 
	}). //dummy
	next().button({
		icons: {
			primary: "ui-icon-info"
		}
	}).click(function() { $filler.dialog("open"); }).parent().buttonset(); // dummy
	
	$(".view-listing").tabs({
		show: function(event, ui) {
			var content = $(ui.panel)
					.find('.scroll-pane-tab .scroll-content-tab');
			
			// fill the list depending on the tab that was selected
			if(ui.panel.id === "tab-room")
				WTSP.divgenerator.createRoomlist(content);
			else if(ui.panel.id === "tab-prof")
				WTSP.divgenerator.createProflist(content);
			else if(ui.panel.id === "tab-grp")
				WTSP.divgenerator.createGrouplist(content);
		}
	});

});
})(jQuery);
