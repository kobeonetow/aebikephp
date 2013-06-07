<?php

class Application_Model_Announcement  extends Application_Model_GetSetModel
{
    private $_id;
    private $_title;
    private $_content;
    private $_url;
    private $_announcer;
    private $_createtime;

    public function setId($id){
        $this->_id = (int)$id;
        return $this;
    }
    
    public function getId(){
        return $this->_id;
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
    
    public function getContent(){
        return $this->_content;
    }
    
    public function setUrl($url){
        $this->_url = $url;
        return $this;
    }
    
    public function getUrl(){
        return $this->_url;
    }
    
    public function setAnnouncer($announcer){
        $this->_announcer = $announcer;
        return $this;
    }
    
    public function getAnnouncer(){
        return $this->_announcer;
    }
    
    public function setCreatetime($time){
        $this->_createtime = $time;
        return $this;
    }
    
    public function getCreatetime(){
        return $this->_createtime;
    }
}

