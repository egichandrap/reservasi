'use strict';

angular.module('appreservationApp')
    .controller('CostumerDetailController', function ($scope, $rootScope, $stateParams, entity, Costumer) {
        $scope.costumer = entity;
        $scope.load = function (id) {
            Costumer.get({id: id}, function(result) {
                $scope.costumer = result;
            });
        };
        var unsubscribe = $rootScope.$on('appreservationApp:costumerUpdate', function(event, result) {
            $scope.costumer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
