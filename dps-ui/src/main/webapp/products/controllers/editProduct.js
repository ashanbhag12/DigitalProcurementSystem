angular.module('editProductApp', ['ngMessages', 'angularUtils.directives.dirPagination'])
        .controller('editProductController', function ($rootScope, $scope, $timeout, getProductsService, modifyProductsService, deleteProductsService, getSuppliersInitialsService) {
            /* Initialize the page variables */
            $scope.showSuccessBox = false; /* Hide the error messages */
            $scope.showErrorBox = false; /* Hide the success messages */
            $scope.editDisabled = false; /* Enable the Edit button */
            $scope.deleteDisabled = true; /* Disable the Delete button */
            $scope.editProductForm = false; /* Hide the edit Product Form */
            $scope.searchedResults = false; /* Hide the search results container */
            $scope.sortOrder = false; /* set the default sort order */
            $scope.sortBy = 'name'; /* set the default sort type */
            $scope.selectedRows = []; /* Array for toggleAll function */
            $scope.products = []; /* Array of all Products */
            
            getSuppliersInitialsService.query().$promise.then(function(data) {
            	$scope.allSupplierInitials = data.map(function (initial) { return { abbrev: initial }; });
            });
            
            $scope.searchProductCode = ''; /* Code for product search */
            
            $scope.showSuccessBox = false;
            $scope.showErrorBox = false;
            
            /* Function will be executed after the page is loaded */
            $scope.$on('$viewContentLoaded', function () {   
                console.log("page loaded")
            });

            /* Product object to be edited */
            /* changed name from editProduct with product bcos it was conflicting with form name*/
            $scope.product = {
                id: "",
                productCode:"",
        		cartoonQuantity:"",
        		cbm:"",
        		price:"",
        		weight:"",
        		description:"",
        		moq:"",
        		defaultMargin:"",
        		supplierInitials:"",
        		supplierProductCode:"",
        		isValid:""
            };

            /* Function to select/unselect all the Products */
            $scope.toggleAll = function () {
                if ($scope.selectAll) {
                    $scope.selectAll = true;
                    $scope.editDisabled = true;
                    $scope.deleteDisabled = false;
                    angular.forEach($scope.products, function (product) {
                        product.isChecked = $scope.selectAll;
                        $scope.selectedRows.push(1);
                    });
                }
                else {
                    $scope.selectAll = false;
                    $scope.editDisabled = true;
                    $scope.deleteDisabled = true;
                    angular.forEach($scope.products, function (product) {
                        product.isChecked = $scope.selectAll;
                        $scope.selectedRows = [];
                    });
                }
            };

            /* Function to select/unselect the Product */
            $scope.toggle = function (element) {
                if (element.isChecked) {
                    $scope.selectedRows.push(1);
                    $scope.editDisabled = false;
                    $scope.deleteDisabled = false;
                }
                else {
                    $scope.selectedRows.pop();
                }
                if ($scope.products.length === $scope.selectedRows.length) {
                    $scope.selectAll = true;
                    $scope.editDisabled = true;
                }
                else {
                    $scope.selectAll = false;
                }
                if ($scope.selectedRows.length === 0) {
                    $scope.editDisabled = true;
                    $scope.deleteDisabled = true;
                }
                else if ($scope.selectedRows.length > 1) {
                    $scope.editDisabled = true;
                }
                else {
                    $scope.editDisabled = false;
                }
            };

            /* Function to edit the selected Product */
            $scope.edit = function () {
                angular.forEach($scope.products, function (product) {
                    if (product.isChecked) {
                        $scope.editProductForm = true;
                        $scope.product.productCode = product.productCode;
                        $scope.product.cartoonQuantity = product.cartoonQuantity;
                        $scope.product.cbm = product.cbm;
                        $scope.product.price = product.price;
                        $scope.product.weight = product.weight;
                        $scope.product.description = product.description;
                        $scope.product.moq = product.moq;
                        $scope.product.defaultMargin = product.defaultMargin;
                        $scope.product.supplierInitials = product.supplierInitials;
                        $scope.product.supplierProductCode = product.supplierProductCode;
                        $scope.product.isValid = "no";
                    }
                });
            };

            /* Function to delete the selected Products */
            $scope.deleteProduct = function () {
                angular.element(document.querySelector('.loader')).addClass('show');
                angular.element(document.querySelector('.modal')).css('display', "block");

                angular.forEach($scope.products, function (product) {
                    if (product.isChecked) {
                    	response = deleteProductsService.remove({productId : product.id});
                    }
                });

                angular.element(document.querySelector('.modal')).css('display', "none");
                $timeout(function () {
                    angular.element(document.querySelector('.loader')).removeClass('show');
                    $scope.selectAll = false;
                    // WS call to get all Product.
                    $scope.products = getProductsService.query({code:$scope.searchProductCode});
                    $scope.selectedRows = [];
                    $scope.editDisabled = true;
                    $scope.deleteDisabled = true;
                }, 1000);
            };

            /* Function to update the selected Product */
            /* added keepgoing check for performance. Bcos for very first product if isChecked condition is true, that product will be updated and for loop breaks
             * Note: no break; concept for angularJs forEach */
            
            $scope.updateProductJson = {};
            
            $scope.update = function () {
            	var keepGoing = true;
                angular.forEach($scope.products, function (product) {
                	if(keepGoing) {
                		if (product.isChecked) {
                            product.productCode = $scope.product.productCode;
                            product.cartoonQuantity = $scope.product.cartoonQuantity;
                            product.cbm = $scope.product.cbm;
                            product.price = $scope.product.price;
                            product.weight = $scope.product.weight;
                            product.description = $scope.product.description;
                            product.moq = $scope.product.moq;
                            product.defaultMargin = $scope.product.defaultMargin;
                            product.supplierInitials = $scope.product.supplierInitials;
                            product.supplierProductCode = $scope.product.supplierProductCode;
                            product.isValid = $scope.product.isValid;
                            
                            $scope.updateProductJson = angular.toJson(product);
                            
                            keepGoing = false;
                        }                    
                	}
                });
                
                /* Service call to update product */
    		    response = modifyProductsService.save($scope.updateProductJson);
    		    
    		    // WS call to get all Product.
    		    $scope.products = getProductsService.query({code:$scope.searchProductCode});
                
                $scope.editProductForm = false;
                $scope.showSuccessBox = true;
                $scope.showErrorBox = false;
            };

            /* Function to search for Products */
            $scope.searchProduct = function () {
                $scope.selectAll = false;
                $scope.searchedResults = true;
                $scope.editDisabled = true;
                $scope.deleteDisabled = true;
                
                /* Service Call to retrieve searched product */
                $scope.products = getProductsService.query({code:$scope.searchProductCode});
            };

            /* Global function to show Modal Window */
            $rootScope.showModal = function () {
                angular.element(document.querySelector('.loader')).addClass('show');
                angular.element(document.querySelector('.modal')).css('display', "block");
            };

            /* Global function to hide Modal Window */
            $rootScope.hideModal = function () {
                angular.element(document.querySelector('.loader')).removeClass('show');
                angular.element(document.querySelector('.modal')).css('display', "none");
            };
            
            $scope.reset = function () {
                $scope.product = {};
                $scope.editProduct.$setPristine();
                $scope.editProduct.productCode.$touched = false;
                $scope.editProduct.cartoonQuantity.$touched = false;
                $scope.editProduct.cbm.$touched = false;
                $scope.editProduct.price.$touched = false;
                $scope.editProduct.weight.$touched = false;
                $scope.editProduct.description.$touched = false;
                $scope.editProduct.moq.$touched = false;
                $scope.editProduct.defaultMargin.$touched = false;
                $scope.editProduct.supplierInitials.$touched = false;
                $scope.editProduct.supplierProductCode.$touched = false;
                $scope.editProduct.isValid.$touched = false;
                
                $scope.showSuccessBox = false;
            };
        });