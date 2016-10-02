/* Directive to scroll to the top of the page */

var DPSApp = angular.module('DPSApp');

DPSApp.directive('scrollToTop', function ($window) {
	return{
		restrict: "A",
		template: "<md-button class='md-fab md-mini md-primary scrollToTop' title='Scroll to top' aria-label='Scroll to top' scroll-to='DPSApp' ng-show='scrollToTop'></md-button>",
		link: function(scope, element){
			scope.scrollToTop = false; /* Hide the scroll to top icon */ 
			angular.element($window).bind("scroll", function(){
				if (this.pageYOffset >= 200) {
					scope.scrollToTop = true;
	             } else {
	            	 scope.scrollToTop = false;
	             }
				scope.$apply();
			});
		}
	}
});