angular.module('productPreferencesApp')

	.factory('getProductPreferencesService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/custprodpref/:shipmark', {shipmark:'@shipmark'})
	})
	
	.factory('modifyProductPreferencesService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/custprodpref/modify');
	})
	
	.factory('getCustomersProductPreferencesService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/customer/find');
	})
	
	.factory('exportToPDF', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/custprodpref/export');
	});