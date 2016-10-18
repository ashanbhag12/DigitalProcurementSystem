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
            	
            	/* Set variable for inline editing in order summary table */
                for (var i = 0; i < $scope.accordianData[0].orderDetails.length; i++) {
                    $scope.editTableRow[i] = false;
                }
            });   
            
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