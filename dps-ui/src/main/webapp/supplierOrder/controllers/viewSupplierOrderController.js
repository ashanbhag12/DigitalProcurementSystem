angular.module('viewSupplierOrderApp', ['smoothScroll', 'angularUtils.directives.dirPagination'])
        .controller('viewSupplierOrderController', function ($scope, $rootScope, $timeout, smoothScroll, getCustomersProductPreferencesService,
        		getProductPreferencesService, modifyProductPreferencesService) {
        	$scope.showSuccessBox = false; /* Hide the Success Box */
            $scope.showErrorBox = false; /* Hide the Error Box */
            $scope.successMessage = "";
            $scope.errorMessage = "";
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.maskColumns = true; /* Hide the columns */    
            $scope.selectedRows = []; /* Array for toggleAll function */
            $scope.selectAll = false; /* Set toggle all to false */
            $scope.excelDisabled = true; /* Disable the Excel button */
            $scope.products = [];  
            $scope.customers; /* Object for storing customers list */
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {  
            	/* WS call to fetch all customers*/
            	getCustomersProductPreferencesService.query().$promise.then(function(data) {
                	$scope.customers = data;
                });
            });
            
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
            
            /* Function to select/unselect all the Products */
    	    $scope.toggleAll = function () {
    	        if ($scope.selectAll) {
    	            $scope.selectAll = true;
    	            $scope.excelDisabled = false;
    	            $scope.selectedRows = [];
    	            angular.forEach($scope.products.customerProductPrices, function (product) {
    	            	product.toExport = $scope.selectAll;
    	                $scope.selectedRows.push(1);
    	            });
    	        }
    	        else {
    	            $scope.selectAll = false;
    	            $scope.excelDisabled = true;
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
    	            $scope.excelDisabled = false;
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
    	        	$scope.excelDisabled = true;
    	        }
    	        else{
    	            $scope.excelDisabled = false;
    	        }
    	    };
            
        }); 