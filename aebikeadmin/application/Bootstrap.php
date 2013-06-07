<?php

class Bootstrap extends Zend_Application_Bootstrap_Bootstrap
{
   protected function _initMylibrary(){
        $loader = Zend_Loader_Autoloader::getInstance();
        $loader->registerNamespace('Mylibrary');
    }
    
    protected function _init(){
        Zend_Session::start();
    }

}

