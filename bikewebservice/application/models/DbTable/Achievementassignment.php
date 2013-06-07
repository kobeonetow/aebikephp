<?php

class Application_Model_DbTable_Achievementassignment extends Zend_Db_Table_Abstract
{

    protected $_name = 'achievementassignment';
    protected $_primary = 'id';
    protected $_sequence = true;

    public function assignAchievement($userid, $planid, $achievementid){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_NEW);
            $data = array(
                'userid' => $userid,
                'planid' => $planid,
                'achievementid' => $achievementid,
                'status' => $statusid
            );
            $id = $this->insert($data);
            return $id;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function getAchievements($userid){
        try {
            $query = "SELECT a.*, s.planid as planid, s.status as achievementassignstatus 
                FROM achievement as a, achievementassignment as s 
                WHERE s.userid = $userid AND a.id = s.achievementid";
            $rows = $this->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function grapAchievement($userid, $planid, $achievementid){
        try {
            $status = new Application_Model_DbTable_Status();
            $statusid = $status->getStatusId(STATUS_ACHIEVEMENT_GRAB);
            $data['status'] = $statusid;
            $where = "userid = $userid and planid = $planid and achievementid = $achievementid";
            $count = $this->update($data, $where);
            return $count;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
}

