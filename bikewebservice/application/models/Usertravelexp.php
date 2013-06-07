<?php

class Application_Model_Usertravelexp extends Application_Model_GetSetModel
{
    private $_id;
    private $_userid;
    private $_totaldistance;
    private $_totaltime;
    private $_totalbatteryused;
    private $_totalbatterytime;

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
    
    public function setTotaldistance($distance){
        $this->_totaldistance = $distance;
        return $this;
    }
    public function getTotaldistance(){
        return $this->_totaldistance;
    }
    
    public function setTotaltime($time){
        $this->_totaltime = $time;
        return $this;
    }
    public function getTotaltime(){
        return $this->_totaltime;
    }
    
    public function setTotalbatteryused($usage){
        $this->_totalbatteryused = $usage;
        return $this;
    }
    public function getTotalbatteryused(){
        return $this->_totalbatteryused;
    }
    
    public function setTotalbatterytime($time){
        $this->_totalbatterytime = $time;
        return $this;
    }
    public function getTotalbatterytime(){
        return $this->_totalbatterytime;
    }
    
}

