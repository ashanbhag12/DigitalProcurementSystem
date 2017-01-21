angular.module('allCPMsApp')
	
	.factory('getAllCPM', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/allcpm/:productCode', {productCode:"@productCode"});
	})
	
	.factory('getProductsForAllCPMs', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/product/find');
	});