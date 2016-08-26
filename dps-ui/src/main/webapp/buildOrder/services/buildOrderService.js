angular.module('buildOrderApp')

	.factory('buildOrderCalculateService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/buildorder/calculate');
	})
	
	.factory('saveOrderService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/buildorder/saveorder');
	})
	
	.factory('getCustomersForBuildOrderService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/customer/find');
	})

	.factory('getSuppliersForBuildOrderService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/supplier/find');
	})
	
	.factory('getProductsForBuildOrderService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/product/find');
	})