<?php

class Application_Model_Chathistory extends Application_Model_GetSetModel
{
    private $_id;
    private $_userid;
    private $_receiverid;
    private $_message;
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
    
    public function setReceiverid($receiverid){
        $this->_receiverid = $receiverid;
        return $this;
    }
    
    public function getReceiverid(){
        return $this->_receiverid;
    }
    
    public function setMessage($message){
        $this->_message = $message;
        return $this;
    }
    
    public function getMessage(){
        return $this->_message;
    }
    
    public function setStatus($status){
        $this->_status = $status;
        return $this;
    }
    
    public function getStatus(){
        return $this->_status;
    }
}

