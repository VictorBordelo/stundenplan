var WTSP = WTSP||{};
WTSP.gui = { scrollpane:  {}};
WTSP.gui.scrollpane.util = (function($) {
	
	// maybe cache the returned object, either in an "cache object" or associative array
	var getScrollContainerModifier = function($scrollPaneContainer) {
		var $container = $scrollPaneContainer,
	    	$pane = $container.find('.scroll-pane-tab'),
	    	$content = $container.find('.scroll-content-tab'),
	    	$handle = $container.find('.ui-slider-handle');
		return {
	    	hideOrShowHandle: function() {
				if( $pane.height() > $content.height() ) {
					$handle.css('visibility', 'hidden');
					return false;
				} else if( $handle.length && $pane.length && $content.length) {
					$handle.css('visibility', 'visible');
					console.log($handle);
					return true;
				}
				throw "scroll container modifier has not been initiated correctly";
			},
			getContainer: function() {
				return $container;
			},
			getPane: function() {
				return $pane;
			},
			getContent: function() {
				return $content;
			},
			getHandle: function() {
				return $handle;
			}
		}
	};
		
	return {
		getScrollContainerModifier: getScrollContainerModifier
	};
})(jQuery);

$(function() {
		//scrollpane parts
		var scrollPane = $( ".scroll-pane-hor" ),
			scrollContent = $( ".scroll-content-hor" );
		
		//build slider
		var scrollbar = $( ".scroll-bar-hor" ).slider({
			slide: function( event, ui ) {
				if ( scrollContent.width() > scrollPane.width() ) {
					scrollContent.css( "margin-left", Math.round(
						ui.value / 100 * ( scrollPane.width() - scrollContent.width() )
					) + "px" );
				} else {
					scrollContent.css( "margin-left", 0 );
				}
			}
		});
		
		//append icon to handle
		var handleHelper = scrollbar.find( ".ui-slider-handle" )
		.mousedown(function() {
			scrollbar.width( handleHelper.width() );
		})
		.mouseup(function() {
			scrollbar.width( "100%" );
		})
		.append( "<span class='ui-icon ui-icon-grip-dotted-vertical'></span>" )
		.wrap( "<div class='ui-handle-helper-parent'></div>" ).parent();
		
		//change overflow to hidden now that slider handles the scrolling
		scrollPane.css( "overflow", "hidden" );
		
		//size scrollbar and handle proportionally to scroll distance
		function sizeScrollbar() {
			var remainder = scrollContent.width() - scrollPane.width();
			var proportion = remainder / scrollContent.width();
			var handleSize = scrollPane.width() - ( proportion * scrollPane.width() );
			scrollbar.find( ".ui-slider-handle" ).css({
				width: handleSize,
				"margin-left": -handleSize / 2
			});
			handleHelper.width( "" ).width( scrollbar.width() - handleSize );
		}
		
		//reset slider value based on scroll content position
		function resetValue() {
			var remainder = scrollPane.width() - scrollContent.width();
			var leftVal = scrollContent.css( "margin-left" ) === "auto" ? 0 :
				parseInt( scrollContent.css( "margin-left" ) );
			var percentage = Math.round( leftVal / remainder * 100 );
			scrollbar.slider( "value", percentage );
		}
		
		//if the slider is 100% and window gets larger, reveal content
		function reflowContent() {
				var showing = scrollContent.width() + parseInt( scrollContent.css( "margin-left" ), 10 );
				var gap = scrollPane.width() - showing;
				if ( gap > 0 ) {
					scrollContent.css( "margin-left", parseInt( scrollContent.css( "margin-left" ), 10 ) + gap );
				}
		}
		
		//change handle position on window resize
		$( window ).resize(function() {
			resetValue();
			sizeScrollbar();
			reflowContent();
		});
		//init scrollbar size
		setTimeout( sizeScrollbar, 10 );//safari wants a timeout
});
$(function() {
	var makeSlider = function(scrollPane) {
		//scrollpane parts
		var scrollContent = scrollPane.find(".scroll-content-tab");
		//build slider
		var scrollbar = scrollPane.find( ".scroll-bar-tab" ).slider({
			orientation: 'vertical',
			value: 100,
			min: 0,
			max: 100,
			slide: function( event, ui ) {
				if ( scrollContent.height() > scrollPane.height() ) {
					scrollContent.css( "margin-top", 
						Math.round(
							(100 - ui.value) / 100 * ( scrollPane.height() - scrollContent.height())
					) + "px" );
				} else {
					scrollContent.css( "margin-top", 0 );
				}
			}
		}). //scrollbar
			height( scrollPane.height() - ( scrollPane.find('.ui-slider-handle').height()) ).
			css('top', (scrollPane.find('.ui-slider-handle').height()/2) + "px");
		scrollPane.css( "overflow", "hidden" );
		if( scrollPane.height() > scrollContent.height() ) {
			scrollPane.parent().find('.ui-slider-handle').css('visibility', 'hidden');
		}
	};
	$('.scroll-pane-tab').each(function() {
		makeSlider($(this));
	});
});