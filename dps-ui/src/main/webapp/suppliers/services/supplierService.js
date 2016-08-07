var supplierServiceApp = angular.module('supplierApp', []);

supplierServiceApp.factory('addSuppliersService', function ($resource) {
            return $resource('/supplier/add');
        });

supplierServiceApp.factory('modifySuppliersService', function ($resource) {
    return $resource('/supplier/modify');
});

supplierServiceApp.factory('getSuppliersService', function ($resource) {
    return $resource('/supplier/find');
});

supplierServiceApp.factory('deleteSuppliersService', function ($resource) {
    return $resource('/supplier/delete/:supplierId', {supplierId:'@supplierId'});
});