'use strict';

/* Services */

var trainerServices = angular.module('trainerServices', ['ngResource']);

trainerServices.service('Server', ['$resource',
  function($resource){

    return {
        isLoggedIn: function() {},
        login: function() {},
        logout: function() {},
        getStats: function() {},
        getWordsToTrain: function() {},
        saveTrainResults: function(results) {}
    }

//    return $resource('phones/:phoneId.json', {}, {
//      query: {method:'GET', params:{phoneId:'phones'}, isArray:true}
//    });
  }
]);

trainerServices.service('Trainer', ['Server',
  function(server){

    return {
        words: [],
        result: [],
        currentIndex: 0,
        maxIndex: -1,

        loadWords: function() {
            words = server.getWordsToTrain();
            result = [];
            currentIndex = 0;
            maxIndex = words.length;
            return this;
        },

        hasNext: function() {
            return currentIndex + 1 < maxIndex;
        },

        getNext: function() {
            if (this.hasNext()) {
                currentIndex += 1;
                return words[currentIndex];
            } else {
                return null;
            }
        },

        getCurrentIndex: function() {
            return currentIndex;
        },

        getTotalQuantity: function() {
            return maxIndex;
        },

        setResult: function(successful) {
            if (currentIndex < maxIndex) {
                result[currentIndex] = !!successful;
            }
            return this;
        },

        sendResults: function() {
            server.saveTrainResults(result);
            return this;
        }
    }
  }
]);
