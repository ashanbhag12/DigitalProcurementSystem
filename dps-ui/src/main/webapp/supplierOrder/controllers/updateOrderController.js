angular.module('updateOrderApp', [])
        .controller('updateOrderController', function ($rootScope, $scope, $timeout) {
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
            $scope.accordianData = [ /* Object given by WS  */
                {
                    "heading" : "Order 1",
                    "orderId":'ord1',
                    "suplierInitials":'TT',
                    "orderDetails":[
                            {
								"productCode":"P101",
								"shipmark":"C101",
								"orderedQuantity":'10',
								"availableQuantity":'5',
								"receivedQuantity":5,								
							},
							{
								"productCode":"P102",
								"shipmark":"C102",
								"orderedQuantity":'10',
								"availableQuantity":'5',
								"receivedQuantity":5,								
							},
							{
								"productCode":"P103",
								"shipmark":"C103",
								"orderedQuantity":'10',
								"availableQuantity":'5',
								"receivedQuantity":5,								
							},
                    ]
                },
                {
                	"heading" : "Order 2",
                    "orderId":'ord2',
                    "suplierInitials":'GS',
                    "orderDetails":[
                            {
								"productCode":"P101",
								"shipmark":"C101",
								"orderedQuantity":'10',
								"availableQuantity":'5',
								"receivedQuantity":5,								
							},
							{
								"productCode":"P102",
								"shipmark":"C102",
								"orderedQuantity":'10',
								"availableQuantity":'5',
								"receivedQuantity":5,								
							},
							{
								"productCode":"P103",
								"shipmark":"C103",
								"orderedQuantity":'10',
								"availableQuantity":'5',
								"receivedQuantity":5,								
							},
                    ]
                }
            ];
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {
            	
            	/* Create array for toggleAll inside accordionList object */
                for (var i = 0; i < $scope.accordianData.length; i++) {
                	$scope.accordionList["selectedRows" + i] = [];
                	$scope.selectAll[i] = false;
                	
                	/* Set variable for inline editing in update order table */
                    for (var j = 0; j < $scope.accordianData[0].orderDetails.length; j++) {
                        $scope.editTableRow[j] = false;
                    }
                }
            });   
            
            /* Function to select/unselect all the Orders from accordions */
    	    $scope.toggleAll = function (index) {
    	        if ($scope.selectAll[index]) {
    	            $scope.selectAll[index] = true;
    	            $scope.accordionList["selectedRows" + index] = [];
    	            angular.forEach($scope.accordianData[index].orderDetails, function (order) {
    	            	order.isChecked = $scope.selectAll[index]
    	                $scope.accordionList["selectedRows" + index].push(1);
    	            });
    	        }
    	        else {
    	            $scope.selectAll[index] = false;
    	            angular.forEach($scope.accordianData[index].orderDetails, function (order) {
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
    	        if ($scope.accordianData[parentIndex].orderDetails.length === $scope.accordionList["selectedRows" + parentIndex].length) {
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
            	for (var i = 0; i < $scope.accordianData[0].orderDetails.length; i++) {
            		$scope.editTableRow[i] = true;
            	}
            };

            /* Function to save all the customer product margins */
            $scope.saveAll = function () {
            	for (var i = 0; i < $scope.accordianData[0].orderDetails.length; i++) {
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
        });