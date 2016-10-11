angular.module('addSupplierApp', ['ngMessages'])
        .controller('addSupplierController', function ($rootScope, $scope, $timeout, addSuppliersService) {
            $scope.showSuccessBox = false; /* Hide the Success Box */
            $scope.showErrorBox = false; /* Hide the Error Box */

            $scope.supplier = {/* Supplier Object */
                name: "",
                initials: "",
                phoneNumber: "",
                emailId: ""
            };

            $scope.submitForm = function (addSupplier) {
                if (addSupplier.$valid) {
                	angular.element(document.querySelector('.loader')).addClass('show'); 
        		    response = addSuppliersService.save($scope.supplier, function(){/* Success Callback */
        		    	$timeout(function () {                            
                            $scope.reset();
                            $scope.showSuccessBox = true;
    					    $scope.showErrorBox = false;
    					    angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
        		    }, function(error){/* Error Callback */
        		    	$scope.showErrorBox = true; 
				    	$scope.showSuccessBox = false;
        		    	$timeout(function () {
                            angular.element(document.querySelector('.loader')).removeClass('show');
                        }, 500);
        		    });
                }               
            };

            $scope.reset = function () {
                $scope.supplier = {};
                $scope.addSupplier.$setPristine();
                $scope.addSupplier.name.$touched = false;
                $scope.addSupplier.initials.$touched = false;
                $scope.addSupplier.phoneNumber.$touched = false;
                $scope.addSupplier.emailId.$touched = false;
                $scope.showSuccessBox = false;
                $scope.showErrorBox = false;
            };
        });