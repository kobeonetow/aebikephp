<?php

class Application_Model_DbTable_Planassignment extends Zend_Db_Table_Abstract
{

    protected $_name = 'planassignment';
    protected $_primary = 'id';
    protected $_sequence = true;

    public function assignPlan($data){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_PLAN_ACCEPT);
            $data['status'] = $statusid;
            $id = $this->insert($data);
            return $id;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function planComplete($userid, $planid){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_PLAN_FINISH);
            $data = array(
                'userid'=>$userid,
                'planid'=>$planid,
                'status'=>$statusid
            );
            $where = "userid = $userid and planid = $planid";
            $id = $this->update($data, $where);
            return $id;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function getMemberCount($planid){
        try {
            $query = "SELECT status, count(userid)
                FROM planassignment
                WHERE planid = $planid
                GROUP BY status
                ";
            $rows = $this->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function startPlan($data){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid  = $status->getStatusId(STATUS_PLAN_START);
            $data['status'] = $statusid;
            $userid = $data['userid'];
            $planid = $data['planid'];
            $where = "userid = $userid and planid = $planid and status != $statusid";
            $count = $this->update($data, $where);
            return $count;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function getHistoryForUser($userid){
        try {
            $status = new Application_Model_DbTable_Status();
            $startedstatusid  = $status->getStatusId(STATUS_PLAN_TERMINATED);
            $finishedstatusid  = $status->getStatusId(STATUS_PLAN_FINISH);
            $query = "SELECT p.id as planid, p.name as planname, p.status as planstatus, s.status as userplanstatus  
                FROM plan as p, planassignment as s 
                WHERE p.id = s.planid AND s.userid = $userid AND (s.status = $startedstatusid OR s.status = $finishedstatusid)";
            $rows = $this->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    /**
     * 
     * @param type $planid
     * @param type $userid
     * @return type
     * @throws Exception
     */
    public function getHisotryByPlanId($planid, $userid){
        try {
            $query = "SELECT p.*, s.userid as joinuserid, s.starttime as planstarttime, s.endtime as planendtime, s.status as assignedplanstatus  
                FROM plan as p, planassignment as s 
                WHERE p.id = s.planid AND p.id = $planid AND s.userid = $userid";
            $rows = $this->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
//    public function fetchAll($select){
//        return $this->fetchAll($select);
//    }
}

