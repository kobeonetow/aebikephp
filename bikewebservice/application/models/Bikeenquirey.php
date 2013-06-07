<?php

class Application_Model_Bikeenquirey extends Application_Model_GetSetModel
{
    private $_id;
    private $_userid;
    private $_bikeid;
    private $_message;
    private $_replymessage;
    private $_createtime;
    
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
    
    public function setBikeid($bikeid){
        $this->_bikeid = (int)$bikeid;
        return $this;
    }
    
    public function getBikeid(){
        return $this->_bikeid;
    }
    
    public function setMessage($message){
        $this->_message = $message;
        return $this;
    }
    
    public function getMessage(){
        return $this->_message;
    }
    
    public function setReplymessage($reply){
        $this->_replymessage = $reply;
        return $this;
    }
    
    public function getReplymessage(){
        return $this->_replymessage;
    }

    public function setCreatetime($createtime){
        $this->_createtime = $createtime;
        return $this;
    }
    
    public function getCreatetime(){
        return $this->_createtime;
    }
}

