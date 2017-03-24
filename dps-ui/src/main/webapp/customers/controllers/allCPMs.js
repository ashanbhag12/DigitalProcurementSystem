angular.module('allCPMsApp', ['angularUtils.directives.dirPagination', 'smoothScroll'])
        .controller('allCPMsController', function ($scope, $rootScope, $timeout, $q, getAllCPM, smoothScroll, getProductsForAllCPMs) {
            $scope.showErrorBox = false; /* Hide the Error Box */
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'shipmark'; /* Set the default sort type */
            $scope.allCPMs = []; /* Object for all CPMs of searched product */
            $scope.allProducts; /* Get all the products for auto complete */ 
            $scope.querySearch = querySearch; /* Call the querySearch function for auto complete */
            var scrollOptions = { /* Set offset to scroll to search table */
            	    offset: -175,
            	    callbackAfter: function(element) {
            	    	angular.element(document.getElementsByName("searchTableText")).focus();
            		}
            	};

            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {
            	
            	/* List of all Products */
            	getProductsForAllCPMs.query().$promise.then(function(data) {
                	$scope.allProducts = data;
                }); 
            });
            
            /* Search for products */
            function querySearch(query) {
                var results = query ? $scope.allProducts.filter(createFilterFor(query)) : $scope.allProducts;
                var deferred = $q.defer();
                $timeout(function () {
                    deferred.resolve(results);
                }, Math.random() * 800, false);
                return deferred.promise;
            }

            /* Create filter function for a query string */
            function createFilterFor(query) {
                return function filterFn(product) {
                    return (product.productCode.toLowerCase().indexOf(query.toLowerCase()) !== -1);
                };
            }
            
            /* Function to get all CPMs for searched product */
            $scope.getProductCPM = function () {   
            	if ($scope.searchedProduct !== null) {
            		angular.element(document.querySelector('.loader')).addClass('show');
                	$scope.allCPMs = getAllCPM.query({productCode:$scope.searchedProduct.productCode}, function(){/* Success Callback */
        	        	$timeout(function(){
        	        		$scope.searchedResults = true;
        	                $scope.showErrorBox = false;
        	        		angular.element(document.querySelector('.loader')).removeClass('show');
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
        });