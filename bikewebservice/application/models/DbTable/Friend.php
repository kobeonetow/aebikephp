<?php

class Application_Model_DbTable_Friend extends Zend_Db_Table_Abstract
{

    protected $_name = 'friend';
    protected $_primary = 'id';
    protected $_sequence = true;

    public function getFriends($userid){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid= $status->getStatusId(STATUS_NEW_FRIEND);
            $query = "SELECT f.friendid as friendid, u.name as name 
                FROM friend as f, useraccount as u
                WHERE f.userid = $userid and f.friendid = u.id and f.status = $statusid";
            $rows  = $this->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
       
    public function removeFriend($userid, $friendid){
        $db = $this->getAdapter();
        $db->beginTransaction();
        try {
            $query = "DELETE FROM friend WHERE userid=$userid AND friendid = $friendid";
            $db->query($query);
            $query = "DELETE FROM friend WHERE userid=$friendid AND friendid = $userid";
            $db->query($query);
            $db->commit();
            return true;
        } catch (Exception $e) {
            $db->rollBack();
            throw new Exception($e);
        }
    }
    
    public function acceptFriend($userid, $requestid){
        $db = $this->getAdapter();
        $db->beginTransaction();
        try {
            $status = new Application_Model_DbTable_Status();
            $statusrequest= $status->getStatusId(STATUS_REQUEST_FRIEND);
            $statusaccept = $status->getStatusId(STATUS_NEW_FRIEND);
            $data = array (
                'status'=>$statusaccept
            );
            $where = "userid = $requestid and friendid = $userid and status = $statusrequest";
            $count = $this->update($data, $where);
            if($count === False) throw new Exception("Cannot accpet friends");
            
            $data = array (
                'userid'=>$userid,
                'friendid'=>$requestid,
                'status'=>$statusaccept
            );
            $id = $this->insert($data);
            $db->commit();
            return $id;
        } catch (Exception $e) {
            $db->rollBack();
            throw new Exception($e);
        }
    }
    
    public function getFriendRequest($userid){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid= $status->getStatusId(STATUS_REQUEST_FRIEND);
            $rows  = $this->fetchAll($this->select()->where('friendid=?',$userid)->where('status=?',$statusid));
            return $rows->toArray();
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function rejectRequest($requestid){
        try {
            $query = "DELETE FROM friend WHERE id=$requestid";
            $count = $this->getAdapter()->query($query);
            if($count === False) return false;
            return true;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function addFriend($userid, $friendid){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_REQUEST_FRIEND);
            $data = array (
                'userid'=>$userid,
                'friendid'=>$friendid,
                'status'=>$statusid
            );
            $id= $this->insert($data);
            return $id;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
}

