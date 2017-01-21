angular.module('viewCustomerOrderApp', ['smoothScroll', 'angularUtils.directives.dirPagination'])
        .controller('viewCustomerOrderController', function ($scope, $rootScope, $timeout, smoothScroll, getCustomersService,
        		getCustomerOrderService, deleteCustomerOrderService, updateCustomerOrderService) { 
        	$scope.showSuccessBox = false; /* Hide the Success Box */
            $scope.showErrorBox = false; /* Hide the Error Box */
            $scope.successMessage = "";
            $scope.errorMessage = "";
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* Set the default sort order */
            $scope.sortType = 'productCode'; /* Set the default sort type */
            $scope.modalSortOrder = false; /* Set the default sort order in the popup*/
            $scope.modalSortType = 'productCode'; /* Set the default sort type in the popup */
            $scope.maskColumns = true; /* Hide the columns */     
            $scope.deleteDisabled = []; /* Model for deleting customer orders */
            $scope.pdfDisabled = []; /* Model for disabling invoice button */
            $scope.customers = []; /* Array of all Customers */ 
            $scope.customerOrders = []; /* Object for all customer orders */            
            $scope.isAccordionOpen = false; /* Collapse all the accordions */
            $scope.editTables = {}; /* Object for inline editing in update order table */
            $scope.accordionList = {}; /* List of Accordions */
            $scope.selectAll = []; /* Model for toggleAll as per list of accordions */
            $scope.deleteOrder; /* Object for deleted order */            
            $scope.deletedOrderIndex; /* Index for deleted order */
            $scope.updateOrder; /* Object for update order */
            $scope.updatedOrderIndex; /* Index for updated order */
            $scope.disabledUpdateBtn = true; /* Disable the Update button if orderDetails array is empty */
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {  
            	/* WS call to fetch all customers*/
            	getCustomersService.query().$promise.then(function(data) {
                	$scope.customers = data;
                });
            });
            
            /* Function to set search date to past 3/6 months */
            $scope.setSearchDate = function(monthsToAdd){
            	$scope.orderEndDate = new Date();
            	if(monthsToAdd === 3){
            		$scope.orderStartDate = new Date(new Date().setMonth(new Date().getMonth() - 3));
            	}
            	else{            		
            		$scope.orderStartDate = new Date(new Date().setMonth(new Date().getMonth() - 6));
            	}
            };
            
            /* Function to search for Products */
            $scope.getCustomerOrders = function () {
                if ($scope.customerShipmark !== undefined) {
                	angular.element(document.querySelector('.loader')).addClass('show');
                    /* Service Call to retrieve all products */
                	$scope.customerOrders = getCustomerOrderService.query({customerShipmark:$scope.customerShipmark, startDate:Date.parse($scope.orderStartDate), endDate:Date.parse($scope.orderEndDate)}, function(){/* Success Callback */
                    	$timeout(function () {
                    		$scope.searchedResults = true;      
                    		$scope.showErrorBox = false;   
                    		console.log($scope.customerOrders)
                            angular.element(document.querySelector('.loader')).removeClass('show');
                            /* Create array for toggleAll inside accordionList object */
                            for (var i = 0; i < $scope.customerOrders.length; i++) {
                            	$scope.accordionList["selectedRows" + i] = [];
                            	$scope.editTables["editTable" + i] = [];
                            	$scope.selectAll[i] = false;  
                            	$scope.pdfDisabled[i] = true;
                            	$scope.deleteDisabled[i] = true;
                            	
                            	/* Set variable for inline editing in update order table */
                                for (var j = 0; j < $scope.customerOrders[i].lineItems.length; j++) {
                                	$scope.editTables["editTable"+i][j] = false;
                                }
                            } 
                        }, 500);
                    }, function(error){/* Error Callback */
                    	$timeout(function () {
                    		$scope.showErrorBox = true;
	                		$scope.errorMessage = "Customer order could not be retrieved. Please try after some time";
                            angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
                    });
                }
            };
            
            /* Function to select/unselect all the Orders from accordions */
    	    $scope.toggleAll = function (index) {
    	        if ($scope.selectAll[index]) {
    	            $scope.selectAll[index] = true;    	            
    	            $scope.pdfDisabled[index] = false;
    	            $scope.deleteDisabled[index] = false;
    	            $scope.disabledUpdateBtn = false;
    	            $scope.accordionList["selectedRows" + index] = [];
    	            angular.forEach($scope.customerOrders[index].lineItems, function (product) {
    	            	product.selected = $scope.selectAll[index]
    	                $scope.accordionList["selectedRows" + index].push(1);
    	            });
    	        }
    	        else {
    	            $scope.selectAll[index] = false;
    	            $scope.pdfDisabled[index] = true;
    	            $scope.deleteDisabled[index] = true;
    	            $scope.disabledUpdateBtn = true;
    	            angular.forEach($scope.customerOrders[index].lineItems, function (product) {
    	            	product.selected = $scope.selectAll[index]
    	                $scope.accordionList["selectedRows" + index] = [];
    	            });
    	        }
    	    };
            
            /* Function to select/unselect the Order from one accordion */
    	    $scope.toggle = function (parentIndex, index, product) {
    	        if (product.selected) {
    	        	$scope.accordionList["selectedRows" + parentIndex].push(1);
    	        	$scope.disabledUpdateBtn = false;
    	        }
    	        else {
    	        	$scope.accordionList["selectedRows" + parentIndex].pop();
    	        }
    	        if ($scope.customerOrders[parentIndex].lineItems.length === $scope.accordionList["selectedRows" + parentIndex].length) {
    	            $scope.selectAll[parentIndex] = true;
    	            $scope.pdfDisabled[parentIndex] = false;
    	            $scope.deleteDisabled[parentIndex] = false;
    	            $scope.disabledUpdateBtn = true;
    	        }
    	        else {
    	        	$scope.selectAll[parentIndex] = false;
    	        	$scope.pdfDisabled[parentIndex] = true;
    	        	$scope.deleteDisabled[parentIndex] = true;
    	        }
    	        if ( $scope.accordionList["selectedRows" + parentIndex].length === 0) {
    	        	$scope.selectAll[parentIndex] = false;
    	        	$scope.pdfDisabled[parentIndex] = true;
    	        	$scope.deleteDisabled[parentIndex] = true;
    	        	$scope.disabledUpdateBtn = true;
    	        }
    	        else{
    	        	$scope.pdfDisabled[parentIndex] = false;
    	        	$scope.deleteDisabled[parentIndex] = false;
    	        	$scope.disabledUpdateBtn = false;
    	        }
    	    };
    	    
            /* Function to edit all the customer product margins */
            $scope.editAll = function (parentIndex) {
            	for (var i = 0; i < $scope.customerOrders[parentIndex].lineItems.length; i++) {
            		$scope.editOrderDetails(parentIndex, i);
            	}
            };

            /* Function to save all the customer product margins */
            $scope.saveAll = function (parentIndex) {
            	for (var i = 0; i < $scope.customerOrders[parentIndex].lineItems.length; i++) {
            		$scope.updateOrderDetails(parentIndex, i);
            	}
            };
            
            $scope.editOrderDetails = function (parentIndex, index) {
            	$scope.editTables["editTable"+parentIndex][index] = true;
                $timeout(function () {
                	angular.element(document.querySelectorAll(".customTable")[parentIndex]).find("input").eq(index).focus();
                }, 100);
            };

            $scope.updateOrderDetails = function (parentIndex, index) {
            	$scope.editTables["editTable"+parentIndex][index] = false;           
            };
    	    
    	    $scope.setDeleteCustomerOrderModal = function(index, order){
            	$scope.deleteOrder = order;
            	$scope.deletedOrderIndex = index;
            };
            
            /* Function to deleted the selected products */
            $scope.deleteCustomerOrder = function(){
            	angular.element(document.querySelector('.loader')).addClass('show');
            	deleteCustomerOrderService.save($scope.deleteOrder, function(){ /* Success Callback */    		    	
                    $timeout(function () {
                    	$scope.customerOrders = getCustomerOrderService.query({customerShipmark:$scope.customerShipmark, startDate:Date.parse($scope.orderStartDate), endDate:Date.parse($scope.orderEndDate)}, function(){
                    		$timeout(function () {/* Open the updated order accordion */
                            	angular.element(document.querySelectorAll(".md-accordion")[$scope.deletedOrderIndex]).find("md-toolbar").triggerHandler("click");
                            }, 100);                    		
                    	});
                    	$scope.showSuccessBox = true;
                    	$scope.successMessage = "Customer order deleted successfully"
    				    $scope.showErrorBox = false;
                    	$scope.selectAll[$scope.deletedOrderIndex] = false;
                        for (var i = 0; i < $scope.deleteOrder.lineItems.length; i++) {
                            $scope.editTables["editTable" + $scope.deletedOrderIndex][i] = false;
                        }                                                 
                        angular.element(document.querySelector('.loader')).removeClass('show');
                    }, 500);
    		    }, function(){/* Error Callback */
    		    	$timeout(function () {
    		    		$scope.showErrorBox = true; 
        		    	$scope.errorMessage = "Customer order could not be deleted. Please try again after some time."
        		    	$scope.showSuccessBox = false;
        		    	angular.element(document.querySelector('.loader')).removeClass('show');
    		    	}, 500);
    		    });
            };
    	    
    	    /* Function to generate invoice without Total Cost (TC) */
    	    $scope.generateInvoiceWithoutTC = function(){
    	    	
    	    };
    	    
    	    /* Function to generate invoice */
	    	$scope.generateInvoice = function(){
    	    	
    	    };
    	    
    	    $scope.setUpdatedCustomerOrderModal = function(index, order){
    	    	$scope.updateOrder = order;
            	$scope.updatedOrderIndex = index;
            };
    	    
    	    /* Function to update the selected products */
            $scope.updateCustomerOrder = function(){ 
            	angular.element(document.querySelector('.loader')).addClass('show');
            	updateCustomerOrderService.save($scope.updateOrder, function(){ /* Success Callback */    		    	
                    $timeout(function () {
                    	$scope.customerOrders = getCustomerOrderService.query({customerShipmark:$scope.customerShipmark, startDate:Date.parse($scope.orderStartDate), endDate:Date.parse($scope.orderEndDate)}, function(){
                    		$timeout(function () {/* Open the updated order accordion */
                            	angular.element(document.querySelectorAll(".md-accordion")[$scope.updatedOrderIndex]).find("md-toolbar").triggerHandler("click");
                            }, 100);                    		
                    	});
                    	$scope.showSuccessBox = true;
                    	$scope.successMessage = "Order of Customer " + $scope.updateOrder.shipmark + " updated successfully"
    				    $scope.showErrorBox = false;  
                    	$scope.pdfDisabled[$scope.updatedOrderIndex] = true;
                    	$scope.selectAll[$scope.updatedOrderIndex] = false;
                        for (var i = 0; i < $scope.updateOrder.lineItems.length; i++) {
                            $scope.editTables["editTable" + $scope.updatedOrderIndex][i] = false;
                        }                           
                        angular.element(document.querySelector('.loader')).removeClass('show');
                    }, 500);
    		    }, function(){/* Error Callback */
    		    	$timeout(function () {
    		    		$scope.showErrorBox = true; 
        		    	$scope.errorMessage = "Customer order could not be updated. Please try again after some time."
        		    	$scope.showSuccessBox = false;
        		    	angular.element(document.querySelector('.loader')).removeClass('show');
    		    	}, 500);
    		    });
            };
    	    
        }); 