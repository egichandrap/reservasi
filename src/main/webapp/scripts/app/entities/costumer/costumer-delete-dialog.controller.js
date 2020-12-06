'use strict';

angular.module('appreservationApp')
	.controller('CostumerDeleteController', function($scope, $uibModalInstance, entity, Costumer) {

        $scope.costumer = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Costumer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
