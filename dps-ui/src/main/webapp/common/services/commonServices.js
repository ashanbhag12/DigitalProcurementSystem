angular.module('DPSApp')
	.factory('getDashboardConfigService', function ($resource) {
	    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/config');
	});