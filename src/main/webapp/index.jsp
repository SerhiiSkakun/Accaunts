<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accounts</title>
    <link rel="icon" href="./favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="./favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="./lib/bootstrap.min.css"/>
    <link rel="stylesheet" href="./lib/fontawesome/css/font-awesome.min.css">

    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.2/angular.min.js"></script>
    <script src="./lib/jquery-3.2.1.min.js"></script>
    <script src="./lib/jquery.fileDownload.js"></script>
    <script src="./lib/bootstrap.min.js"></script>
    <script src="./lib/ui-bootstrap-tpls.js"></script>
    <script src="./lib/ng-file-upload-all.js"></script>

    <link rel="stylesheet" href="./css/styles.css"/>
    <script src="./js/accounts.controller.js"></script>
    <script src="./js/accounts.service.js"></script>
</head>
<body ng-app="accountsModule" ng-controller="accountsController as vm">
    <div class="panel mb-20">
        <div class="panel-heading">
            <div class="col-xs-6" style="text-align: left">
                <h3 class="panel-title"><b>Accounts</b></h3>
            </div>
            <div class="col-xs-6" style="text-align: right">
                <a href="./html/task.html" target="_blank" style="color: white">
                    <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                </a>
            </div>
        </div>

        <br/>

        <input name="selectFilePath" type="text"
               class="form-text-input"
               ng-model="userId"
               placeholder="Input UserId">

        <button class="btn btn-te"
                ng-click="vm.getUser(userId)">Get User
            <i class="fa m-r-5"
               ng-class="{'fa-spinner fa-pulse fa-1x fa-fw': true}">
            </i>
        </button>

        <br>
        <div ng-bind-html="vm.user"></div>
    </div>
</body>
</html>
