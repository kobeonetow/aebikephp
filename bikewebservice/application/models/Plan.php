<?php

class Application_Model_Plan extends Application_Model_GetSetModel
{
    private $_id;
    private $_name;
    private $_starttime;
    private $_endtime;
    private $_startlocation;
    private $_endlocation;
    private $_expecttime;
    private $_distance;
    private $_pplinterested;
    private $_pplgoing;
    private $_pplexpected;
    private $_description;
    private $_sponsor;
    private $_type;
    private $_achievementid;
    private $_status;
    private $_userid;
    
    public function setId($id){
        $this->_id = (int)$id;
        return $this;
    }
    
    public function getId(){
        return $this->_id;
    }
    
    public function setUserid($id){
        $this->_userid = $id;
    }
    
    public function getUserid(){
        return $this->_userid;
    }
    
    public function setName($name){
        $this->_name = $name;
        return $this;
    }
    
    public function getName(){
        return $this->_name;
    }
    
    public function setStarttime($starttime){
        $this->_starttime = $starttime;
        return $this;
    }
    
    public function getStarttime(){
        return $this->_starttime;
    }
    
    public function setEndtime($endtime){
        $this->_endtime = $endtime;
        return $this;
    }
    
    public function getEndtime(){
        return $this->_endtime;
    }
    
    public function setStartlocation($startlocation){
        $this->_startlocation = $startlocation;
        return $this;
    }
    
    public function getStartlocation(){
        return $this->_startlocation;
    }
    
    public function setEndlocation($endlocation){
        $this->_endlocation = $endlocation;
        return $this;
    }
    
    public function getEndlocation(){
        return $this->_endlocation;
    }
    
    public function setExpecttime($expecttime){
        $this->_expecttime = $expecttime;
        return $this;
    }
    
    public function getExpecttime(){
        return $this->_expecttime;
    }
    
    public function setDistance($distance){
        $this->_distance = $distance;
        return $this;
    }
    
    public function getDistance(){
        return $this->_distance;
    }
    
    public function setPplinterested($pplinterested){
        $this->_pplinterested = $pplinterested;
        return $this;
    }
    
    public function getPplinterested(){
        return $this->_pplinterested;
    }
    
    public function setPplgoing($pplgoing){
        $this->_pplgoing = $pplgoing;
        return $this;
    }
    
    public function getPplgoing(){
        return $this->_pplgoing;
    }
    
    public function setPplexpected($pplexpected){
        $this->_pplexpected = $pplexpected;
        return $this;
    }
    
    public function getPplexpected(){
        return $this->_pplexpected;
    }
    
    public function setDescription($description){
        $this->_description = $description;
        return $this;
    }
    
    public function getDescription(){
        return $this->_description;
    }
    
    public function setSponsor($sponsor){
        $this->_sponsor = $sponsor;
        return $this;
    }
    
    public function getSponsor(){
        return $this->_sponsor;
    }
    
    public function setType($type){
        $this->_type = $type;
        return $this;
    }
    
    public function getType(){
        return $this->_type;
    }
    
    public function setAchievementid($achievementid){
        $this->_achievementid = $achievementid;
        return $this;
    }
    
    public function getAchievementid(){
        return $this->_achievementid;
    }
    
    public function setStatus($status){
        $this->_status = $status;
        return $this;
    }
    
    public function getStatus(){
        return $this->_status;
    }
}

