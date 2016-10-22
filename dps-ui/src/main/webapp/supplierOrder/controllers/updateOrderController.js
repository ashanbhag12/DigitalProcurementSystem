angular.module('updateOrderApp', [])
        .controller('updateOrderController', function ($rootScope, $scope, $timeout, getUpdateSupplierOrderService, saveUpdateSupplierOrderService) {
        	$scope.showSuccessBox = false; /* Hide the Success Box */
            $scope.showErrorBox = false; /* Hide the Error Box */
            $scope.successMessage = "";
            $scope.errorMessage = "";
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.maskColumns = true; /* Hide the columns */
            $scope.isAccordionOpen = false; /* Collapse all the accordions */
            $scope.editTableRow = {}; /* Object for inline editing in Order Summary table */           
            $scope.accordionList = new Object(); /* List of Accordions */
            $scope.selectAll = []; /* model for toggleAll as per list of accordions */
            $scope.ordersData = [];
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {
            	
            	// WS to get all orders.
            	$scope.ordersData = getUpdateSupplierOrderService.query();
            	
            	/* Create array for toggleAll inside accordionList object */
                for (var i = 0; i < $scope.ordersData.length; i++) {
                	$scope.accordionList["selectedRows" + i] = [];
                	$scope.selectAll[i] = false;
                	
                	/* Set variable for inline editing in update order table */
                    for (var j = 0; j < $scope.ordersData[0].orderDetails.length; j++) {
                        $scope.editTableRow[j] = false;
                    }
                }
            });   
            
            /* Function to select/unselect all the Orders from accordions */
    	    $scope.toggleAll = function (index) {
    	        if ($scope.selectAll[index]) {
    	            $scope.selectAll[index] = true;
    	            $scope.accordionList["selectedRows" + index] = [];
    	            angular.forEach($scope.ordersData[index].orderDetails, function (order) {
    	            	order.isChecked = $scope.selectAll[index]
    	                $scope.accordionList["selectedRows" + index].push(1);
    	            });
    	        }
    	        else {
    	            $scope.selectAll[index] = false;
    	            angular.forEach($scope.ordersData[index].orderDetails, function (order) {
    	            	order.isChecked = $scope.selectAll[index]
    	                $scope.accordionList["selectedRows" + index] = [];
    	            });
    	        }
    	    };
    	    
    	    /* Function to select/unselect the Order from one accordion */
    	    $scope.toggle = function (parentIndex, index, order) {
    	        if (order.isChecked) {
    	        	$scope.accordionList["selectedRows" + parentIndex].push(1);
    	        }
    	        else {
    	        	$scope.accordionList["selectedRows" + parentIndex].pop();
    	        }
    	        if ($scope.ordersData[parentIndex].orderDetails.length === $scope.accordionList["selectedRows" + parentIndex].length) {
    	            $scope.selectAll[parentIndex] = true;
    	        }
    	        else {
    	        	$scope.selectAll[parentIndex] = false;
    	        }
    	        if ( $scope.accordionList["selectedRows" + parentIndex].length === 0) {
    	        	$scope.selectAll[parentIndex] = false;
    	        }
    	    };
            
            /* Function to edit all the customer product margins */
            $scope.editAll = function () {
            	for (var i = 0; i < $scope.ordersData[0].orderDetails.length; i++) {
            		$scope.editTableRow[i] = true;
            	}
            };

            /* Function to save all the customer product margins */
            $scope.saveAll = function () {
            	for (var i = 0; i < $scope.ordersData[0].orderDetails.length; i++) {
            		$scope.updateOrderDetails(i);
            	}
            };
            
            $scope.editOrderDetails = function (index) {
                $scope.editTableRow[index] = true;                
                $timeout(function () {
                	angular.element(document.querySelectorAll("input[name=receivedQuantity]")[index]).focus();
                }, 100);
            };

            $scope.updateOrderDetails = function (index) {
                $scope.editTableRow[index] = false;                
            };
            
            $scope.setSupplierForUpdateOrderModal = function(suppData){
            	$scope.suppData = suppData;
            }
            
            $scope.updateOrder = function () {
            	saveUpdateSupplierOrderService.save($scope.ordersData, function(){ /* Success Callback */    		    	
                    $timeout(function () {
                    	$scope.ordersData = getUpdateSupplierOrderService.query();
                    }, 500);
    		    }, function(){/* Error Callback */
    		    	$timeout(function () {}, 500);
    		    });
            };
        });