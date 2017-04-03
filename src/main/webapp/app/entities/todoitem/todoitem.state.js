(function() {
    'use strict';

    angular
        .module('toDoJhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('todoitem', {
            parent: 'entity',
            url: '/todoitem',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'toDoJhipsterApp.todoitem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/todoitem/todoitems.html',
                    controller: 'TodoitemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('todoitem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('todoitem-detail', {
            parent: 'todoitem',
            url: '/todoitem/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'toDoJhipsterApp.todoitem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/todoitem/todoitem-detail.html',
                    controller: 'TodoitemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('todoitem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Todoitem', function($stateParams, Todoitem) {
                    return Todoitem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'todoitem',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('todoitem-detail.edit', {
            parent: 'todoitem-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/todoitem/todoitem-dialog.html',
                    controller: 'TodoitemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Todoitem', function(Todoitem) {
                            return Todoitem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('todoitem.new', {
            parent: 'todoitem',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/todoitem/todoitem-dialog.html',
                    controller: 'TodoitemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                todoName: null,
                                dateCreated: null,
                                isDone: false,
                                dateDue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('todoitem', null, { reload: 'todoitem' });
                }, function() {
                    $state.go('todoitem');
                });
            }]
        })
        .state('todoitem.edit', {
            parent: 'todoitem',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/todoitem/todoitem-dialog.html',
                    controller: 'TodoitemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Todoitem', function(Todoitem) {
                            return Todoitem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('todoitem', null, { reload: 'todoitem' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('todoitem.delete', {
            parent: 'todoitem',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/todoitem/todoitem-delete-dialog.html',
                    controller: 'TodoitemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Todoitem', function(Todoitem) {
                            return Todoitem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('todoitem', null, { reload: 'todoitem' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
