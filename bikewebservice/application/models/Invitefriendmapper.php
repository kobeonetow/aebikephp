<?php

class Application_Model_Invitefriendmapper
{

    protected $table;
    public function __construct(){
        $this->table = new Application_Model_DbTable_Invitefriend();
    }
    
    /**
     * 邀请朋友参加计划
     * @param INT $userid
     * @param INT $friends
     * @param INT $planid
     * @return boolean 邀请发送成功侧返回true
     * @throws Exception
     */
    public function inviteFriends($userid, $friends, $planid){
        try {
            $statusid = STATUS_INVITE_FRIEND;
            $data = array('userid' => $userid, 'planid' => $planid, 'status' => $statusid);
            $countArray = array();
            $count=0;
            foreach($friends as $friend){
                $data['friendid'] = $friend;
                if(!$this->hasInvite($friend,$planid)){
                    $this->table->insert($data);
                    $countArray[$count++] = $friend;
                }
            }
            return $countArray;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M090000 invite friend fail. ".$e->getMessage());
        }
    }
    
    /**
     * 查询计划邀请
     * @param INT $userid 用户id
     * @return Array 数组
     * <br>(invitefriends column header)
     * @throws Exception
     */
    public function getInvites($userid){
        try {
            $statusid = STATUS_INVITE_FRIEND;
            $rows = $this->table->fetchAll($this->table->select()->where('friendid=?',$userid)->where('status=?', $statusid));
            return $rows->toArray();
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M090001 get friend invites fail. ".$e->getMessage());
        }
    }
    
    /**
     * 拒绝计划邀请
     * @param INT $inviteid 邀请id
     * @return boolean 拒绝成功侧返回true
     * @throws Exception
     */
    public function rejectInvite($inviteid){
        try {
            $query = "DELETE FROM invitefriend WHERE id=$inviteid";
            $count = $this->table->getAdapter()->query($query);
            if($count === False) return false;
            return true;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M090002 reject friend invite fail. ".$e->getMessage());
        }
    }
    
    /**
     * 接受计划邀请
     * @param INT $friendid 朋友id
     * @param INT $planid 计划id
     * @return $result 匹配的计划得匹配id
     * @throws Exception
     */
    public function acceptInvite($friendid, $planid){
        try {
            $statusid = STATUS_PLAN_ACCEPT;
            $data['status'] = $statusid;
            $where = "friendid = $friendid and planid = $planid";
            $countUpdate = $this->table->update($data, $where);
//            $planassign  = new Application_Model_Planassignmentmapper();
//            $result = $planassign->assignPlan(array('userid'=>$friendid,'planid'=>$planid));
            return $countUpdate;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M090003 accept friend invite fail. ".$e->getMessage());
        }
    }
    
    /**
     * Check whether friend has been invited
     * @param type $friend
     * @param type $planid
     * @return boolean
     * @throws Exception
     */
    public function hasInvite($friend, $planid){
        try {
            $rows = $this->table->fetchAll($this->table->select()->where('friendid=?',$friend)->where('planid=?', $planid));
            if($rows !== False && $rows != null && count($rows)>0)
                return true;
            else
                return false;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M090004 check has invites fail. ".$e->getMessage());
        }
    }
}

