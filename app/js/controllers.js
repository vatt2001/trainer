'use strict';

/**
 * This controller manages home page
 */
trainerApp.controller('HomeController', ['$scope', '$location', 'Server',
    function($scope, $location, Server) {

        $scope.isWorking = true;
        $scope.stats = null;

        Server.getTrainingStats().then(
            function(response) {
                $scope.isWorking = false;
                $scope.stats = response;
            },
            function(error) {
                $scope.isWorking = false;
                notify("Error receiving statistics from server: " + error.statusText);
            }
        );

        $scope.go = function(path) {
            $location.path(path);
        };
    }
]);

/**
 * This controller performs dictionary management: displays the dictionary from server, allows quick search on it
 * and adding new words
 */
trainerApp.controller('DictController', ['$scope', '$location', 'Server',
    function($scope, $location, Server) {

        $scope.isWorking = true;
        $scope.words = [];

        Server.listWords().then(
            function(response) {
                $scope.isWorking = false;
                $scope.words = response.words;
            },
            function(error) {
                $scope.isWorking = false;
                notify("Error receiving words from server: " + error.statusText);
                $location.path('/');
            }
        );

        $scope.go = function(path) {
            $location.path(path);
        };
    }
]);

/**
 * This controller performs main training process: it fetches training data from server, performs training
 * and sends results back to server
 */
trainerApp.controller('TrainController', ['$scope', '$routeParams', '$location', 'Trainer', 'Server', 'notify',
    function($scope, $routeParams, $location, Trainer, Server, notify) {

        this.init = function() {
            $scope.reachedWordIndex = 0;
            $scope.isWorking = false;

            if ($location.path() == '/train-prepare') {
                this.start();
            } else {
                this.checkStarted();
            }
        };

        this.start = function() {
            $scope.isWorking = true;
            Trainer.reset();
            Server.newTraining().then(
                function(response) {
                    $scope.isWorking = false;
                    if (response.words.length > 0) {
                        Trainer.setWords(response.words);
                        $location.path('/train');
                    } else {
                        notify("Nothing to train yet");
                        $location.path('/');
                    }
                },
                function(error) {
                    $scope.isWorking = false;
                    notify("Error getting new training details from server: " + error.statusText);
                    $location.path('/');
                }
            );
        };

        this.checkStarted = function() {
            if ($scope.getTotalQuantity() == 0) {
                // User has reloaded page, redirect to the beginning
                $location.path('/train-prepare');
            }
        };

        $scope.getWord = function() {
            return Trainer.getCurr();
        };

        $scope.getCurrentIndex = function() {
            return Trainer.getCurrentIndex();
        };

        $scope.getCurrentResult = function() {
            return Trainer.getCurrentResult();
        };

        $scope.getTotalQuantity = function() {
            return Trainer.getTotalQuantity();
        };

        $scope.getResultRememberedQty = function() {
            return Trainer.getResultRememberedQty();
        };

        $scope.getResultForgottenQty = function() {
            return Trainer.getResultForgottenQty();
        };

        $scope.isFirstWord = function() {
            return Trainer.getCurrentIndex() == 0;
        };

        $scope.nextNavigationDisabled = function() {
            return (Trainer.getCurrentIndex() >= $scope.reachedWordIndex) && $scope.getCurrentResult() === null;
        };

        $scope.isLastWord = function() {
            return Trainer.getCurrentIndex() == Trainer.getTotalQuantity() - 1;
        };

        $scope.wordBgClass = function() {
            if ($scope.getCurrentResult() === true) {
                return "bg-success";
            } else if ($scope.getCurrentResult() === false) {
                return "bg-danger";
            } else {
                return "";
            }
        };

        $scope.go = function ( path ) {
            $location.path( path );
        };

        $scope.remember = function() {
            Trainer.setCurrentResult(true);
        };

        $scope.forgot = function() {
            Trainer.setCurrentResult(false);
        };

        $scope.prev = function() {
            if (Trainer.hasPrev()) {
                $scope.currentWord = Trainer.getPrev();
            }
        };

        $scope.next = function() {
            if (Trainer.hasNext()) {
                $scope.currentWord = Trainer.getNext();
                $scope.reachedWordIndex = Math.max($scope.reachedWordIndex, Trainer.getCurrentIndex());
            }
        };

        $scope.finish = function() {
            $scope.isWorking = true;
            Server.submitTraining(Trainer.getResult()).then(
                function() {
                    notify("Results saved");
                    $scope.isWorking = false;
                },
                function(error) {
                    notify("Error submitting results: " + error.statusText);
                    $scope.isWorking = false;
                }
            );
            $location.path('/train-result');
        };

        $scope.breakTraining = function() {
            if (confirm("Are you sure?")) {
                $location.path("/");
            }
        };

        this.init();
    }
]);
