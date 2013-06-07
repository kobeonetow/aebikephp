<?php

class Application_Model_DbTable_Useraccount extends Zend_Db_Table_Abstract
{

    protected $_name = 'useraccount';
    protected $_primary = 'id';
    protected $_sequence = true;

    public function createUser($data){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_NEW);
            $data['status'] = $statusid;
            $rows = $this->fetchAll($this->select()->where('username = ?',$data['username']));
            if(count($rows) > 0){
                throw new Exception("User already exists, cannot create");
            }
            return $this->insert($data);
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function getUserInfo($userid){
        try {
            $query = "SELECT e.*, u.username as username, u.name as name, u.phonenumber as phonenumber, u.province as province, u.city as city, u.age as age, u.sex as sex, u.onlinestatus as onlinestatus
                FROM useraccount as u, usertravelexp as e
                WHERE u.id = $userid AND u.id = e.userid";
            $rows = $this->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
}

