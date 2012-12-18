'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('Stundenplan', function() {

  it('should get main side on index.html', function() {
    browser().navigateTo('../../angular/index.html');
    expect(browser().location().url()).toBe('');
  });
  
  describe('Sidebar Element', function() {
	  
	  
	  it('should have a Tab-Bar with 3 Elements (Dozenten, Räume, Gruppen) ', function() {
		  browser().navigateTo('../../angular/index.html');
		  expect(repeater('.list .professors li').count()).toBe(55);
		  element('.typeselection #radio2').click();
		  expect(repeater('.list .groups li').count()).toBe(37);
		  element('.typeselection #radio3').click();
		  expect(repeater('.list .rooms li').count()).toBe(29);
	  });
	  it('should have a sidebar with 55 Dozenten and a functioning filter', function() {
		  browser().navigateTo('../../angular/index.html');
		  expect(repeater('.list .professors li').count()).toBe(55);
		  input('query').enter('bo');
		  expect(repeater('.list li').count()).toBe(2);
		  input('query').enter('anlauf');
		  expect(repeater('.list li').count()).toBe(1);
		  
	  });
	  it('should have a sidebar with 37 Gruppen and a functioning filter', function() {
		  browser().navigateTo('../../angular/index.html');
		  element('.typeselection #radio2').click();
		  expect(repeater('.list .groups li').count()).toBe(37);
		  input('query').enter('GO');
		  expect(repeater('.list .groups li').count()).toBe(4);
	  });
	  it('should have a sidebar with 29 Räume and a functioning filter', function() {
		  browser().navigateTo('../../angular/index.html');
		  element('.typeselection #radio3').click();
		  expect(repeater('.list .rooms li').count()).toBe(29);
		  input('query').enter('R0.005');
		  expect(repeater('.list .rooms li').count()).toBe(1);
	  });
	  
  });
  describe('Table Element', function() {
	  it('should have a Days-Hours-Table after selecting a Dozent) ', function() {
		  browser().navigateTo('../../angular/index.html');
		  element('#Eich-Soellner_Edda').click();
		  pause();
		  
		  
	  });
  });
});
