'use strict';

var trainerApp = angular.module('trainerApp', [
    'ngRoute',
    'ngResource',
    'cgNotify'
]);

trainerApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/', {
                redirectTo: '/home'
            }).
            when('/home', {
                templateUrl: 'partials/home.html',
                controller: 'HomeController'
            }).
            when('/train-prepare', {
                templateUrl: 'partials/train-prepare.html',
                controller: 'TrainController',
                controllerAs: 'c'
            }).
            when('/train', {
                templateUrl: 'partials/train.html',
                controller: 'TrainController',
                controllerAs: 'c'
            }).
            when('/train-result', {
                templateUrl: 'partials/train-result.html',
                controller: 'TrainController',
                controllerAs: 'c'
            }).
            when('/dict', {
                templateUrl: 'partials/dict.html',
                controller: 'DictController'
            }).
            otherwise({
                redirectTo: '/'
            });
    }
]);


trainerApp.run(['notify',
    function(notify) {
        notify.config({templateUrl: 'bower_components/angular-notify/angular-notify.html'});
    }
]);