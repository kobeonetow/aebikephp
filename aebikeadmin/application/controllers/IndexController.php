<?php

class IndexController extends Zend_Controller_Action
{
    protected $bikeservice;
    
    public function init()
    {
        /* Initialize action controller here */
        $this->bikeservice = new Application_Model_BikewsClient();
    }

    public function indexAction()
    {
        // action body
        $result = $this->bikeservice->testConnection(); 
        var_dump($result);
    }


}

