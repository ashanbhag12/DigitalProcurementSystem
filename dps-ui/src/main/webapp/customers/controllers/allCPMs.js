angular.module('allCPMsApp', ['angularUtils.directives.dirPagination', 'smoothScroll'])
        .controller('allCPMsController', function ($scope, $rootScope, $timeout, getCustomersProductPreferencesService,
        		getProductPreferencesService, getProductsService, smoothScroll) {
            $scope.showErrorBox = false; /* Hide the Error Box */
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.products = [];

            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {});
            
            /* Function to search for Products */
            $scope.searchProduct = function () {    
            	angular.element(document.querySelector('.loader')).addClass('show');
            	$scope.products = getProductsService.query({code:$scope.searchProductCode}, function(){/* Success Callback */
    	        	$timeout(function(){
    	        		$scope.searchedResults = true;
    	                $scope.showErrorBox = false;
    	        		angular.element(document.querySelector('.loader')).removeClass('show');
    	        	}, 500);
    	        }, function(){ /* Error Callback */
    	        	$timeout(function(){
    	        		$scope.searchedResults = false;
    	                $scope.showErrorBox = true;
    	        		angular.element(document.querySelector('.loader')).removeClass('show');
    	        	}, 500);
    	        });
            };
            
            /* Trigger searchProduct() function on "enter" key press in search product field */
            $scope.triggerSearchProduct = function(event){
            	if(event.which === 13) {
                    event.preventDefault();
                    $scope.searchProduct();
                }
            };
        });