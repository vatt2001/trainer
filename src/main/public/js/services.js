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
            wordsResource: $resource('api/words/:id', {id: '@id'}, {'put' : {method: 'PUT'}}),
            trainingResource: $resource('api/training/:action', {action: '@action'}),

            // Temporary resources
            //wordsResource: $resource('api/words.json'),
            //trainingResource: $resource('api/training.json'),
            //trainingStatsResource: $resource('api/training_stats.json'),

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
                this.answers = [];
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
                    this.answers[this.currentIndex] = !!successful;
                }
            },

            getCurrentResult: function(successful) {
                if (this.currentIndex >= 0 && this.currentIndex < this.maxIndex && this.answers[this.currentIndex] !== undefined) {
                    return this.answers[this.currentIndex];
                } else {
                    return null;
                }
            },

            getResult: function() {
                var result = [];
                for (var i = 0; i < this.maxIndex; i++) {
                    result.push({id: this.words[i].id, isCorrect: this.answers[i]})
                }
                return result;
            },

            getResultRememberedQty: function() {
                return this.answers.filter(function(r) { return r; }).length;
            },

            getResultForgottenQty: function() {
                return this.words.length - this.getResultRememberedQty();
            }
        }
    }
]);
