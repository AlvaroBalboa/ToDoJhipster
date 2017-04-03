(function() {
    'use strict';

    angular
        .module('toDoJhipsterApp')
        .controller('CitizenDialogController', CitizenDialogController);

    CitizenDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Citizen', 'Company'];

    function CitizenDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Citizen, Company) {
        var vm = this;

        vm.citizen = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.citizen.id !== null) {
                Citizen.update(vm.citizen, onSaveSuccess, onSaveError);
            } else {
                Citizen.save(vm.citizen, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('toDoJhipsterApp:citizenUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
