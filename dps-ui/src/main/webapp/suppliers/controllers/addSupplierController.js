angular.module('addSupplierApp', ['ngMessages'])
        .controller('addSupplierController', function ($scope, addSuppliersService) {
            $scope.showSuccessBox = false;
            $scope.showErrorBox = false;

            $scope.supplier = {
                name: "",
                initials: "",
                phoneNumber: "",
                emailId: ""
            };

            $scope.submitForm = function (addSupplier) {
                if (addSupplier.$valid) {
                	$scope.supplierJson = angular.toJson($scope.supplier);
        		    //alert($scope.supplierJson);
        		    response = addSuppliersService.save($scope.supplierJson);
        		    //alert(response);
        		    $scope.reset();
        		    $scope.showSuccessBox = true;
                    $scope.showErrorBox = false;
                }
                else{                    
                    console.log("form invalid");
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
            };
        });