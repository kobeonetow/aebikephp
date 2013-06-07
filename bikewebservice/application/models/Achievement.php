<?php

class Application_Model_Achievement extends Application_Model_GetSetModel
{
    private $_id;
    private $_name;
    private $_description;
    private $_level;
    
    public function setId($id){
        $this->_id = (int)$id;
        return $this;
    }
    
    public function getId(){
        return $this->_id;
    }
    
    public function setName($name){
        $this->_name = $name;
        return $this;
    }
    
    public function getName(){
        return $this->_name;
    }
    
    public function setDescription($description){
        $this->_description = $description;
        return $this;
    }
    
    public function getDescription(){
        return $this->_description;
    }
    
    public function setLevel($level){
        $this->_level = $level;
        return $this;
    }
    
    public function getLevel(){
        return $this->_level;
    }
}

