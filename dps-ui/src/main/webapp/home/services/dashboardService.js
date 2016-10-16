var dashboardServiceApp = angular.module('dashboardApp');

dashboardServiceApp.factory('getDashboardCardService', function ($resource) {
    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/dashboard');
});
dashboardServiceApp.factory('modifyDashboardConfigService', function ($resource) {
    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/config/modify');
});