<?php

class Application_Model_BaeuserMapper{
    protected $table;
    public function __construct() {
        $this->table = new Application_Model_DbTable_Baepushmapper();;
    }
    
    /**
     * Get the bae user id model and return null if not exist
     * @param type $userid
     * @return null|\Application_Model_BaeUserModel
     * @throws Exception
     */
    public function getBaeUserId($userid){
        try{
            $row = $this->table->getBaeUserId($userid);
            if($row == False)
                return null;
            else{
                $model = new Application_Model_BaeUserModel($row);
                return $model;
            }
        }  catch (Exception $e){
             throw new Exception("M110001 get bae user id fail for userid=$userid.".$e->getMessage());
        }
    }
    
    /**
     * Update the baeuser id, and create it if not exists
     * @param type $userid
     * @param type $baeuserid
     * @throws Exception
     */
    public function updateBaeUserid($userid, $baeuserid,$channelId){
        try{
            if($this->table->isUserExist($userid)){
                $this->table->updateBaeUserId($userid, $baeuserid, $channelId);
            }else{
                $data['id'] = $userid;
                $data['baeuserid'] = $baeuserid;
                $data['baechannelid'] = $channelId;
                $data['baetags'] = "";
                $this->table->insert($data);
            }
        }  catch (Exception $e){
             throw new Exception("M110002 update bae user id fail for userid=$userid and baeuserid=$baeuserid.".$e->getMessage());
        }
    }
    
     public function getBaeUserById($userid){
        try{
            $row = $this->table->getBaeUserId($userid);
            if($row == False || $row == null)
                return null;
            else{
                $model = new Application_Model_BaeUserModel($row);
                return $model;
            }
        }  catch (Exception $e){
             throw new Exception("M110001 get bae user id fail for userid=$userid.".$e->getMessage());
        }
    }
}
?>
