<?php

class Application_Model_Planassignment extends Application_Model_GetSetModel
{
    private $_id;
    private $_userid;
    private $_planid;
    private $_endtime;
    private $_status;

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
    
    public function setPlanid($planid){
        $this->_planid = $planid;
        return $this;
    }
    public function getPlanid(){
        return $this->_planid;
    }
    
    public function setEndtime($endtime){
        $this->_endtime = $endtime;
        return $this;
    }
    public function getEndtime(){
        return $this->_endtime;
    }
    
    public function setStatus($status){
        $this->_status = $status;
        return $this;
    }
    public function getStatus(){
        return $this->_status;
    }
}

