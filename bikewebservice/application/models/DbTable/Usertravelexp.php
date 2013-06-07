<?php

class Application_Model_DbTable_Usertravelexp extends Zend_Db_Table_Abstract
{

    protected $_name = 'usertravelexp';
    protected $_primary = 'id';
    protected $_sequence = true;

    public function updateTravelExpForUser($userid){
        try {
            $plansummary  = new Application_Model_DbTable_Plansummary();
            $plansummaryinfo = $plansummary->getSummaryUpdateInfo($userid);
            if(count($plansummaryinfo) <= 0) return false;
            $data = array(
                'totaldistance'=>$plansummaryinfo[0]['totaldistance'],
                'totaltime'=>$plansummaryinfo[0]['totaltime'],
                'totalbatteryused'=>$plansummaryinfo[0]['totalbatteryused'],
                'totalbatterytime'=>$plansummaryinfo[0]['totalbatterytime']
            );
            $count = $this->fetchAll($this->select()->where("userid = ?", $userid));
            $ret= "";
            if(count($count)>0){
                $where = "userid = $userid";
                $ret = $this->update($data, $where);
            }else{
                $data['userid'] = $userid;
                $ret = $this->insert($data);
            }
            return $ret;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
}

