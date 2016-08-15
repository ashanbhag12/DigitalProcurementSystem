angular.module('addProductApp', ['ngMessages'])
        .controller('addProductController', function ($scope, addProductsService, getSuppliersInitialsService) {
            $scope.showSuccessBox = false;
            $scope.showErrorBox = false;
            // WebService should be called to fetch initials for Suppliers
            getSuppliersInitialsService.query().$promise.then(function(data) {
            	$scope.allSupplierInitials = data.map(function (initial) { return { abbrev: initial }; });
            });

            $scope.product = {                	
            		"productCode":"",
            		"cartoonQuantity":"",
            		"cbm":"",
            		"price":"",
            		"weight":"",
            		"description":"",
            		"moq":"",
            		"defaultMargin":"",
            		"supplierProductInfoList": [{
            			"supplierInitials": "",
            			"supplierProductCode": ""
            		}, {
            			"supplierInitials": "",
            			"supplierProductCode": ""
            		}, {
            			"supplierInitials": "",
            			"supplierProductCode": ""
            		}],
            		"isValid":"false"
            };

            $scope.submitForm = function (addProduct) {
                if (addProduct.$valid) {
                	$scope.productJson = angular.toJson($scope.product);
        		    //alert($scope.productJson);
        		    response = addProductsService.save($scope.productJson);
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
                $scope.product = {};
                $scope.product.isValid="false";
                $scope.addProduct.$setPristine();
                $scope.addProduct.productCode.$touched = false;
                $scope.addProduct.cartoonQuantity.$touched = false;
                $scope.addProduct.cbm.$touched = false;
                $scope.addProduct.price.$touched = false;
                $scope.addProduct.weight.$touched = false;
                $scope.addProduct.description.$touched = false;
                $scope.addProduct.moq.$touched = false;
                $scope.addProduct.defaultMargin.$touched = false;
                $scope.addProduct.supplierInitials1.$touched = false;
                $scope.addProduct.supplierProductCode1.$touched = false;
                $scope.addProduct.supplierInitials2.$touched = false;
                $scope.addProduct.supplierProductCode2.$touched = false;
                $scope.addProduct.supplierInitials3.$touched = false;
                $scope.addProduct.supplierProductCode3.$touched = false;
                $scope.addProduct.isValid.$touched = false;
                $scope.showSuccessBox = false;
            };
        });