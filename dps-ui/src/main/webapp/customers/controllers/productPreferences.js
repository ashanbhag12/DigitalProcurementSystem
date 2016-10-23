angular.module('productPreferencesApp', ['angularUtils.directives.dirPagination', 'smoothScroll'])
        .controller('productPreferencesController', function ($scope, $rootScope, $timeout, getCustomersProductPreferencesService,
        		getProductPreferencesService, modifyProductPreferencesService, exportToPDF, smoothScroll, setOtherCustomerCPM) {
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
            $scope.customers; /* Object for storing customers list */
            $scope.otherCustomers = []; /* Object for storing customers list other than the selected customer */
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
            
            $scope.createOtherCustomersList = function(){
            	$scope.otherCustomers = []; /* Empty the object */
            	angular.forEach($scope.customers, function (customer) {
            		if(customer.shipmark !== $scope.customerShipmark){
            			$scope.otherCustomers.push(customer);
            		}
            	});
            };
            
            $scope.setOtherCustomerCPM = function(){
            	if ($scope.customerShipmark !== undefined && $scope.otherCustomerShipmark !== undefined) {
                	angular.element(document.querySelector('.loader')).addClass('show');
                	$scope.showSuccessBox = false;
                	/*Service call for seting CPM for Other customers */
                    setOtherCustomerCPM.query({cust1:$scope.customerShipmark,cust2:$scope.otherCustomerShipmark}, function(){/* Success callback */
                    	$timeout(function () {
                    		$scope.showSuccessBox = true;
                            $scope.successMessage = "Successfully changed CPM for other customer with shipmark : "+$scope.otherCustomerShipmark;
        				    $scope.showErrorBox = false;
                            angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
                    }, function(error){/* Error Callback */
                    	$timeout(function () {
                    		$scope.showErrorBox = true;
	                		$scope.errorMessage = "Failed to change CPM for other customer with shipmark : "+$scopeotherCustomerShipmark;
	                		$scope.showSuccessBox = false;
                            angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
                    });
                }
            }

            /* Function to search for Products */
            $scope.getProductDetails = function () {
                if ($scope.customerShipmark !== undefined) {
                	$scope.maskColumns = true; /* Hide the columns */
                	angular.element(document.querySelector('.loader')).addClass('show');
                	$scope.showSuccessBox = false;
                    /* Service Call to retrieve all products */
                    $scope.products = getProductPreferencesService.get({shipmark : $scope.customerShipmark}, function(){/* Success callback */
                    	$timeout(function () {
                            $scope.searchedResults = true;
                            angular.element(document.querySelector('.loader')).removeClass('show');
                            angular.forEach($scope.products.customerProductPrices, function (product, index) {
                        		$scope.updateProductDetails(index, product);
                        	});
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
            		$scope.updateProductDetails(index, product);
            	});
            };
            
            $scope.editProductDetails = function (index, product) {
                $scope.editProductDetailsRow[index] = true;                
                $timeout(function () {
                	angular.element(document.querySelectorAll("input[name=customerProductMarginPercentage]")[index]).focus();
                }, 100);
            };

            $scope.updateProductDetails = function (index, product) {
            	product.customerProductMargin = parseFloat(product.customerProductMarginPercentage);
            	if(product.customerProductMargin >= 0){
            		product.customerProductMargin = (1 / (1 - (Math.abs(product.customerProductMargin)/100))).toFixed(6);
        		}
            	else{
            		product.customerProductMargin = (1 - (Math.abs(product.customerProductMargin)/100)).toFixed(6);            		
            	}
            	product.calculatedCost = (product.cost * product.customerProductMargin).toFixed(6);
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
    		    response = exportToPDF.save($scope.products, function(){/* Success Callback */
    		    	$timeout(function () {
                        $scope.showSuccessBox = true;
                        $scope.successMessage = "PDF created successfully"
    				    $scope.showErrorBox = false;
                        $scope.selectedRows = [];
                        $scope.selectAll = false;
                        angular.forEach($scope.products.customerProductPrices, function (product) {
        	            	product.toExport = $scope.selectAll;
        	            });
    				    angular.element(document.querySelector('.loader')).removeClass('show');
    				    smoothScroll(document.getElementById("editProductPage")); /* Scroll to the form */
                    }, 500);
    		    }, function(error){/* Error Callback */
    		    	$scope.showErrorBox = true; 
    		    	$scope.errorMessage = "PDF could not be created. Please try again after some time."
    		    	$scope.showSuccessBox = false;
    		    	$timeout(function () {
                        angular.element(document.querySelector('.loader')).removeClass('show');
                        smoothScroll(document.getElementById("editProductPage")); /* Scroll to the form */
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
                    		$scope.products = getProductPreferencesService.get({shipmark : $scope.customerShipmark}, 
                				function(){/* Success callback */
	                    			angular.forEach($scope.products.customerProductPrices, function (product, index) {
	                            		$scope.updateProductDetails(index, product);
	                            	});
                    		});
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