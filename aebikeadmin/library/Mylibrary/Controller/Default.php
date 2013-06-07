<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
class Cdlibrary_Controller_Default extends Zend_Controller_Action 
{
    protected $_session;
    
    public function init(){
        $this->_session = new Application_Model_ClientSessionModel();
        $this->setSessions();
    }
    
    public function setSessions(){
        if(!$this->_session->isStaffSet()){
            $this->_redirect($this->view->baseUrl("/login"));
        }else{
            $this->view->ses = $this->_session;
        }
    }
} 
?>
