angular.module('viewSupplierOrderApp', ['smoothScroll', 'angularUtils.directives.dirPagination'])
        .controller('viewSupplierOrderController', function ($scope, $rootScope, $timeout, smoothScroll, getSupplierOrderService, getSuppliersService) {
        	$scope.showSuccessBox = false; /* Hide the Success Box */
            $scope.showErrorBox = false; /* Hide the Error Box */
            $scope.successMessage = "";
            $scope.errorMessage = "";
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.maskColumns = true; /* Hide the columns */ 
            $scope.isAccordionOpen = false; /* Collapse all the accordions */
            $scope.editTables = {}; /* Object for inline editing in update order table */
            $scope.accordionList = {}; /* List of Accordions */
            $scope.selectAll = []; /* model for toggleAll as per list of accordions */
            $scope.selectedRows = []; /* Array for toggleAll function */
            $scope.excelDisabled = true; /* Disable the Excel button */
            $scope.suppliers = []; /* Array of all Suppliers */ 
            $scope.supplierOrders = []; /* Object for all supplier orders */
            $scope.selectedOrder; /* Object for selected order */
            $scope.selectedOrderIndex; /* Index for selected order */
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {  
            	/* WS call to fetch all suppliers */
            	getSuppliersService.query().$promise.then(function(data) {
            		$scope.suppliers = data;
            		console.log($scope.suppliers)
            	});
            });
            
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
            
            /* Function to search for Products */
            $scope.getSupplierOrders = function () {
                console.log(Date.parse($scope.orderStartDate))
                console.log(Date.parse($scope.orderEndDate))
                if ($scope.supplierInitials !== undefined) {
                	angular.element(document.querySelector('.loader')).addClass('show');
                    /* Service Call to retrieve all products */
                	$scope.supplierOrders = getSupplierOrderService.query({supplierInitials:$scope.supplierInitials, startDate:Date.parse($scope.orderStartDate), endDate:Date.parse($scope.orderEndDate)}, function(){/* Success Callback */
                    	$timeout(function () {
                    		console.log($scope.supplierOrders)
                            $scope.searchedResults = true;
                            angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
                    }, function(error){/* Error Callback */
                    	$timeout(function () {
                    		console.log(error)
                    		$scope.showErrorBox = true;
	                		$scope.errorMessage = "Supplier order could not be retrieved. Please try after some time";
                            angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
                    });
                }
            };
            
        }); 