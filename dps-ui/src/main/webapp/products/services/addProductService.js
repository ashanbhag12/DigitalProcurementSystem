var productServiceApp = angular.module('addProductApp');

productServiceApp.factory('addProductsService', function ($resource) {
            return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/product/add');
        });