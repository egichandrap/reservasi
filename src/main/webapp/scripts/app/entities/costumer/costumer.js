'use strict';

angular.module('appreservationApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('costumer', {
                parent: 'entity',
                url: '/costumers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'appreservationApp.costumer.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/costumer/costumers.html',
                        controller: 'CostumerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('costumer');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('costumer.detail', {
                parent: 'entity',
                url: '/costumer/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'appreservationApp.costumer.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/costumer/costumer-detail.html',
                        controller: 'CostumerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('costumer');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Costumer', function($stateParams, Costumer) {
                        return Costumer.get({id : $stateParams.id});
                    }]
                }
            })
            .state('costumer.new', {
                parent: 'costumer',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/costumer/costumer-dialog.html',
                        controller: 'CostumerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    namaCostumer: null,
                                    createDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('costumer', null, { reload: true });
                    }, function() {
                        $state.go('costumer');
                    })
                }]
            })
            .state('costumer.edit', {
                parent: 'costumer',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/costumer/costumer-dialog.html',
                        controller: 'CostumerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Costumer', function(Costumer) {
                                return Costumer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('costumer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('costumer.delete', {
                parent: 'costumer',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/costumer/costumer-delete-dialog.html',
                        controller: 'CostumerDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Costumer', function(Costumer) {
                                return Costumer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('costumer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
