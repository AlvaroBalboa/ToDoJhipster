(function() {
    'use strict';

    angular
        .module('toDoJhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('citizen', {
            parent: 'entity',
            url: '/citizen',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'toDoJhipsterApp.citizen.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/citizen/citizens.html',
                    controller: 'CitizenController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('citizen');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('citizen-detail', {
            parent: 'citizen',
            url: '/citizen/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'toDoJhipsterApp.citizen.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/citizen/citizen-detail.html',
                    controller: 'CitizenDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('citizen');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Citizen', function($stateParams, Citizen) {
                    return Citizen.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'citizen',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('citizen-detail.edit', {
            parent: 'citizen-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/citizen/citizen-dialog.html',
                    controller: 'CitizenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Citizen', function(Citizen) {
                            return Citizen.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('citizen.new', {
            parent: 'citizen',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/citizen/citizen-dialog.html',
                    controller: 'CitizenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                citizen: null,
                                age: null,
                                job: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('citizen', null, { reload: 'citizen' });
                }, function() {
                    $state.go('citizen');
                });
            }]
        })
        .state('citizen.edit', {
            parent: 'citizen',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/citizen/citizen-dialog.html',
                    controller: 'CitizenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Citizen', function(Citizen) {
                            return Citizen.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('citizen', null, { reload: 'citizen' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('citizen.delete', {
            parent: 'citizen',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/citizen/citizen-delete-dialog.html',
                    controller: 'CitizenDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Citizen', function(Citizen) {
                            return Citizen.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('citizen', null, { reload: 'citizen' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
