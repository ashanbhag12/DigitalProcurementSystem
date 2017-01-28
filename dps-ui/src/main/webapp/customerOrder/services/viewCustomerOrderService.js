angular.module('viewCustomerOrderApp')

	.factory('getCustomersService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/customer/find'); 
	})
	
	.factory('getCustomerOrderService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/customerorder/:customerShipmark/:startDate/:endDate', 
	    		{customerShipmark:'@customerShipmark', startDate: "@startDate", endDate: "@startDate"});
	})
	
	.factory('deleteCustomerOrderService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/customerorder/cancel');
	})
	
	.factory('updateCustomerOrderService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/customerorder/update');
	})
	
	.factory('imageInvoiceService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/customerorder/imginv');
	})
	
	.factory('pdfInvoiceService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/customerorder/txtinv');
	});