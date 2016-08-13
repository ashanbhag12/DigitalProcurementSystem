var productServiceApp = angular.module('productApp', []);

productServiceApp.factory('addProductsService', function ($resource) {
            return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/product/add');
        });

productServiceApp.factory('modifyProductsService', function ($resource) {
    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/product/modify');
});

productServiceApp.factory('getProductsService', function ($resource) {
    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/product/find');
});

productServiceApp.factory('deleteProductsService', function ($resource) {
    return $resource('http://localhost:8080/dps-web-service-0.0.1/rest/product/delete/:productId', {productId:'@productId'});
});