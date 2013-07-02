<?php

class Application_Model_DbTable_Baepushmapper extends Zend_Db_Table_Abstract{
    
    protected $_name = 'baepushmapper';
    protected $_primary = 'id';
    
    public function isUserExist($userid){
        try{
            $rows = $this->fetchAll($this->select()->where("id = $userid"));
            if($rows !== False && count($rows) > 0){
                return true;
            }
            return false;
        }  catch (Exception $e){
            throw new Exception($e);
        }
    }
    
    public function getBaeUserId($userid){
        try{
            $row = $this->find($userid);
            return $row;
        }  catch (Exception $e){
            throw new Exception($e);
        }
    }
    
    public function updateBaeUserId($userid, $baeuserId){
        try{
            $row = $this->getBaeUserId($userid);
            if($row !== False && count($row) > 0){
                $data['baeuserid'] = $baeuserId;
                $data['id'] = $userid;
                $data['baetags'] = $row->baetags;
                $this->update($data, "id=$userid");
                return true;
            }else
                return false;
        }  catch (Exception $e){
            throw new Exception($e);
        }
    }
}
?>
