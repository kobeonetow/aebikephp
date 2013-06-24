<?php

class Application_Model_Planmapper
{

    protected $table;
    public function __construct() {
        $this->table = new Application_Model_DbTable_Plan();;
    }
    
    /**
     * 
     * @param string $data
     * @return $id 返回创建计划的id
     * @throws Exception
     */
    public function createPlan($data){
        try {
            $data['status'] = STATUS_NEW;
            return $this->table->insert($data);
        } catch (Exception $e) {
            Zend_Registry::get("logger")->info($e->getMessage());
            throw new Exception("M020000 Create plan fail. ".$e->getMessage());
        }
    }
    
    /**
     * 
     * @param type $id
     * @return \Application_Model_Plan|boolean 返回找到的计划模型
     * @throws Exception
     */
    public function findPlan($id){
        try {
            $rows = $this->table->find($id);
            if ($rows !== False && count($rows) > 0) {
                $rowarray = $rows[0]->toArray();
                $model = new Application_Model_Plan($rowarray);
                return $model;
            }else
                return False;
        } catch (Exception $e) {
            throw new Exception("M020001 get plan fail for planid ".$id." ".$e->getMessage());
        }
    }
    
   
    /**
     * 
     * @param type $pagenumber
     * @param type $lotsize
     * @return \Application_Model_Plan|boolean 返回计划得模型数组
     * @throws Exception
     */
    public function getFinishedPlanList($pagenumber, $lotsize){
        try {
            $start = ($pagenumber - 1) * $lotsize;
            $statusid = STATUS_PLAN_FINISH;
            $query = "SELECT *
                FROM plan as p 
                WHERE p.status != $statusid and p.type != '" . PLAN_TYPE_QUICK . "'
                LIMIT $lotsize OFFSET $start";
            $rows = $this->table->getAdapter()->query($query)->fetchAll();
            if ($rows !== False && count($rows) > 0) {
                $models = array();
                foreach ($rows as $row) {
                    $model = new Application_Model_Plan($row);
                    array_push($models, $model);
                }
                return $models;
            } else {
                return False;
            }
        } catch (Exception $e) {
            throw new Exception("M020002 get plan list fail for pagenumber $pagenumber and lotsize $lotsize. " . $e->getMessage());
        }
    }
    
    /**
     * 
     * @param type $userid
     * @return \Application_Model_Plan|boolean 返回计划得模型数组
     * @throws Exception
     */
    public function getOwnPlanList($userid){
        try {
            $query = "SELECT *
                FROM plan as p 
                WHERE p.userid = $userid";
            $rows = $this->table->getAdapter()->query($query)->fetchAll();
            if ($rows !== False && count($rows) > 0) {
                $models = array();
                foreach ($rows as $row) {
                    $model = new Application_Model_Plan($row);
                    array_push($models, $model);
                }
                return $models;
            } else {
                return False;
            }
        } catch (Exception $e) {
            throw new Exception("M020003 get own plan list fail for userid $userid. " . $e->getMessage());
        }
    }
    
    /**
     * 获取现有的正在进行的计划list
     * @param type $pagenumber
     * 页码
     * @param type $lotsize
     * 每页显示数目
     * @return array|boolean
     * 如果有，返回planModels，否则否会False
     * @throws Exception
     */
     public function getCurrentPlanList($pagenumber, $lotsize){
        try{
            $start = ($pagenumber - 1) * $lotsize;
            $statusid = STATUS_NEW;
            $query = "SELECT *
                FROM plan as p 
                WHERE p.status = $statusid and p.type != '" . PLAN_TYPE_QUICK . "'
                LIMIT $lotsize OFFSET $start";
            $rows = $this->table->getAdapter()->query($query)->fetchAll();
            if ($rows !== False && count($rows) > 0) {
                $models = array();
                foreach ($rows as $row) {
                    $model = new Application_Model_Plan($row);
                    array_push($models, $model);
                }
                return $models;
            } else {
                return False;
            }
        }  catch (Exception $e){
            throw new Exception("M020004 get current plan list fail for pagenumber $pagenumber and lotsize $lotsize. " . $e->getMessage());
        }
    }
    
    public function isOwnerOfPlan($planid, $userid){
        try {
            $query = "SELECT count(*) as count 
                FROM plan as p 
                WHERE p.id = $planid AND p.userid = $userid";
            $rows = $this->table->getAdapter()->query($query)->fetchAll();
            $count = 0;
            if ($rows !== False && count($rows) > 0) {
                $count = $rows[0]['count'];
                if($count > 0){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (Exception $e) {
            throw new Exception("M020005 is Owner of plan check fail for userid $userid. " . $e->getMessage());
        }
    }
    
    public function deletePlan($planid){
        try {
            $query = "DELETE FROM plan 
                WHERE id = $planid";
            $rows = $this->table->getAdapter()->query($query);
            if ($rows !== False && count($rows) > 0) {
                return 1;
            }else{
                return 0;
            }
        } catch (Exception $e) {
            throw new Exception("M020006 delete plan fail for planid $planid. " . $e->getMessage());
        }
    }
    
    public function joinPlan($planid){
        try{
           $model  = $this->findPlan($planid);
           $model->setPplgoing($model->getPplgoing()+1);
           $this->table->update($model->toKeyValueArray(), "id=$planid");
        }catch (Exception $e){
            throw new Exception("M020007 join plan fail for adding pplgoing.".$e->getMessage());
        }
    }
    
    public function quitPlan($planid){
        try{
           $model  = $this->findPlan($planid);
           if($model->getPplgoing()> 0){
            $model->setPplgoing($model->getPplgoing()-1);
            $this->table->update($model->toKeyValueArray(), "id=$planid");
           }
        }catch (Exception $e){
            throw new Exception("M020008 quit plan fail for adding pplgoing.".$e->getMessage());
        }
    }
}

