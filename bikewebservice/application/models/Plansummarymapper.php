<?php

class Application_Model_Plansummarymapper
{
    protected $table;
    public function __construct() {
        $this->table = new Application_Model_DbTable_Plansummary();;
    }

    /**
     * 创建一个计划得总结
     * @param Array $data
     * @param Timestamp $endtime
     * @return $id 新建的计划总结ID
     * @throws Exception
     */
    public function createSummary($data, $endtime){
        try {
            $endtime = strtotime($endtime);
            
            //Update plan end time
            $planassignmapper = new Application_Model_Planassignmentmapper();
            $count = $planassignmapper->planComplete($data['userid'], $data['planid'],$endtime);
            if($count === False){
                Zend_Registry::get('logger')->err("userid ".$data['userid']." planid ".$data['planid']." updated empty.");
                throw new Exception("M050000 Cannot mark plan as complete because nothing updated");
            }
            //Update summary
            $data['status'] = STATUS_NEW;
            $id = $this->table->insert($data);
            return $id;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M050001 Create summary fail. ".$e->getMessage());
        }
    }
    
    /**
     * 返回用户各项数据总结的和
     * <br>(totaldistance,totaltime,totalbatterytime,totalbatteryused)2D数组
     * @param INT $userid 用户ID
     * @return Array
     * @throws Exception
     */
    public function getSummaryUpdateInfo($userid){
        try {
            $statusid = STATUS_PLAN_FINISH;
            $statustermid = STATUS_PLAN_TERMINATED;
            $query = "SELECT sum(distancetravelled) as totaldistance, sum(timecost) as totaltime, sum(batterytimeon) as totalbatterytime, sum(batteryusage) as totalbatteryused
                FROM plansummary
                WHERE userid=$userid and (status = $statusid or status = $statustermid)";
            $rows = $this->table->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M050002 Get summary update info fail for userid $userid. ".$e->getMessage());
        }
    }
    
    /**
     * 更新计划总结状态，确认计划完成
     * @param INT $userid 用户ID
     * @param INT $planid 计划ID
     * @return $rows 返回更新了的记录数量
     * @throws Exception
     */
    public function confirmPlanSummary($userid, $planid){
        try {
            $data = array(
                'status'=>STATUS_PLAN_FINISH
            );
            $where = "userid = $userid and planid = $planid";
            $rows = $this->table->update($data, $where);
            return $rows;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M050003 Confirm plan fail for user $userid and plan $planid. ".$e->getMessage());
        }
    }
    
    /**
     * 终结一个计划
     * @param INT $userid 用户ID
     * @param INT $planid 计划ID
     * @return $rows 更新的记录数量
     * @throws Exception
     */
    public function terminatePlanSummary($userid, $planid){
        try {
            $data = array(
                'status'=>STATUS_PLAN_TERMINATED
            );
            $where = "userid = $userid and planid = $planid";
            $rows = $this->table->update($data, $where);
            return $rows;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M050004 Terminate plan fail for user $userid and plan $planid. ".$e->getMessage());
        }
    }
}

