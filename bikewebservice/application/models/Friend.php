<?php

class Application_Model_Friend extends Application_Model_GetSetModel
{
    private $_id;
    private $_userid;
    private $_friendid;
    private $_status;
    
    public function setId($id){
        $this->_id = (int)$id;
        return $this;
    }
    
    public function getId(){
        $this->_id;
    }
    
    public function setUserid($userid){
        $this->_userid = $userid;
        return $this;
    }
    
    public function getUserid(){
        return $this->_userid;
    }
    
    public function setFriendid($friendid){
        $this->_friendid = $friendid;
        return $this;
    }
    
    public function getFriendid(){
        return $this->_friendid;
    }
    
    public function setStatus($status){
        $this->_status = $status;
        return $this;
    }
    
    public function getStatus(){
        return $this->_status;
    }

}

