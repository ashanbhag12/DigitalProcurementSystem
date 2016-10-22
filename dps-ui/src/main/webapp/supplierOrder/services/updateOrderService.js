angular.module('updateOrderApp')

	.factory('getUpdateSupplierOrderService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/updatesupplierorder');
	})
	
	.factory('saveUpdateSupplierOrderService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/updatesupplierorder/update');
	})