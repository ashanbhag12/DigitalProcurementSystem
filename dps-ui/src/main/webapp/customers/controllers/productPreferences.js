angular.module('productPreferencesApp', ['angularUtils.directives.dirPagination'])
        .controller('productPreferencesController', function ($scope, $rootScope, $timeout, getCustomersProductPreferencesService,
        		getProductPreferencesService, modifyProductPreferencesService, exportToPDF) {
            /* Initialize the page variables */
            $scope.showSuccessBox = false; /* Hide the error messages */
            $scope.showErrorBox = false; /* Hide the success messages */
            $scope.successMessage = "";
            $scope.errorMessage = "";
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.maskColumns = true; /* Hide the columns */
            $scope.products = [];     
            $scope.selectedRows = []; /* Array for toggleAll function */
            $scope.selectAll = false; /* Set toggle all to false */
            $scope.pdfDisabled = true; /* Disable the PDF button */
            $scope.products = [];           

            $scope.editProductDetailsRow = {}; /* Object for inline editing in Order Summary table */

            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {
                /* WS call to fetch all customers*/
            	getCustomersProductPreferencesService.query().$promise.then(function(data) {
                	$scope.customers = data;
                });
            	
            	/* Set variable for inline editing in order summary table */
                for (var i = 0; i < $scope.products.length; i++) {
                    $scope.editProductDetailsRow[i] = false;
                }

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
            		product.cost = (product.productPrice * product.productMargin * $scope.products.additionalCustomerMargin * product.customerProductMargin).toFixed(3);
                    $scope.editProductDetailsRow[index] = false;
                });
            };

            /* Function to search for Products */
            $scope.getProductDetails = function () {
                if ($scope.customerShipmark !== undefined) {
                	angular.element(document.querySelector('.loader')).addClass('show');                    
                    /* Service Call to retrieve all products */
                    $scope.products = getProductPreferencesService.get({shipmark : $scope.customerShipmark}, function(){/* Success callback */
                    	$timeout(function () {
                            $scope.searchedResults = true;
                            angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
                    }, function(error){/* Error Callback */
                    	$timeout(function () {
                    		$scope.showErrorBox = true;
	                		$scope.errorMessage = "Product margin for Customer could not be retrieved. Please try after some time";
                            angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
                    });
                }
            };
            
            /* Function to edit all the customer product margins */
            $scope.editAll = function () {
            	for (var i = 0; i < $scope.products.customerProductPrices.length; i++) {
            		$scope.editProductDetailsRow[i] = true;
            	}
            };

            /* Function to save all the customer product margins */
            $scope.saveAll = function () {
            	angular.forEach($scope.products.customerProductPrices, function (product, index) {
            		product.cost = (product.productPrice * product.productMargin * $scope.products.additionalCustomerMargin * product.customerProductMargin).toFixed(3);
            		$scope.editProductDetailsRow[index] = false;
            	});
            };
            
            $scope.editProductDetails = function (index, product) {
                $scope.editProductDetailsRow[index] = true;
            };

            $scope.updateProductDetails = function (index, product) {
                product.cost = (product.productPrice * product.productMargin * $scope.products.additionalCustomerMargin * product.customerProductMargin).toFixed(3);
                $scope.editProductDetailsRow[index] = false;                
            };
            
            /* Function to select/unselect all the Products */
    	    $scope.toggleAll = function () {
    	        if ($scope.selectAll) {
    	            $scope.selectAll = true;
    	            $scope.pdfDisabled = false;
    	            $scope.selectedRows = [];
    	            angular.forEach($scope.products.customerProductPrices, function (product) {
    	            	product.toExport = $scope.selectAll;
    	                $scope.selectedRows.push(1);
    	            });
    	        }
    	        else {
    	            $scope.selectAll = false;
    	            $scope.pdfDisabled = true;
    	            angular.forEach($scope.products.customerProductPrices, function (product) {
    	            	product.toExport = $scope.selectAll;
    	                $scope.selectedRows = [];
    	            });
    	        }
    	    };
    	
    	    /* Function to select/unselect the Product */
    	    $scope.toggle = function (element) {
    	        if (element.toExport) {
    	            $scope.selectedRows.push(1);
    	            $scope.pdfDisabled = false;
    	        }
    	        else {
    	            $scope.selectedRows.pop();
    	        }
    	        if ($scope.products.customerProductPrices.length === $scope.selectedRows.length) {
    	            $scope.selectAll = true;
    	        }
    	        else {
    	            $scope.selectAll = false;
    	        }
    	        if ($scope.selectedRows.length === 0) {
    	        	$scope.pdfDisabled = true;
    	        }
    	        else{
    	            $scope.pdfDisabled = false;
    	        }
    	    };
            
            /* Function to export data to PDF */
            $scope.exportToPDF = function(){
            	angular.element(document.querySelector('.loader')).addClass('show'); 
    		    response = exportToPDF.save($scope.products.customerProductPrices, function(){/* Success Callback */
    		    	$timeout(function () {
                        $scope.showSuccessBox = true;
    				    $scope.showErrorBox = false;
    				    angular.element(document.querySelector('.loader')).removeClass('show');
                    }, 500);
    		    }, function(error){/* Error Callback */
    		    	$scope.showErrorBox = true; 
    		    	$scope.showSuccessBox = false;
    		    	$timeout(function () {
    		    		console.log(error);
                        angular.element(document.querySelector('.loader')).removeClass('show');
                    }, 500);
    		    });
    		  
            }

            $scope.saveProductDetails = function () {
            	angular.element(document.querySelector('.loader')).addClass('show');
                /* WS call to save the changes and update the table */                              
                modifyProductPreferencesService.save($scope.products, function(successResult) { /* Success Callback */
                		$timeout(function(){
                			$scope.showSuccessBox = true; 
                			$scope.successMessage = "Product margin for Customer updated successfully";
                    		$scope.showErrorBox = false;
                    		$scope.products = getProductPreferencesService.get({shipmark : $scope.customerShipmark});
                    		angular.element(document.querySelector('.loader')).removeClass('show');
                		}, 500);                		
                	}, function(error){/* Error Callback */
                		$timeout(function(){
	                		$scope.showSuccessBox = false; 
	                		$scope.showErrorBox = true;
	                		$scope.errorMessage = "Product margin for Customer could not be updated. Please try after some time";
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