<?php

class Application_Model_Useraccount extends Application_Model_GetSetModel
{
    private $_id;
    private $_username;
    private $_password;
    private $_status;
    private $_onlinestatus;
    private $_phonenumber;
    private $_addressline;
    private $_city;
    private $_province;
    private $_postcode;
    private $_age;
    private $_sex;
    private $_weight;
    private $_height;
    private $_emailaddress;

    public function setId($id){
        $this->_id = (int)$id;
        return $this;
    }
    
    public function getId(){
        return $this->_id;
    }
    
    public function setUsername($username){
        $this->_username = $username;
        return $this;
    }
    
    public function getUsername(){
        return $this->_username;
    }
    
    public function setPassword($password){
        $this->_password = $password;
        return $this;
    }
    
    public function getPassword(){
        return $this->_password;
    }
    
    public function setStatus($status){
        $this->_status = $status;
        return $this;
    }
    
    public function getStatus(){
        return $this->_status;
    }
    
    public function setOnlinestatus($status){
        $this->_onlinestatus = $status;
        return $this;
    }
    
    public function getOnlinestatus(){
        return $this->_onlinestatus;
    }
    
    public function setPhonenumber($number){
        $this->_phonenumber = $number;
        return $this;
    }
    
    public function getPhonenumber(){
        return $this->_phonenumber;
    }
    
    public function setAddressline($addressline){
        $this->_addressline = $addressline;
        return $this;
    }
    
    public function getAddressline(){
        return $this->_addressline;
    }
    
    public function setCity($city){
        $this->_city = $city;
        return $this;
    }
    
    public function getCity(){
        return $this->_city;
    }
    
    public function setProvince($province){
        $this->_province = $province;
        return $this;
    }
    
    public function getProvince(){
        return $this->_province;
    }
     
    public function setPostcode($postcode){
        $this->_postcode = $postcode;
        return $this;
    }
    
    public function getPostcode(){
        return $this->_postcode;
    }
    
    public function setAge($age){
        $this->_age  = $age;
        return $this;
    }
    
    public function getAge(){
        return $this->_age;
    }
    
    public function setSex($sex){
        $this->_sex = $sex;
        return $this;
    }
    
    public function getSex(){
        return $this->_sex;
    }
    
    public function setWeight($weight){
        $this->_weight = $weight;
        return $this;
    }
    
    public function getWeight(){
        return $this->_weight;
    }
    
    public function setHeight($height){
        $this->_height = $height;
        return $this;
    }
    
    public function getHeight(){
        return $this->_height;
    }
    
    public function setEmailaddress($email){
        $this->_emailaddress = $email;
        return $this;
    }
    
    public function getEmailaddress(){
        return $this->_emailaddress;
    }
}



