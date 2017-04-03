(function() {
    'use strict';

    angular
        .module('toDoJhipsterApp')
        .controller('TodoitemDetailController', TodoitemDetailController);

    TodoitemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Todoitem', 'Citizen'];

    function TodoitemDetailController($scope, $rootScope, $stateParams, previousState, entity, Todoitem, Citizen) {
        var vm = this;

        vm.todoitem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('toDoJhipsterApp:todoitemUpdate', function(event, result) {
            vm.todoitem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
