'use strict';

/* Controllers */

var trainerControllers = angular.module('trainerControllers', []);

trainerControllers.controller('MainController', ['$scope', 'Server',
  function($scope, Server) {

    // Check that user is not yet logged in (e.g. we store his email; if we do not have, use isLoggedIn() to check)
    // If he is, then redirect to /stats page
    // If he is not, then show login page and add method to perform login and redirect to /stats on success

  }
]);

trainerControllers.controller('StatsController', ['$scope', 'Server',
  function($scope, Server) {

    // Check that user is logged in (e.g. we have his email, see MainController)
    // If he is not, then redirect to /login page
    // If he is, then request stats from server and show it on template with action button "Start training" and action button Logout

  }
]);

trainerControllers.controller('TrainController', ['$scope', '$routeParams', 'Trainer',
  function($scope, $routeParams, Trainer) {

    // Hold current word in scope
    // Map getCurrentIndex() and getTotalQuantity() to local methods
    // Create 2 methods: init() and complete():
    // - on init - show waiting image and load data from server, then redirect (?) to training with first word (if got)
    //   and if not - then redirect (?) to error page
    // - on complete() - show waiting image and send data to server (along with showing current results)

  }
]);
