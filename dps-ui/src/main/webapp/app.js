'use strict';
angular.module('mainApp', ['ngMaterial', 'ngMessages', 'ngResource', 'ui.router', 
                           'dashboardApp', 'addCustomerApp', 'editCustomerApp',
                           'addSupplierApp', 'editSupplierApp', 'addProductApp', 'editProductApp',
                           'productPreferencesApp', 'buildOrderApp', 'placeOrderApp'])
        .config(function ($stateProvider, $urlRouterProvider, $mdThemingProvider) {
        	
        	$mdThemingProvider.theme('default')
            /*.primaryPalette('indigo')*/
            .warnPalette('red')
            .accentPalette('amber'); 
        	
            $urlRouterProvider.otherwise("/dashboard");

            $stateProvider
                    .state('dashboard', {
                        url: "/dashboard",
                        templateUrl: "home/views/dashboard.html",
                        controller: "dashboardController"
                    })

                    .state('addProduct', {
                        url: "/addProduct",
                        templateUrl: "products/views/addProduct.html",
                        controller: "addProductController"
                    })

                    .state('editProduct', {
                        url: "/editProduct",
                        templateUrl: "products/views/editProduct.html",
                        controller: "editProductController"
                    })
                    
                    .state('productPreferences', {
                        url: "/productPreferences",
                        templateUrl: "products/views/productPreferences.html",
                        controller: "productPreferencesController"
                    })
                    
                    .state('addCustomer', {
                        url: "/addCustomer",
                        templateUrl: "customers/views/addCustomer.html",
                        controller: "addCustomerController"
                    })
                    
                    .state('editCustomer', {
                        url: "/editCustomer",
                        templateUrl: "customers/views/editCustomer.html",
                        controller: "editCustomerController"
                    })

                    .state('addSupplier', {
                        url: "/addSupplier",
                        templateUrl: "suppliers/views/addSupplier.html",
                        controller: "addSupplierController"
                    })

                    .state('editSupplier', {
                        url: "/editSupplier",
                        templateUrl: "suppliers/views/editSupplier.html",
                        controller: "editSupplierController"
                    })
                    
                    .state('buildOrder', {
                        url: "/buildOrder",
                        templateUrl: "buildOrder/views/buildOrder.html",
                        controller: "buildOrderController"
                    })
                    
                    .state('placeOrder', {
                        url: "/placeOrder",
                        templateUrl: "placeOrder/views/placeOrder.html",
                        controller: "placeOrderController"
                    });
        }).run(function ($rootScope) {
            
            /* Global function to show Modal Window */
            $rootScope.showModal = function (modalId) {
                console.log(modalId);
                angular.element(document.querySelector('.loader')).addClass('show');
                angular.element(document.getElementById(modalId)).css('display', "block");
            };

            /* Global function to hide Modal Window */
            $rootScope.hideModal = function (modalId) {
                angular.element(document.querySelector('.loader')).removeClass('show');
                angular.element(document.getElementById(modalId)).css('display', "none");
            };
            
        });