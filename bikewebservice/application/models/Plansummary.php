<?php

class Application_Model_Plansummary extends Application_Model_GetSetModel
{
    private $_id;
    private $_userid;
    private $_planid;
    private $_timecost;
    private $_distancebybattery;
    private $_distancetralvelled;
    private $_batteryusage;
    private $_batterytimeon;
    private $_remarks;
    private $_status;

    public function setId($id){
        $this->_id = (int)$id;
        return $this;
    }
    
    public function getId(){
        return $this->_id;
    }
    
    public function setUserid($userid){
        $this->_userid = (int)$userid;
        return $this;
    }
    
    public function getUserid(){
        return $this->_userid;
    }
    
    public function setPlanid($planid){
        $this->_planid = (int)$planid;
        return $this;
    }
    
    public function getPlanid(){
        return $this->_planid;
    }
    
    public function setTimecost($timecost){
        $this->_timecost = $timecost;
        return $this;
    }
    
    public function getTimecost(){
        return $this->_timecost;
    }
    
    public function setDistancebybattery($distance){
        $this->_distancebybattery = $distance;
        return $this;
    }
    
    public function getDistancebybattery(){
        return $this->_distancebybattery;
    }
    
    public function setDistancetravelled($distance){
        $this->_distancetralvelled = $distance;
        return $this;
    }
    
    public function getDistancetravelled(){
        return $this->_distancetralvelled;
    }
    
    public function setBatteryusage($usage){
        $this->_batteryusage = $usage;
        return $this;
    }
    
    public function getBatteryusage(){
        return $this->_batteryusage;
    }
    
    public function setBatterytimeon($timeon){
        $this->_batterytimeon = $timeon;
        return $this;
    }
    
    public function getBatterytimeon(){
        return $this->_batterytimeon;
    }
    
    public function setRemarks($remarks){
        $this->_remarks = $remarks;
        return $this;
    }
    
    public function getRemarks(){
        return $this->_remarks;
    }
    
    public function setStatus($status){
        $this->_status = $status;
        return $this;
    }
    
    public function getStatus(){
        return $this->_status;
    }
}

