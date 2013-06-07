<?php

class Application_Model_Newsandcommunity extends Application_Model_GetSetModel
{
    private $_id;
    private $_title;
    private $_content;
    private $_createtime;
    private $_location;
    private $_img;

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
    
    public function setCreatetime($time){
        $this->_createtime = $time;
        return $this;
    }
    
    public function getCreatetime(){
        return $this->_createtime;
    }
    
    public function setLocation($location){
        $this->_location = $location;
        return $this;
    }
    
    public function getLocation(){
        return $this->_location;
    }
    
    public function setImg($img){
        $this->_img = $img;
        return $this;
    }
    
    public function getImg(){
        return $this->_img;
    }
}

