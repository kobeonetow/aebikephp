<?php

class Application_Model_DbTable_Invitefriend extends Zend_Db_Table_Abstract
{

    protected $_name = 'invitefriend';
    protected $_primary = 'id';
    protected $_sequence = true;

    public function inviteFriends($userid, $friends, $planid){
        $db = $this->getAdapter();
        $db->beginTransaction();
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_INVITE_FRIEND);
            $data = array('userid' => $userid, 'planid' => $planid, 'status' => $statusid);
            foreach($friends as $friend){
                $data['friendid'] = $friend;
                $this->insert($data);
            }
            $db->commit();
            return true;
        } catch (Exception $e) {
            $db->rollBack();
            throw new Exception($e);
        }
    }
    
    public function getInvites($userid){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_INVITE_FRIEND);
            $rows = $this->fetchAll($this->select()->where('friendid=?',$userid)->where('status=?', $statusid));
            return $rows->toArray();
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function rejectInvite($inviteid){
        try {
            $query = "DELETE FROM invitefriend WHERE id=$inviteid";
            $count = $this->getAdapter()->query($query);
            if($count === False) return false;
            return true;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function acceptInvite($friendid, $planid){
        $db = $this->getAdapter();
        $db->beginTransaction();
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_PLAN_ACCEPT);
            $data['status'] = $statusid;
            $where = "friendid = $friendid and planid = $planid";
            $result = $this->update($data, $where);
            if(!$result){
                throw Exception("Cannot accept because cannot response to the invite");
            }
            $planassign  = new Application_Model_DbTable_Planassignment();
            $result = $planassign->assignPlan(array('userid'=>$friendid,'planid'=>$planid));
            $db->commit();
            return $result;
        } catch (Exception $e) {
            $db->rollBack();
            throw new Exception($e);
        }
    }
}

