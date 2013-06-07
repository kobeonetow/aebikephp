<?php

class Application_Model_Bike extends Application_Model_GetSetModel
{
    private $_id;
    private $_name;
    private $_model;
    private $_properties;
    private $_price;
    private $_height;
    private $_weight;
    private $_wheelsize;
    private $_speed;
    private $_speedchangable;
    private $_createtime;
    
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
    
    public function setModel($model){
        $this->_model = $model;
        return $this;
    }
    
    public function getModel(){
        return $this->_model;
    }
    
    public function setProperties($properties){
        $this->_properties = $properties;
        return $this;
    }
    
    public function getProperties(){
        return $this->_properties;
    }
    
    public function setPrice($price){
        $this->_price = $price;
        return $this;
    }
    
    public function getPrice(){
        return $this->_price;
    }
    
    public function setHeight($height){
        $this->_height = $height;
        return $this;
    }
    
    public function getHeight(){
        return $this->_height;
    }
    
    public function setWeight($weight){
        $this->_weight = $weight;
        return $this;
    }
    
    public function getWeight(){
        return $this->_weight;
    }
    
    public function setWheelsize($wheelsize){
        $this->_wheelsize = $wheelsize;
        return $this;
    }
    
    public function getWheelsize(){
        return $this->_wheelsize;
    }
    
    public function setSpeed($speed){
        $this->_speed = $speed;
        return $this;
    }
    
    public function getSpeed(){
        return $this->_speed;
    }

    public function setSpeedchangable($changable){
        $this->_speedchangable = $changable;
        return $this;
    }
    
    public function getSpeedchangable(){
        return $this->_speedchangable;
    }
    
    public function setCreatetime($createtime){
        $this->_createtime = $createtime;
        return $this;
    }
    
    public function getCreatetime(){
        return $this->_createtime;
    }
}

