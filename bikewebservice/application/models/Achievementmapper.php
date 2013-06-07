<?php

class Application_Model_Achievementmapper
{
    protected $table;
    public function __construct() {
        $this->table = new Application_Model_DbTable_Achievement();;
    }
    
    /**
     * 新建成就
     * @param Array $data 
     * @return $id 新建的ID
     * @throws Exception
     */
    public function createAchievement($data){
        try {
            $id = $this->table->insert($data);
            return $id;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M060000 Create achievement fail. ".$e->getMessage());
        }
    }
    
    /**
     * 获取成就
     * @param INT $aid 成就的ID
     * @return \Application_Model_Achievement|boolean 成就的模型或者False
     * @throws Exception
     */
    public function getAchievementById($aid){
        try {
            $rows = $this->table->find($aid);
            if($rows !== False && count($rows)>0){
                $rowarray = $rows[0]->toArray();
                return new Application_Model_Achievement($rowarray);
            }else{
                return False;
            }
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("M060001 get achievement $aid fail. ".$e->getMessage());
        }
    }

}

