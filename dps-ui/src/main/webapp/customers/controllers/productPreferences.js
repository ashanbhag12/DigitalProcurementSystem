angular.module('productPreferencesApp', ['angularUtils.directives.dirPagination'])
        .controller('productPreferencesController', function ($scope, $rootScope, $timeout, getCustomersProductPreferencesService,
        		getProductPreferencesService, modifyProductPreferencesService ) {
            /* Initialize the page variables */
            $scope.showSuccessBox = false; /* Hide the error messages */
            $scope.showErrorBox = false; /* Hide the success messages */
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.maskColumns = true; /* Hide the columns */
            $scope.products = [];           

            $scope.editProductDetailsRow = {}; /* Object for inline editing in Order Summary table */

            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {
                /* WS call to fetch all customers*/
            	getCustomersProductPreferencesService.query().$promise.then(function(data) {
                	$scope.customers = data;
                });
            });
            
            /* Function to edit all the customer product margins */
            $scope.editAll = function () {
            	for (var i = 0; i < $scope.products.customerProductPrices.length; i++) {
                    $scope.editProductDetailsRow[i] = true;
                }
            };
            
            /* Function to save all the customer product margins */
            $scope.saveAll = function () {
            	angular.forEach($scope.products.customerProductPrices, function (product, index) {
            		product.cost = (product.productPrice * product.productMargin * $scope.products.additionalCustomerMargin * product.customerProductMargin).toFixed(2);
                    $scope.editProductDetailsRow[index] = false;
                });
            };

            /* Function to search for Products */
            $scope.getProductDetails = function () {
                if ($scope.customerShipmark !== undefined) {
                    $scope.searchedResults = true;
                    /* Service Call to retrieve all products */
                    $scope.products = getProductPreferencesService.get({shipmark : $scope.customerShipmark})
                }
            };

            /* Set variable for inline editing in order summary table */
            for (var i = 0; i < $scope.products.length; i++) {
                $scope.editProductDetailsRow[i] = false;
            }

            $scope.editProductDetails = function (index, product) {
                $scope.editProductDetailsRow[index] = true;
            };

            $scope.updateProductDetails = function (index, product) {
                product.cost = (product.productPrice * product.productMargin * $scope.products.additionalCustomerMargin * product.customerProductMargin).toFixed(2);
                $scope.editProductDetailsRow[index] = false;                
            };

            $scope.saveProductDetails = function () {
            	angular.element(document.querySelector('.loader')).addClass('show');
                /* WS call to save the changes and update the table */                              
                modifyProductPreferencesService.save($scope.products, function(successResult) { /* Success Callback */
                		$timeout(function(){
                			$scope.showSuccessBox = true; 
                    		$scope.showErrorBox = false;
                    		$scope.products = getProductPreferencesService.get({shipmark : $scope.customerShipmark});
                    		angular.element(document.querySelector('.loader')).removeClass('show');
                		}, 500);                		
                	}, function(error){/* Error Callback */
                		$timeout(function(){
	                		$scope.showSuccessBox = false; 
	                		$scope.showErrorBox = true;
	                		angular.element(document.querySelector('.loader')).removeClass('show');
                		}, 500);
                	});
            };

            $scope.cancelChanges = function () {/* Cancel the changes and hide the table */
                $scope.showSuccessBox = false;
                $scope.searchedResults = false;
                $scope.customerShipmark = "";
            };
        });