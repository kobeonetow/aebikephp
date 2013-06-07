<?php

class Application_Model_DbTable_Status extends Zend_Db_Table_Abstract
{

    protected $_name = 'status';

    protected $_primary = 'id';
    protected $_sequence = true;
    
    public function getStatusId($name){
        try {
            $row = $this->fetchRow($this->select()->where('name = ?', $name));
            if ($row !== False) {
                return $row['id'];
            } else {
                return 0;
            }
        } catch (Exception $e) {
            throw new Exception($e);
        }
    }
 
}

