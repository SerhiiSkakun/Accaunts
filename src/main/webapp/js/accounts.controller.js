let app = angular.module('accountsModule', ['ngFileUpload']);

app.controller('accountsController', Controller);
Controller.$inject = ['$scope', 'accountsService'];
function Controller($scope, accountsService) {
    let vm = this;
    vm.userId = 0;
    vm.getUser = getUser;

    vm.filter = {
        filePath: null,
        fileName: null,
        startRow: null,
        finishRow: null,
        isUniqRecords: true,
        isGatherMessages: false,
        isErrorsOnly: true,
        isTeStackTraceOnly: true
    };

    vm.getPdfReport = getPdfReport;
    vm.selectFile = selectFile;
    vm.onChangeUniqRecords = onChangeUniqRecords;

    function selectFile (file) {
        if(file) {
            vm.filter.fileName = file.name;
        }
    }

    function onChangeUniqRecords() {
        if (!vm.filter.isUniqRecords) vm.filter.isGatherMessages = false;
    }

    function getUser(userId) {
        accountsService.getUser(userId)
            .then((response) => {
                vm.user = response.user;
            });
    }

    function getPdfReport() {
        vm.isGettingReportInProgress = true;
        accountsService.getPdfReport(vm.filter)
            .then(function () {
                vm.isGettingReportInProgress = false;
            }).catch(function (error) {
            vm.isGettingReportInProgress = false;
            alert("Exception: " + error);
        });
    }
}
