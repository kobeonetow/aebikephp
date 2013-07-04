<?php

class Application_Model_BaeuserModel extends Application_Model_GetSetModel{
    
    private $_id;
    private $_baeuserid;
    private $_baechannelid;
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
    
    public function getBaechannelid(){
        return $this->_baechannelid;
    }
    
    public function setBaechannelid($channelId){
        $this->_baechannelid = $channelId;
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
