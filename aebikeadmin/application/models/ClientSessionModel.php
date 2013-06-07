<?php

class Application_Model_ClientSessionModel
{
    private $namespace;
    
    public function __construct() {
        $this->namespace = new Zend_Session_Namespace('adminWeb');
    }

    public function destroySession(){
        Zend_Session::destroy(true);
    }
    
}

