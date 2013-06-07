<?php

class Application_Model_Achievementassignmentmapper
{

    protected $table;
    public function __construct() {
        $this->table = new Application_Model_DbTable_Achievementassignment();;
    }
    
    /**
     * 分配一个成就到计划和用户
     * @param INT $userid
     * @param INT $planid
     * @param INT $achievementid
     * @return $id 分配的ID
     * @throws Exception
     */
    public function assignAchievement($userid, $planid, $achievementid){
        try {
            $data = array(
                'userid' => $userid,
                'planid' => $planid,
                'achievementid' => $achievementid,
                'status' => STATUS_NEW
            );
            $id = $this->table->insert($data);
            return $id;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M070001 Assign achievement fail for aid $achievementid. ".$e->getMessage());
        }
    }
    
    /**
     * 返回用户的成就信息
     * @param INT $userid 用户ID
     * @return Array 2d数组<br>
     * (achievement column headers, planid, achievementassignstatus)
     * 
     * @throws Exception
     */
    public function getAchievements($userid){
        try {
            $query = "SELECT a.*, s.planid as planid, s.status as achievementassignstatus 
                FROM achievement as a, achievementassignment as s 
                WHERE s.userid = $userid AND a.id = s.achievementid";
            $rows = $this->table->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M070001 get achievements fail. ".$e->getMessage());
        }
    }
    
    /**
     * 将分配的成就状态变为获得
     * @param INT $userid 用户id
     * @param INT $planid 计划id
     * @param INT $achievementid 成就id
     * @return $count 更新的记录行数
     * @throws Exception
     */
    public function grapAchievement($userid, $planid, $achievementid){
        try {
            $data['status'] = STATUS_ACHIEVEMENT_GRAB;
            $where = "userid = $userid and planid = $planid and achievementid = $achievementid";
            $count = $this->table->update($data, $where);
            return $count;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M070002 grap achievement fail. ".$e->getMessage());
        }
    }

}

