'use strict';

/**
 * This service represents interaction with server
 * All responses are async, i.e. are promises which should be treated accordingly
 * (you should provide successful and failure callbacks for them)
 */
trainerApp.service('Server', ['$resource',
    function($resource){
        return {
            userResource: $resource('api/user/:action/', {action: '@action'}),
            wordsResource: $resource('api/words/:id', {id: '@id'}),
            //trainingResource: $resource('api/training/:action', {action: '@action'}),
            trainingResource: $resource('api/training.json'),

            userLogin: function(email, password) {
                return this.userResource.save({action: 'login', email: email, password: password}).$promise;
            },

            userLogout: function() {
                return this.userResource.save({action: 'logout'}).$promise;
            },

            listWords: function() {
                return this.wordsResource.get().$promise;
            },

            addWord: function(spelling, translation) {
                return this.wordsResource.save({spelling: spelling, translation: translation}).$promise;
            },

            deleteWord: function(id) {
                return this.wordsResource.delete({id: id}).$promise;
            },

            repeatWord: function(id) {
                return this.wordsResource.put({id: id}).$promise;
            },

            newTraining: function() {
                return this.trainingResource.get().$promise;
            },

            submitTraining: function(answers) {
                return this.trainingResource.save({answers: answers}).$promise;
            },

            getTrainingStats: function() {
                return this.trainingResource.get({action: 'stats'}).$promise;
            }
        };
    }
]);

/**
 * This service holds data on current training as well as progress on it
 */
trainerApp.service('Trainer', [
    function() {

        return {
            words: [],
            result: [],
            currentIndex: 0,
            maxIndex: 0,

            setWords: function (words) {
                this.words = words;
                this.maxIndex = this.words.length;
                this.currentIndex = 0;
            },

            reset: function() {
                this.setWords([]);
                this.result = [];
            },

            hasPrev: function() {
                return this.currentIndex > 0;
            },

            hasNext: function() {
                return this.currentIndex + 1 < this.maxIndex;
            },

            getPrev: function() {
                if (this.hasPrev()) {
                    this.currentIndex -= 1;
                    return this.words[this.currentIndex];
                } else {
                    return null;
                }
            },

            getNext: function() {
                if (this.hasNext()) {
                    this.currentIndex += 1;
                    return this.words[this.currentIndex];
                } else {
                    return null;
                }
            },

            getCurr: function() {
                if (this.currentIndex >= 0 && this.currentIndex < this.words.length) {
                    return this.words[this.currentIndex];
                } else {
                    return null;
                }
            },

            getCurrentIndex: function() {
                return this.currentIndex;
            },

            getTotalQuantity: function() {
                return this.maxIndex;
            },

            setCurrentResult: function(successful) {
                if (this.currentIndex >= 0 && this.currentIndex < this.maxIndex) {
                    this.result[this.currentIndex] = !!successful;
                }
            },

            getCurrentResult: function(successful) {
                if (this.currentIndex >= 0 && this.currentIndex < this.maxIndex && this.result[this.currentIndex] !== undefined) {
                    return this.result[this.currentIndex];
                } else {
                    return null;
                }
            },

            getResult: function() {
                return this.result;
            },

            getResultRememberedQty: function() {
                return this.result.filter(function(r) { return r; }).length;
            },

            getResultForgottenQty: function() {
                return this.words.length - this.getResultRememberedQty();
            }
        }
    }
]);
