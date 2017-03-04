'use strict';
angular.module('DPSApp', ['ngMaterial', 'ngMessages', 'ngResource', 'ui.router', 
                           'dashboardApp', 'addCustomerApp', 'editCustomerApp', 'allCPMsApp', 'customerInvoiceApp',
                           'addSupplierApp', 'editSupplierApp', 'addProductApp', 'editProductApp',
                           'productPreferencesApp', 'buildOrderApp', 'viewCustomerOrderApp',
                           'placeOrderApp', 'viewSupplierOrderApp', 'updateOrderApp'])
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
                    
                    .state('productPreferences', {
                        url: "/productPreferences",
                        templateUrl: "customers/views/productPreferences.html",
                        controller: "productPreferencesController"
                    })
                    
                    .state('allCPMs', {
                        url: "/allCPMs",
                        templateUrl: "customers/views/allCPMs.html",
                        controller: "allCPMsController"
                    })
                    
                    .state('customerItemization', {
                        url: "/customerItemization",
                        templateUrl: "customers/views/customerInvoice.html",
                        controller: "customerInvoiceController"
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
                        templateUrl: "customerOrder/views/buildOrder.html",
                        controller: "buildOrderController"
                    })
                    
                    .state('viewCustomerOrder', {
                        url: "/viewCustomerOrder",
                        templateUrl: "customerOrder/views/viewCustomerOrder.html",
                        controller: "viewCustomerOrderController"
                    })
                    
                    .state('placeOrder', {
                        url: "/placeOrder",
                        templateUrl: "supplierOrder/views/placeOrder.html",
                        controller: "placeOrderController"
                    })
                    
                    .state('viewSupplierOrder', {
                        url: "/viewSupplierOrder",
                        templateUrl: "supplierOrder/views/viewSupplierOrder.html",
                        controller: "viewSupplierOrderController"
                    })
                    
                    .state('updateOrder', {
                        url: "/updateOrder",
                        templateUrl: "supplierOrder/views/updateOrder.html",
                        controller: "updateOrderController"
                    });
            
        }).run(function ($rootScope, $state, $timeout, getDashboardConfigService) {    
        	
        	$rootScope.global = getDashboardConfigService.get(); /* Get Global Parameters */
        	
        	/* Show loader on route change start */
        	$rootScope.$on('$stateChangeStart',function(){
        		angular.element(document.querySelector('.loader')).addClass('show');
        	 });
        	
        	/* Hide loader on route change success after 500ms delay */
        	$rootScope.$on('$stateChangeSuccess',function(){
	    		  $timeout(function(){
	    			  angular.element(document.querySelector('.loader')).removeClass('show'); 	    			  
	    		  }, 500)        		  
	    	 });      	
        	
            /* Global function to show Modal Window */
            $rootScope.showModal = function (modalId) {
                angular.element(document.querySelector('.loader')).addClass('show');
                angular.element(document.getElementById(modalId)).css('display', "block");
            };

            /* Global function to hide Modal Window */
            $rootScope.hideModal = function (modalId) {
                angular.element(document.querySelector('.loader')).removeClass('show');
                angular.element(document.getElementById(modalId)).css('display', "none");
            };
            
        });