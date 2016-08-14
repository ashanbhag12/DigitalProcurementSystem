angular.module('editProductApp')
	.factory('modifyProductsService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/product/modify');
	})
	.factory('getProductsService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/product/find');
	})
	.factory('deleteProductsService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/product/delete/:productId', {productId:'@productId'},{
	    	remove:{
	            method:'POST'
	            }
	    });
	})
	/* to get suppliers initials for product page */
	.factory('getSuppliersInitialsService', function ($resource, addSuppliersService) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/supplier/initials');
	});