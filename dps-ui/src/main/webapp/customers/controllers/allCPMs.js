angular.module('allCPMsApp', ['angularUtils.directives.dirPagination', 'smoothScroll'])
        .controller('allCPMsController', function ($scope, $rootScope, $timeout, getAllCPM, smoothScroll) {
            $scope.showErrorBox = false; /* Hide the Error Box */
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'shipmark'; /* Set the default sort type */
            $scope.allCPMs = []; /* Object for all CPMs of searched product */
            var scrollOptions = { /* Set offset to scroll to search table */
            	    offset: -175,
            	};

            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {});
            
            /* Function to get all CPMs for searched product */
            $scope.getProductCPM = function () {   
            	if($scope.searchProductCode !== undefined && $scope.searchProductCode !== ""){
            		angular.element(document.querySelector('.loader')).addClass('show');
                	$scope.allCPMs = getAllCPM.query({productCode:$scope.searchProductCode}, function(){/* Success Callback */
        	        	$timeout(function(){
        	        		$scope.searchedResults = true;
        	                $scope.showErrorBox = false;
        	        		angular.element(document.querySelector('.loader')).removeClass('show');
        	        		console.log($scope.allCPMs);
        	        		smoothScroll(document.getElementsByClassName("searchedResults"), scrollOptions); /* Scroll to the table */
        	        	}, 500);
        	        }, function(){ /* Error Callback */
        	        	$timeout(function(){
        	        		$scope.searchedResults = false;
        	                $scope.showErrorBox = true;
        	        		angular.element(document.querySelector('.loader')).removeClass('show');
        	        	}, 500);
        	        });
            	}            	
            };
            
            /* Trigger getProductCPM() function on "enter" key press in search product field */
            $scope.triggerSearchProduct = function(event){
            	if(event.which === 13) {
                    event.preventDefault();
                    $scope.getProductCPM();
                }
            };
        });