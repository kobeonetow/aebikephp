<?php

class Application_Model_DbTable_Achievement extends Zend_Db_Table_Abstract
{

    protected $_name = 'achievement';
    protected $_primary = 'id';
    protected $_sequence = true;
    
    
    public function createAchievement($data){
        try {
            $id = $this->insert($data);
            return $id;
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
    
    public function getAchievementById($aid){
        try {
            $row = $this->find($aid);
            return $row->toArray();
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
}

