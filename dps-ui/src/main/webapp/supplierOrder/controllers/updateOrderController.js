angular.module('updateOrderApp', ['angularUtils.directives.dirPagination'])
        .controller('updateOrderController', function ($rootScope, $scope, $timeout, getUpdateSupplierOrderService, saveUpdateSupplierOrderService) {
        	$scope.showSuccessBox = false; /* Hide the Success Box */
            $scope.showErrorBox = false; /* Hide the Error Box */
            $scope.successMessage = "";
            $scope.errorMessage = "";
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.modalSortOrder = false; /* Set the default sort order in the popup*/
            $scope.modalSortType = 'productCode'; /* Set the default sort type in the popup */
            $scope.maskColumns = true; /* Hide the columns */
            $scope.isAccordionOpen = false; /* Collapse all the accordions */
            $scope.editTables = {}; /* Object for inline editing in update order table */
            $scope.accordionList = {}; /* List of Accordions */
            $scope.selectAll = []; /* model for toggleAll as per list of accordions */
            $scope.ordersData = [];
            $scope.updatedOrder; /* Object for updated order */
            $scope.updatedOrderIndex; /* Index for updated order */
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {            	
            	/* WS to get all orders */
            	getUpdateSupplierOrderService.query().$promise.then(function(data) {
            		$scope.ordersData = data;
            		
            		/* Create array for toggleAll inside accordionList object */
                    for (var i = 0; i < $scope.ordersData.length; i++) {
                    	$scope.accordionList["selectedRows" + i] = [];
                    	$scope.editTables["editTable" + i] = [];
                    	$scope.selectAll[i] = false;                	
                    	
                    	/* Set variable for inline editing in update order table */
                        for (var j = 0; j < $scope.ordersData[i].orderDetails.length; j++) {
                        	$scope.editTables["editTable"+i][j] = false;
                        }
                    }
                });
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
            $scope.editAll = function (parentIndex) {
            	for (var i = 0; i < $scope.ordersData[parentIndex].orderDetails.length; i++) {
            		$scope.editOrderDetails(parentIndex, i);
            	}
            };

            /* Function to save all the customer product margins */
            $scope.saveAll = function (parentIndex) {
            	for (var i = 0; i < $scope.ordersData[parentIndex].orderDetails.length; i++) {
            		$scope.updateOrderDetails(parentIndex, i);
            	}
            };
            
            $scope.editOrderDetails = function (parentIndex, index) {
            	$scope.editTables["editTable"+parentIndex][index] = true;
                $timeout(function () {
                	angular.element(document.querySelectorAll("input[name=receivedQuantity]")[parentIndex + index]).focus();
                }, 100);
            };

            $scope.updateOrderDetails = function (parentIndex, index) {
            	$scope.editTables["editTable"+parentIndex][index] = false;           
            };
            
            $scope.setSupplierForUpdateOrderModal = function(index, data){
            	$scope.updatedOrder = data;
            	$scope.updatedOrderIndex = index;
            };
            
            $scope.cancelUpdate = function(index){
            	$scope.selectAll[index] = false;
	            angular.forEach($scope.ordersData[index].orderDetails, function (order) {
	            	order.isChecked = $scope.selectAll[index]
	                $scope.accordionList["selectedRows" + index] = [];
	            });
            };
            
            $scope.updateOrder = function () {
            	angular.element(document.querySelector('.loader')).addClass('show');
            	saveUpdateSupplierOrderService.save($scope.ordersData, function(){ /* Success Callback */    		    	
                    $timeout(function () {
                    	$scope.ordersData = getUpdateSupplierOrderService.query();
                    	$scope.showSuccessBox = true;
                        $scope.successMessage = "Order updated successfully"
    				    $scope.showErrorBox = false;
                    	$scope.selectAll[$scope.updatedOrderIndex] = false;
                        for (var i = 0; i < $scope.updatedOrder.orderDetails.length; i++) {
                            $scope.editTables["editTable" + $scope.updatedOrderIndex][i] = false;
                        } 
                        angular.element(document.querySelector('.loader')).removeClass('show');
                    }, 500);
    		    }, function(){/* Error Callback */
    		    	$timeout(function () {
    		    		$scope.showErrorBox = true; 
        		    	$scope.errorMessage = "Order could not be updated. Please try again after some time."
        		    	$scope.showSuccessBox = false;
        		    	angular.element(document.querySelector('.loader')).removeClass('show');
    		    	}, 500);
    		    });
            };
        });