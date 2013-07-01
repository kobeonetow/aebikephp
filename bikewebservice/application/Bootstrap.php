<?php

class Bootstrap extends Zend_Application_Bootstrap_Bootstrap
{
    protected function _initMylibrary(){
        $loader = Zend_Loader_Autoloader::getInstance();
        $loader->registerNamespace('Mylibrary');
        $frontController = Zend_Controller_Front::getInstance();
        $frontController->setControllerDirectory(APPLICATION_PATH . '/controllers');
        $frontController->setParam('env', APPLICATION_ENV);
        unset($frontController);
    }
    
    protected function _initLogger(){
        require_once 'Zend/Log.php';
        require_once 'Zend/Log/Writer/Stream.php';
        $writer = new Zend_Log_Writer_Stream(APPLICATION_PATH.'/../docs/application.log');
        $logger = new Zend_Log($writer);
        Zend_Registry::set("logger",$logger);
        
        $apiKey = "7kGhBDp0chc3q9kXhPg6YVWT";
        $secretKey = "GBjWFfsK0LZe0u8zYidgh4TAILkGDV6q";
        $push_service = new Mylibrary_Push_Channel($apiKey,$secretKey);
        Zend_Registry::set("push",$push_service);
        
        $logger->info("Register push_service success can get from Zend::Registry::get('push') and use pushMessage to send");
    }
}

