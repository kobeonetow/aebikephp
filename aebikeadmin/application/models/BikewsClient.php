<?php

class Application_Model_BikewsClient extends Application_Model_HttpClient
{
    public function __construct() {
        parent::__construct('bikehost', 'bikesecret');
    }
    
    public function testConnection(){
        return $this->call($this->URI, $_POST);
    }
    
}

