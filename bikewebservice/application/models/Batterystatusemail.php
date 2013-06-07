<?php

class Application_Model_Batterystatusemail extends Application_Model_GetSetModel
{

    private $_id;
    private $_userid;
    private $_title;
    private $_content;
    private $_senttime;
    private $_response;
    private $_status;
    
    public function setId($id){
        $this->_id = (int)$id;
        return $this;
    }
    
    public function getId(){
        return $this->_id;
    }
    
    public function setUserid($userid){
        $this->_userid =(int)$userid;
        return $this;
    }
    
    public function getUserid(){
        return $this->_userid;
    }
    
    public function setTitle($title){
        $this->_title = $title;
        return $this;
    }
    
    public function getTitle(){
        return $this->_title;
    }
    
    public function setContent($content){
        $this->_content = $content;
        return $this;
    }
    
    public function getContetn(){
        return $this->_content;
    }
    
    public function setSenttime($senttime){
        $this->_senttime = $senttime;
        return $this;
    }
    
    public function getSenttime(){
        return $this->_senttime;
    }
    
    public function setResponse($response){
        $this->_response = $response;
        return $this;
    }
    
    public function getResponse(){
        return $this->_response;
    }
    
    public function setStatus($status){
        $this->_status = $status;
        return $this;
    }
    
    public function getStatus(){
        return $this->_status;
    }
}

