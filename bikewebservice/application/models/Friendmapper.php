<?php

class Application_Model_Friendmapper
{

    protected $table;
    public function __construct() {
        $this->table = new Application_Model_DbTable_Friend();;
    }
    
    /**
     * 获取朋友list
     * @param INT $userid
     * @return Array 2d数组<br>
     * (friendid, name)
     * @throws Exception
     */
    public function getFriends($userid, $startRow){
        try {
            $statusid= STATUS_NEW_FRIEND;
            $query = "SELECT f.friendid as friendid, u.name as name 
                FROM friend as f, useraccount as u
                WHERE f.userid = $userid and f.friendid = u.id and f.status = $statusid 
                LIMIT 30 OFFSET $startRow";
            $rows  = $this->table->getAdapter()->query($query)->fetchAll();
            return $rows;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M080001 get friends fail for user $userid. ".$e->getMessage());
        }
    }
    
    /**
     * 删除朋友，朋友列别里也会把你移除
     * @param INT $userid 用户id
     * @param INT $friendid 朋友id
     * @return boolean 如果成功侧返回true
     * @throws Exception
     */
    public function removeFriend($userid, $friendid){
        try {
            $db = $this->table->getAdapter();
            $query = "DELETE FROM friend WHERE userid=$userid AND friendid = $friendid";
            $db->query($query);
            $query = "DELETE FROM friend WHERE userid=$friendid AND friendid = $userid";
            $db->query($query);
            return true;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M080002 remove friend fail for user $userid and friend $friendid. ".$e->getMessage());
        }
    }
    
    /**
     * 接受朋友请求
     * @param INT $userid 用户id
     * @param INT $requestid 请求id
     * @return $id 新创建的朋友id，对方加你的那个
     * @throws Exception
     */
    public function acceptFriend($userid, $requestid){
        try {
            $db = $this->table->getAdapter();
            $statusrequest= STATUS_REQUEST_FRIEND;
            $statusaccept = STATUS_NEW_FRIEND;
            $data = array (
                'status'=>$statusaccept
            );
            $where = "userid = $requestid and friendid = $userid and status = '$statusrequest'";
            $count = $this->table->update($data, $where);
            if($count === False) 
                throw new Exception("M080003 Cannot accpet friends");
            
            $data = array (
                'userid'=>$userid,
                'friendid'=>$requestid,
                'status'=>$statusaccept
            );
            $id = $this->table->insert($data);
            return $id;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M080004 accept friend fail for user $userid and request $requestid. ".$e->getMessage());
        }
    }
    
    /**
     * 获取朋友请求
     * @param INT $userid 用户ID
     * @return Array 2d数组<br>
     * (friend column header)
     * @throws Exception
     */
    public function getFriendRequest($userid){
        try {
            $statusid= STATUS_REQUEST_FRIEND;
            $rows  = $this->table->fetchAll($this->table->select()->where('friendid=?',$userid)->where('status=?',$statusid));
            return $rows->toArray();
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M080005 get frined request fail for user $userid. ".$e->getMessage());
        }
    }
    
    /**
     * 拒绝请求
     * @param INT $requestid 请求id
     * @return boolean 如果成功，返回true
     * @throws Exception
     */
    public function rejectRequest($requestid){
        try {
            $query = "DELETE FROM friend WHERE id=$requestid";
            $count = $this->table->getAdapter()->query($query);
            if($count === False) return false;
            return true;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M080006 reject frined request fail for request $requestid. ".$e->getMessage());
        }
    }
    
    /**
     * 添加朋友
     * @param INT $userid 用户id
     * @param INT $friendid 想添加的朋友id
     * @return $id 返回发送的请求的id
     * @throws Exception
     */
    public function addFriend($userid, $friendid){
        try {
            $data = array (
                'userid'=>$userid,
                'friendid'=>$friendid,
                'status'=>STATUS_REQUEST_FRIEND
            );
            $id= $this->table->insert($data);
            return $id;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M080007 add frined fail user $userid, friend $friendid. ".$e->getMessage());
        }
    }
}

