<?php

class Application_Model_DbTable_Plan extends Zend_Db_Table_Abstract
{

    protected $_name = 'plan';
    protected $_primary = 'id';
    protected $_sequence = true;

    public function createPlan($data){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_NEW);
            $data['status'] = $statusid;
            return $this->insert($data);
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    /**
     * Return a single plan
     * @param type $planid
     * plan id must be given
     */
    public function getPlanById($planid){
        try {
            $row = $this->find($planid);
            if ($row !== False && count($row) > 0) {
                return $row->toArray();
            } else {
                return "0";
            }
        } catch (Exception $e) {
            throw new Exception($e);
        }
        
    }
    
    /**
     * Return a list of plan
     * @param type $pagenumber
     * to filter out plans
     * @param type $lotsize
     * the size of list
     */
    public function getPlanList($pagenumber, $lotsize){
        try {
            $start = ($pagenumber-1) * $lotsize;
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_PLAN_FINISH);
            $query = "SELECT *
                FROM plan as p 
                WHERE p.status != $statusid and p.plantype != '".PLAN_TYPE_QUICK."'
                LIMIT $lotsize OFFSET $start";
            $rows = $this->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    /**
     * Get the plans created by users
     * @param type $userid
     * the user id
     */
    public function getOwnPlanList($userid){
        try {
            $query = "SELECT *
                FROM plan as p 
                WHERE p.userid = $userid";
            $rows = $this->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
}

