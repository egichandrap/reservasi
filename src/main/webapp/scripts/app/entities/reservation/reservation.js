'use strict';

angular.module('appreservationApp')
    .config(function($stateProvider) {
        $stateProvider
            .state('reservation', {
                parent: 'entity',
                url: '/reservations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'appreservationApp.reservation.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reservation/reservations.html',
                        controller: 'ReservationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('reservation');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('reservation.detail', {
                parent: 'entity',
                url: '/reservation/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'appreservationApp.reservation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reservation/reservation-detail.html',
                        controller: 'ReservationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('reservation');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Reservation', function($stateParams, Reservation) {
                        return Reservation.get({ id: $stateParams.id });
                    }]
                }
            })
            .state('reservation.new', {
                parent: 'reservation',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/reservation/reservation-dialog.html',
                        controller: 'ReservationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function() {
                                return {
                                    startDate: null,
                                    endDate: null,
                                    keterangan: null,
                                    createBy: null,
                                    createDate: null,
                                    id: null,
                                    status: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('reservation', null, { reload: true });
                    }, function() {
                        $state.go('reservation');
                    })
                }]
            })
            .state('reservation.edit', {
                parent: 'reservation',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/reservation/reservation-dialog.html',
                        controller: 'ReservationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Reservation', function(Reservation) {
                                return Reservation.get({ id: $stateParams.id });
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('reservation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('reservation.delete', {
                parent: 'reservation',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/reservation/reservation-delete-dialog.html',
                        controller: 'ReservationDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Reservation', function(Reservation) {
                                return Reservation.get({ id: $stateParams.id });
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('reservation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
