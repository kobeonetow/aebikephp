<?php

class Application_Model_DbTable_Plansummary extends Zend_Db_Table_Abstract
{

    protected $_name = 'plansummary';
    protected $_primary = 'id';
    protected $_sequence = true;
    
    public function createSummary($data, $endtime){
        $db = $this->getAdapter();
        $db->beginTransaction();
        try {
            $endtime = strtotime($endtime);
            $status = new Application_Model_DbTable_Status();
            
            //Update plan end time
            $planassign = new Application_Model_DbTable_Planassignment();
            $id = $planassign->planComplete($data['userid'], $data['planid']);
            if($id === False)
                throw new Exception("Cannot mark plan as complete");
            
            //Update summary
            $statusid = $status->getStatusId(STATUS_NEW);
            $data['status'] = $statusid;
            $id = $this->insert($data);
            $db->commit();
            return $id;
        } catch (Exception $e) {
            $db->rollBack();
            throw new Exception($e);
        }
    }
    
    public function getSummaryUpdateInfo($userid){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_PLAN_FINISH);
            $statustermid = $status->getStatusId(STATUS_PLAN_TERMINATED);
            $query = "SELECT sum(distancetravelled) as totaldistance, sum(timecost) as totaltime, sum(batterytimeon) as totalbatterytime, sum(batteryusage) as totalbatteryused
                FROM plansummary
                WHERE userid=$userid and (status = $statusid or status = $statustermid)";
            $rows = $this->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function confirmPlanSummary($userid, $planid){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_PLAN_FINISH);
            $data = array(
                'userid' =>$userid,
                'planid'=>$planid,
                'status'=>$statusid
            );
            $where = "userid = $userid and planid = $planid";
            $rows = $this->update($data, $where);
            return $rows;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function terminatePlanSummary($userid, $planid){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_PLAN_TERMINATED);
            $data = array(
                'userid' =>$userid,
                'planid'=>$planid,
                'status'=>$statusid
            );
            $where = "userid = $userid and planid = $planid";
            $rows = $this->update($data, $where);
            return $rows;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
}

