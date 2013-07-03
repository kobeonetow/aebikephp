<?php

class Application_Model_BaeuserModel extends Application_Model_GetSetModel{
    
    private $_id;
    private $_baeuserid;
    private $_channelid;
    private $_baetags;
    
    public function getId(){
        return $this->_id;
    }
    
    public function setId($id){
        $this->_id = (int)$id;
        return $this;
    }
    
    public function getBaeuserid(){
        return $this->_baeuserid;
    }
    
    public function setBaeuserid($baeuserid){
        $this->_baeuserid = $baeuserid;
        return $this;
    }
    
    public function getChannelid(){
        return $this->_channelid;
    }
    
    public function setChannelid($channelId){
        $this->_channelid = $channelId;
        return $this;
    }
    
    public function getBaetags(){
        return $this->_baetags;
    }
    
    public function setBaetags($tags){
        $this->_baetags = $tags;
        return $this;
    }
}
?>
