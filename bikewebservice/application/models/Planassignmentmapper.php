<?php

class Application_Model_Planassignmentmapper
{
    protected $table;
    public function __construct() {
        $this->table = new Application_Model_DbTable_Planassignment();;
    }

    /**
     * 指定计划到用户
     * @param Array $data
     * @return $id 返回新建的计划ID
     * @throws Exception
     */
    public function assignPlan($data){
        try {
            $data['status'] = STATUS_PLAN_ACCEPT;
            $id = $this->table->insert($data);
            return $id;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M040001 Assign plan fail. ".$e->getMessage());
        }
    }
    
    /**
     * 改变计划状态为完成，并更新完成时间
     * @param INT $userid
     * @param INT $planid
     * @param INT $endtime
     * @return $count Number of records updated
     * @throws Exception
     */
    public function planComplete($userid, $planid,$endtime){
        try {
            $data = array(
                'endtime'=>$endtime,
                'status'=>STATUS_PLAN_FINISH
            );
            $where = "userid = $userid and planid = $planid";
            $count = $this->table->update($data, $where);
            return $count;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M040002 set plan to complete fail. ".$e->getMessage());
        }
    }
    
    /**
     * 获取计划的状态统计，统计一个计划的不同状态的人员数
     * <br>数组，每一组分别有两个值(status,count)
     * @param INT $planid 计划ID
     * @return type
     * @throws Exception
     */
    public function getMemberCount($planid){
        try {
            $query = "SELECT *
                FROM planassignment
                WHERE planid = $planid";
            $rows = $this->table->getAdapter()->query($query)->fetchAll();
            return count($rows);
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M040003 Get status count for plan $planid fail. ".$e->getMessage());
        }
    }
    
    /**
     * 开始一个计划，计划id和用户id要提供
     * @param Array $data
     * @return $count 返回更新了的计划数
     * @throws Exception
     */
    public function startPlan($data){
        try {
            $data['status'] = STATUS_PLAN_START;
            $userid = $data['userid'];
            $planid = $data['planid'];
            $where = "userid = $userid and planid = $planid and status != ".STATUS_PLAN_START;
            $count = $this->table->update($data, $where);
            return $count;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M040004 Start plan $planid fail. ".$e->getMessage());
        }
    }
    
    /**
     * 返回用户参加过的计划资料
     * <br>(planid,planname,planstatus,userplanstatus)
     * @param INT $userid 用户id
     * @return Array
     * @throws Exception
     */
    public function getHistoryForUser($userid){
        try {
            $terminatedstatusid  = STATUS_PLAN_TERMINATED;
            $finishedstatusid  = STATUS_PLAN_FINISH;
            $query = "SELECT p.id as planid, p.name as planname, p.status as planstatus, s.status as userplanstatus  
                FROM plan as p, planassignment as s 
                WHERE p.id = s.planid AND s.userid = $userid AND (s.status = $terminatedstatusid OR s.status = $finishedstatusid)";
            $rows = $this->table->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M040005 Get history plans fail for user $userid. ".$e->getMessage());
        }
    }
    
    /**
     * 获取参加的计划信息
     * @param INT $assignid 参加记录的ID
     * @return \Application_Model_Planassignment|boolean
     * @throws Exception
     */
    public function getAssignmentById($assignid){
        try {
            $rows = $this->table->find($assignid);
            if($rows !== False && count($rows)>0){
                $rowarray = $rows[0]->toArray();
                return new Application_Model_Planassignment($rowarray);
            }else{
                return False;
            }
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M040006 Get assignment fail by id  $assignid. ".$e->getMessage());
        }
    }
    
    /**
     * 获取参加的计划信息
     * @param INT $planid 计划id
     * @param INT $userid 用户id
     * @return \Application_Model_Planassignment|boolean
     * @throws Exception
     */
    public function getAssignment($planid, $userid){
       try {
            $rows = $this->table->fetchAll($this->table->select()->where("planid = $planid")->where("userid = $userid"));
            if($rows !== False && count($rows)>0){
                $rowarray = $rows[0]->toArray();
                return new Application_Model_Planassignment($rowarray);
            }else{
                return False;
            }
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M040007 Get assignment fail by planid $planid and userid $userid. ".$e->getMessage());
        } 
    }
    
    /**
     * 获取计划得历史记录
     * @param INT $planid
     * @param INT $userid
     * @return Array (plan column headers, joinuserid, planstarttime,planendtime,assignedplanstatus)2D数组
     * @throws Exception
     */
    public function getHisotryByPlanId($planid, $userid){
        try {
            $query = "SELECT p.*, s.userid as joinuserid, s.starttime as planstarttime, s.endtime as planendtime, s.status as assignedplanstatus  
                FROM plan as p, planassignment as s 
                WHERE p.id = s.planid AND p.id = $planid AND s.userid = $userid";
            $rows = $this->table->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M040008 Get history fail by planid $planid and userid $userid. ".$e->getMessage());
        }
    }
    
    /**
     * Check whether assignment deletable, if started or finish, not deletable
     * @param type $planid
     * @return boolean
     * @throws Exception
     */
    public function isAssignmentDeletable($planid){
        try {
            $query = "SELECT count(*) as count FROM planassignment as p 
                WHERE p.planid = $planid AND status !=".STATUS_PLAN_ACCEPT." AND status !=".STATUS_PLAN_INTEREST ;
            $row = $this->table->getAdapter()->query($query)->fetchAll();
            $count = $row[0]['count'];
            if($count == 0)
                return true;
            else
                return false;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M040009 check plan deletable fail planid $planid. ".$e->getMessage());
        }
    }
    
    /**
     * Delete assignments for given $planid
     * @param type $planid
     * @return type
     * @throws Exception
     */
    public function deleteAssignment($planid){
        try {
            $query = "DELETE FROM planassignment 
                WHERE planid = $planid";
            $rows = $this->table->getAdapter()->query($query);
            return $rows;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M040010 delete plan fail planid $planid. ".$e->getMessage());
        }
    }
    
    /**
     * Get joined plans by $userid
     * @param type $userid
     */
    public function getJoinedPlanList($userid){
         try {
            $query = "SELECT p.*, s.userid as joinuserid, s.starttime as planstarttime, s.endtime as planendtime, s.status as assignstatus  
                FROM plan as p, planassignment as s 
                WHERE p.id = s.planid AND s.userid = $userid 
                ORDER BY plandate DESC";
            $rows = $this->table->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M040011 Get joined plans fail by userid $userid. ".$e->getMessage());
        }
    }
}

