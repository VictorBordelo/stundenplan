/*
 * When strings are dynamically loaded and inserted into a HTML document, there
 * is a chance that it will destroy the layout because it is too large. This may
 * or may not have fatal consequences for the layout, but either way it is ugly.
 * 
 * Sometimes this can be avoided by specifying overflow: hidden; to simply cut and hide
 * content which is too large. Alternatively overflow: auto; to show scrollbars instead
 * of extending content. Neither may be possible or adequate for the layout we are using.
 * 
 * CSS3 word-wrap attribute may be a good way to fix this problem, but seems to be poorly supported
 * amongst current browsers. But an good, supported way is to use <wbr> oder &shy; tag/entity to let the
 * browser decide where to break an word. "Langes&shy;wort" will display as "Langeswort" if there is
 * enough room in the remaining of the current line. But may break to "Langes-" and "wort" if not.
 * <wbr> behaves nearly the same way, but does not insert the hyphen "-" character. Therefore it can be used
 * to break links across lines, without destroying it.
 * 
 * Either insert this tag/entity manually or use JavaScript to insert it automatically. The following code
 * is an example of a very simple JS Program which will do that. But http://code.google.com/p/hyphenator/
 * seems to be a far more sophisticated program to do this job. 
 */

WTSP = WTSP||{};
WTSP.gui = WTSP.gui||{};
WTSP.gui.util = WTSP.gui.util||{};
WTSP.gui.util = (function() {
	var breakInterval = 5,
		replacer = function(match, offset, str) {
			return match + ((offset+breakInterval < str.length) ? '&shy;' : '');
		},
		regex = new RegExp("[^\\s-]{"+breakInterval+"}", "g");
		hyphenate = function(input) {
			return input.replace(regex, replacer);
		}
	return {
		hyphenate: hyphenate
	};
})();