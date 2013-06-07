<?php

class Application_Model_Achievementassignment extends Application_Model_GetSetModel
{
    private $_id;
    private $_userid;
    private $_planid;
    private $_achievementid;
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
    
    public function setAchievementid($aid){
        $this->_achievementid = $aid;
        return $this;
    }
    public function getAchievementid(){
        return $this->_achievementid;
    }
    
    public function setStatus($status){
        $this->_status = $status;
        return $this;
    }
    public function getStatus(){
        return $this->_status;
    }
}

