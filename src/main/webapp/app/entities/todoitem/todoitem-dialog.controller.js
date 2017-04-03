(function() {
    'use strict';

    angular
        .module('toDoJhipsterApp')
        .controller('TodoitemDialogController', TodoitemDialogController);

    TodoitemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Todoitem', 'Citizen'];

    function TodoitemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Todoitem, Citizen) {
        var vm = this;

        vm.todoitem = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.citizens = Citizen.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.todoitem.id !== null) {
                Todoitem.update(vm.todoitem, onSaveSuccess, onSaveError);
            } else {
                Todoitem.save(vm.todoitem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('toDoJhipsterApp:todoitemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateCreated = false;
        vm.datePickerOpenStatus.dateDue = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
