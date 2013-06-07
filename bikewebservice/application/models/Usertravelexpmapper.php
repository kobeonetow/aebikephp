<?php

class Application_Model_Usertravelexpmapper
{
    protected $table;
    public function __construct() {
        $this->table = new Application_Model_DbTable_Usertravelexp();;
    }
    
    /**
     * 更新用户数据信息
     * @param Array $userid 用户ID
     * @return $ret 如果信息不存在则返回新建的id，否则返回更新记录数量
     * @throws Exception
     */
    public function updateTravelExpForUser($userid){
        try {
            $plansummarymapper  = new Application_Model_Plansummarymapper();
            $plansummaryinfo = $plansummarymapper->getSummaryUpdateInfo($userid);
            if(count($plansummaryinfo) <= 0) return false;
            $data = array(
                'totaldistance'=>$plansummaryinfo[0]['totaldistance'],
                'totaltime'=>$plansummaryinfo[0]['totaltime'],
                'totalbatteryused'=>$plansummaryinfo[0]['totalbatteryused'],
                'totalbatterytime'=>$plansummaryinfo[0]['totalbatterytime']
            );
            $count = $this->table->fetchAll($this->table->select()->where("userid = ?", $userid));
            $ret= "";
            if(count($count)>0){
                $where = "userid = $userid";
                $ret = $this->table->update($data, $where);
            }else{
                $data['userid'] = $userid;
                $ret = $this->table->insert($data);
            }
            return $ret;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M100001 Update travel exp fail. ".$e->getMessage());
        }
    }
}

