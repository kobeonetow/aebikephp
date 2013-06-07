<?php

class Application_Model_Shops extends Application_Model_GetSetModel
{
    private $_id;
    private $_name;
    private $_type;
    private $_location;
    private $_city;
    private $_province;
    private $_postcode;
    private $_status;
    
    public function setId($id){
        $this->_id = (int)$id;
        return $this;
    }
    
    public function getId(){
        return $this->_id;
    }

    public function setName($name){
        $this->_name = $name;
        return $this;
    }
    
    public function getName(){
        return $this->_name;
    }
    
    public function setType($type){
        $this->_type = $type;
        return $this;
    }
    
    public function getType(){
        return $this->_type;
    }
    
    public function setLocation($location){
        $this->_location = $location;
        return $this;
    }
    
    public function getLocation(){
        return $this->_location;
    }
    
    public function setCity($city){
        $this->_city = $city;
        return $this;
    }
    
    public function getCity(){
        return $this->_city;
    }
    
    public function setProvince($province){
        $this->_province = $province;
        return $this;
    }
    
    public function getProvince(){
        return $this->_province;
    }
    
    public function setPostcode($postcode){
        $this->_postcode = $postcode;
        return $this;
    }
    
    public function getPostcode(){
        return $this->_postcode;
    }
    
    public function setStatus($status){
        $this->_status =  $status;
        return $this;
    }
    
    public function getStatus(){
        return $this->_status;
    }
}


