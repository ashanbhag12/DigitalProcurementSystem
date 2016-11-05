angular.module('viewSupplierOrderApp')

	.factory('getSuppliersService', function ($resource) {
		return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/supplier/find');
	})

	.factory('getSupplierOrderService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/supplierorder/');
	})