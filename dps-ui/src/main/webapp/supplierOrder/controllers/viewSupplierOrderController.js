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
            $scope.excelDisabled = []; /* Disable the Excel button */
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
            
            /* Function to select/unselect all the Orders from accordions */
    	    $scope.toggleAll = function (index) {
    	        if ($scope.selectAll[index]) {
    	            $scope.selectAll[index] = true;
    	            $scope.excelDisabled[index] = false;
    	            $scope.accordionList["selectedRows" + index] = [];
    	            angular.forEach($scope.supplierOrders[index].details, function (order) {
    	            	order.isChecked = $scope.selectAll[index]
    	                $scope.accordionList["selectedRows" + index].push(1);
    	            });
    	        }
    	        else {
    	            $scope.selectAll[index] = false;
    	            $scope.excelDisabled[index] = true;
    	            angular.forEach($scope.supplierOrders[index].details, function (order) {
    	            	order.isChecked = $scope.selectAll[index]
    	                $scope.accordionList["selectedRows" + index] = [];
    	            });
    	        }
    	    };
    	    
    	    /* Function to select/unselect the Order from one accordion */
    	    $scope.toggle = function (parentIndex, index, product) {
    	        if (product.isChecked) {
    	        	$scope.accordionList["selectedRows" + parentIndex].push(1);
    	        	$scope.excelDisabled[parentIndex] = false;
    	        }
    	        else {
    	        	$scope.accordionList["selectedRows" + parentIndex].pop();
    	        }
    	        if ($scope.supplierOrders[parentIndex].details.length === $scope.accordionList["selectedRows" + parentIndex].length) {
    	            $scope.selectAll[parentIndex] = true;
    	            $scope.excelDisabled[parentIndex] = true;
    	        }
    	        else {
    	        	$scope.selectAll[parentIndex] = false;
    	        }
    	        if ( $scope.accordionList["selectedRows" + parentIndex].length === 0) {
    	        	$scope.selectAll[parentIndex] = false;
    	        	$scope.excelDisabled[parentIndex] = true;
    	        }
    	        else{
    	        	$scope.excelDisabled[parentIndex] = false;
    	        }
    	    };
            
            /* Function to search for Products */
            $scope.getSupplierOrders = function () {
                if ($scope.supplierInitials !== undefined) {
                	angular.element(document.querySelector('.loader')).addClass('show');
                    /* Service Call to retrieve all products */
                	$scope.supplierOrders = getSupplierOrderService.query({supplierInitials:$scope.supplierInitials, startDate:Date.parse($scope.orderStartDate), endDate:Date.parse($scope.orderEndDate)}, function(){/* Success Callback */
                    	$timeout(function () {
                    		console.log($scope.supplierOrders)
                    		$scope.searchedResults = true;
                    		/* Create array for toggleAll inside accordionList object */
		                    for (var i = 0; i < $scope.supplierOrders.length; i++) {
		                    	$scope.accordionList["selectedRows" + i] = [];
		                    	$scope.editTables["editTable" + i] = [];
		                    	$scope.selectAll[i] = false;                	
		                    	$scope.excelDisabled[i] = true;
		                    	/* Set variable for inline editing in update order table */
		                        for (var j = 0; j < $scope.supplierOrders[i].details.length; j++) {
		                        	$scope.editTables["editTable"+i][j] = false;
		                        }
		                    }                            
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