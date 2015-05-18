'use strict';

/* App Module */

var trainerApp = angular.module('trainerApp', [
  'ngRoute',
  'trainerControllers',
  'trainerServices'
]);

phonecatApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/', {
        templateUrl: 'partials/main.html',
        controller: 'MainController'
      }).
      when('/stats', {
        templateUrl: 'partials/stats.html',
        controller: 'StatsController'
      }).
      when('/train', {
        templateUrl: 'partials/train.html',
        controller: 'TrainController'
      }).
      when('/train-result', {
        templateUrl: 'partials/train-result.html',
        controller: 'TrainController'
      }).
      otherwise({
        redirectTo: '/'
      });
  }]);
