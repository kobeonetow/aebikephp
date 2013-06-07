<?php

class Application_Model_Useraccountmapper
{
    protected $table;
    public function __construct(){
        $this->table = new Application_Model_DbTable_Useraccount();
    }
    
    public function insert($data){
        try{
            $id = $this->table->insert($data);
            return $id;
        }  catch (Exception $e){
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M010000 insert new user fail. ".$e->getMessage());
        }
    }
    
    public function find($id){
        try{
            $rows = $this->table->find($id);
            if($rows !== False){
                $rowarray = $rows[0]->toArray();
                $model = new Application_Model_Useraccount($rowarray);
            }
            return $model;
        }  catch (Exception $e){
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M010001 find user fail. ".$e->getMessage());
        }
    }
    
    public function createUser($data){
        try {
            $data['status'] = STATUS_NEW;
            $rows = $this->table->fetchAll($this->table->select()->where('username = ?',$data['username']));
            if(count($rows) > 0){
                throw new Exception("M011001 User already exists, cannot create");
            }
            return $this->insert($data);
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M010002 create user fail. ".$e->getMessage());
        }
    }
    
    /**
     * 获取用户信息
     * @param INT $userid 用户id
     * @return Array 数组<br>
     * (usertravelexp column headers, username, name, phonenumber, province,city, age, sex,onlinestatus)
     * @throws Exception
     */
    public function getUserInfo($userid){
        try {
            $query = "SELECT e.*, u.username as username, u.name as name, u.phonenumber as phonenumber, u.province as province, u.city as city, u.age as age, u.sex as sex, u.onlinestatus as onlinestatus
                FROM useraccount as u, usertravelexp as e
                WHERE u.id = $userid AND u.id = e.userid";
            $rows = $this->table->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M010003 get user info fail. ".$e->getMessage());
        }
    }
    
    /**
     * 登录用户，返回用户信息
     * @param type $username
     * 用户名
     * @param type $password
     * 密码
     */
    public function login($username, $password){
        try{
            $rows = $this->table->fetchAll($this->table->select()->where('username = ?',$username));
            if($rows !== False && count($rows) > 0){
//                Zend_Registry::get('logger')->info($row['username']);
                $model = new Application_Model_Useraccount($rows[0]->toArray());
//                 Zend_Registry::get('logger')->info(count($rows) . " ---- ".$username . " -DB-P- " .$model->getPassword()." -US-P- ".$password);
                if($password == $model->getPassword()){
                    Zend_Registry::get('logger')->info("Password same , ok");
                    return $model;
                }else{
                    Zend_Registry::get('logger')->info("Password wrong.");
                    return NULL;
                }
            }
            return NULL;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M010004 login user fail. ".$e->getMessage());
        }
    }
}

