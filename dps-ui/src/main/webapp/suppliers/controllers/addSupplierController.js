angular.module('addSupplierApp', ['ngMessages','supplierApp'])
        .controller('addSupplierController', function ($scope, addSuppliersService) {
            $scope.showSuccessBox = true;
            $scope.showErrorBox = true;

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