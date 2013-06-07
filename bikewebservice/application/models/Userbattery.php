<?php

class Application_Model_Userbattery extends Application_Model_GetSetModel
{
    private $_id;
    private $_userid;
    private $_bicyclemodel;
    private $_batteryaddress;
    private $_bicycleaddress;
    private $_batterystatus;
    private $_batteryerrors;
    
    public function setId($id){
        $this->_id = (int)$id;
        return $this;
    }
    public function getId(){
        return $this->_id;
    }
    
    public function setUserid($userid){
        $this->_userid = $userid;
        return $this;
    }
    public function getUserid(){
        return $this->_userid;
    }
    
    public function setBicyclemodel($model){
        $this->_bicyclemodel  =$model;
        return $this;
    }
    public function getBicyclemodel(){
        return $this->_bicyclemodel;
    }
    
    public function setBicycleaddress($address){
        $this->_bicycleaddress = $address;
        return $this;
    }
    public function getBicycleaddress(){
        return $this->_bicycleaddress;
    }
    
    public function setBatteryaddress($address){
        $this->_batteryaddress = $address;
        return $this;
    }
    public function getBatteryaddress(){
        return $this->_batteryaddress;
    }
    
    public function setBatterystatus($status){
        $this->_batterystatus = $status;
        return $this;
    }
    public function getBatterystatus(){
        return $this->_batterystatus;
    }
    
    public function setBatteryerrors($errors){
        $this->_batteryerrors = $errors;
        return $this;
    }
    public function getBatteryerrors(){
        return $this->_batteryerrors;
    }
    
}

