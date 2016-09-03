angular.module('placeOrderApp')

	.factory('getPlaceOrderService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/placeorder');
	});
	
	/*.factory('savePlaceOrderService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/placeorder/save');
	})*/