var productServiceApp = angular.module('addProductApp');

productServiceApp.factory('addProductsService', function ($resource) {
            return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/product/add');
        });

//to get suppliers initials for product page    
supplierServiceApp.factory('getSuppliersInitialsService', function ($resource) {
    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/supplier/initials');
	});