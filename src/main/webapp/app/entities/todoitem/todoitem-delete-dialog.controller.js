(function() {
    'use strict';

    angular
        .module('toDoJhipsterApp')
        .controller('TodoitemDeleteController',TodoitemDeleteController);

    TodoitemDeleteController.$inject = ['$uibModalInstance', 'entity', 'Todoitem'];

    function TodoitemDeleteController($uibModalInstance, entity, Todoitem) {
        var vm = this;

        vm.todoitem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Todoitem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
