<?php

class Application_Model_DbAdapter
{
    protected $_db;
    protected $_adapter;
    
    public function __construct() {
        $this->_db = new Zend_Db_Table();
        $this->_adapter = $this->_db->getAdapter();
    }
    
    public function beginTxn() {
        $this->_adapter->beginTransaction();
    }
    
    public function commitTxn() {
        $this->_adapter->commit();
    }
    
    public function rollbackTxn() {
        $this->_adapter->rollBack();
    }
}

