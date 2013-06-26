<?php

class IndexController extends Mylibrary_Controller_Action
{
    protected $_service;
    protected $logger;


    public function init()
    {
        /* Initialize action controller here */
        $this->_helper->viewRenderer->setNoRender();
        $this->_service = new Application_Model_ProcessModel();
        $this->logger = Zend_Registry::get("logger");
    }
    
    public function indexAction()
    {
        // action body
        $str = "Connection successful";
        $this->_helper->json->sendJson($str);
    }
    
    public function loginAction(){
//        $data = array(
//             'username'=>'jianxing',
//            'password'=>'jianxing'
//        );
        $result = $this->_service->login($this->_postdata);
        $arr['result'] = $result;
        $this->_helper->json->sendJson($arr);
    }
    
    public function createuserAction(){
        try{
            $result  = $this->_service->createUser($this->_postdata);
            $arr['result'] = $result;
        }  catch (Exception $e){
            $arr['result'] = "0";
            $arr['msg'] = $e->getMessage();
            $this->logger->info($e->getTraceAsString());
        }
        $this->_helper->json->sendJson($arr);
    }
    
    public function createplanAction(){
        try{
            $result = $this->_service->createPlan($this->_postdata);
            $arr['result'] = $result;
        }  catch (Exception $e){
            $arr['result'] = 0;
            $arr['msg'] = $e->getMessage();
            $this->logger($e->getTraceAsString());
        }
        $this->_helper->json->sendJson($arr);
    }
    
    public function getcurrentplanlistAction(){
        try{
            if(empty($this->_postdata['userid']))
                throw new Exception("user id and start index not specify");
            if(empty($this->_postdata['start']))
                $this->_postdata['start'] = 0;
            $result = $this->_service->getCurrentPlanList($this->_postdata);
            if(count($result) > 0){
                $arr['data'] = $result;
                $arr['result'] = 1;
            }else{
                $arr['result'] =2;
            }
        }  catch (Exception $e){
            $arr['result'] = 0;
            $arr['msg'] = $e->getMessage();
            $this->logger->info($e->getMessage());
        }
        $this->_helper->json->sendJson($arr);
    }
    
       
    public function getfriendlistAction(){
        try{
            if(empty($this->_postdata['userid'])){
                throw new Exception("userid is not provided");
            }
            $result = $this->_service->getFriendList($this->_postdata);
            if(count($result) > 0){
                $arr['result'] = 1;//got something
                $arr['data'] = $result;
            }else{
                $arr['result'] = 2;//empty
            }
        }  catch (Exception $e){
            $arr['result'] = 0;
            $arr['msg'] = $e->getMessage();
            $this->logger->err($e->getMessage());
        }
        $this->_helper->json->sendJson($arr);
    }
    
    public function getuserplanlistAction(){
        try{
            if(empty($this->_postdata['userid'])){
                throw new Exception("userid is not provided");
            }
            $result = $this->_service->getMyPlanList($this->_postdata['userid']);
            if(count($result) > 0){
                $arr['result'] = 1;//got something
                $arr['data'] = $result;
            }else{
                $arr['result'] = 2;//empty
            }
        }  catch (Exception $e){
            $arr['result'] = 0;
            $arr['msg'] = $e->getMessage();
            $this->logger->err($e->getMessage());
        }
        $this->_helper->json->sendJson($arr);
    }
    
    public function getjoinedplanlistAction(){
        try{
            if(empty($this->_postdata['userid'])){
                throw new Exception("userid is not provided");
            }
            $result = $this->_service->getJoinedPlanList($this->_postdata['userid']);
            if(count($result) > 0){
                $arr['result'] = 1;//got something
                $arr['data'] = $result;
            }else{
                $arr['result'] = 2;//empty
            }
        }  catch (Exception $e){
            $arr['result'] = 0;
            $arr['msg'] = $e->getMessage();
            $this->logger->err($e->getMessage());
        }
        $this->_helper->json->sendJson($arr);
    }
    
    public function getfinishplanlistAction(){
        try{
            if(empty($this->_postdata['userid'])){
                throw new Exception("userid is not provided");
            }
            $result = $this->_service->getFinishedPlanList($this->_postdata['userid']);
            if(count($result) > 0){
                $arr['result'] = 1;//got something
                $arr['data'] = $result;
            }else{
                $arr['result'] = 2;//empty
            }
        }  catch (Exception $e){
            $arr['result'] = 0;
            $arr['msg'] = $e->getMessage();
            $this->logger->err($e->getMessage());
        }
        $this->_helper->json->sendJson($arr);
    }
    
    public function submitsummaryAction(){
        $data = array(
                'userid'=>1,
                'planid'=>1,
                'timecost'=>1232131,
                'distancebybattery'=>321321,
                'distancetravelled'=>1323213,
                'batteryusage'=>3323,
                'batterytimeon'=>12321,
                'remark'=>'remark',
                'endtime'=>"2012-10-09"
        );
        $result = $this->_service->submitSummary($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function acceptplanAction(){
        try{
            if(empty($this->_postdata['planid']) || empty($this->_postdata['userid']))
                throw new Exception ("Planid, userid not set");
            $result['accpeted'] = $this->_service->accpetPlan($this->_postdata);
            $result['memebers'] = $this->_service->getMemberForPlan($this->_postdata);
            $arr['data'] = $result;
            $arr['result'] = 1;
        }  catch (Exception $e){
            $arr['result'] = "0";
            $arr['msg'] = $e->getMessage();
        }
        $this->_helper->json->sendJson($arr);
    }
    
        
    public function quitplanAction(){
        try{
             if(empty($this->_postdata['planid']) || empty($this->_postdata['userid']))
                throw new Exception ("Planid, userid not set");
            $this->_service->quitPlan($this->_postdata);
            $result['memebers'] = $this->_service->getMemberForPlan($this->_postdata);
            $arr['result'] = 1;
            $arr['data'] = $result;
        }catch(Exception $e){
            $arr['result'] = 0;
            $arr['msg'] = $e->getMessage();
        }
        $this->_helper->json->sendJson($arr);
    }
    
    public function startplanAction(){
        $data = array(
            'userid'=>1,
            'planid'=>1,
            'starttime'=>'2012-10-01'
        );
        $result = $this->_service->startPlan($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function getplanhistoryAction(){
        $data = array('userid'=>1);
        $result = $this->_service->getPlanHistory($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function getplanbyidAction(){
        try{
            if(empty($this->_postdata['planid']))
                throw new Exception("planid is empty");
            $result = $this->_service->getPlanById($this->_postdata);
            $arr['data'] = $result;
            $arr["result"] = 1;
        }catch(Exception $e){
            $arr['result'] = 0;
            $arr['msg'] = $e->getMessage();
        }
        $this->_helper->json->sendJson($arr);
    }
    
    public function gethistorybyplanidAction(){
        $data = array('userid'=>1,'planid'=>1);
        $result = $this->_service->getHistoryByPlanId($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function createachievementAction(){
        $data = array(
            'name' => 'reateapikind',
            'description' => 'apiking',
            'level' => 1
        );
        $result = $this->_service->createAchievement($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function assignachievementAction(){
        $data = array(
            'userid' => 1,
            'achievementid'=>1,
            'planid'=>1
        );
        $result = $this->_service->assignAchievement($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function getachievementsAction(){
        $data = array(
            'userid' => 1
        );
        $result = $this->_service->getAchievements($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function getachievementbyidAction(){
        $data = array(
            'achievementid'=>1
        );
        $result = $this->_service->getAchievementById($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function grabachievementAction(){
        $data = array(
            'userid'=>1,
            'planid'=>1,
            'achievementid'=>1
        );
        $result = $this->_service->grabAchievement($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function invitefriendsAction(){
        $data = array(
            'userid'=>1,
            'friends'=>'2',
            'planid'=>1
        );
        $result = $this->_service->inviteFriends($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function getinvitesAction(){
        $data = array(
            'userid'=>2
        );
        $result = $this->_service->getInvites($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function rejectinviteAction(){
        $data = array(
            'inviteid'=>2
        );
        $result = $this->_service->rejectInvite($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function addfriendAction(){
        $data = array(
            'userid'=>1,
            'friendid'=>2
        );
        $result = $this->_service->addFriend($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function rejectfriendAction(){
        $data = array(
            'requestid'=>2
        );
        $result = $this->_service->rejectFriendRequest($data);
        $this->_helper->json->sendJson($result);
    }
    
//    public function getfriendrequestAction(){
//        $data = array(
//            'userid'=>2
//        );
//        $result = $this->_service->getFriendRequest($data);
//        $this->_helper->json->sendJson($result);
//    }
    
    public function acceptfriendAction(){
        $data = array(
            'userid'=>2,
            'requestid'=>1
        );
        $result = $this->_service->acceptFriend($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function removefriendAction(){
        $data = array(
            'userid'=>1,
            'friendid'=>2
        );
        $result = $this->_service->removeFriend($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function getuserdetailAction(){
        $data = array(
            'userid'=>1
        );
        $result = $this->_service->getUserDetail($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function getsummaryupdateinfoAction(){
        $data = array(
            'userid'=>1
        );
        $result = $this->_service->getSummaryUpdateInfo($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function updateusertravelexpAction(){
        $data = array(
            'userid'=>1
        );
        $result = $this->_service->updateUserTravelExp($data);
        $this->_helper->json->sendJson($result);
    }
 
    
    public function confirmplansummaryAction(){
        $data = array(
            'userid'=>1,
            'planid'=>1
        );
        $result = $this->_service->confirmPlanSummary($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function terminateplansummaryAction(){
        $data = array(
            'userid'=>1,
            'planid'=>1
        );
        $result = $this->_service->terminatePlanSummary($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function getfriendrequestAction(){
        $data = array(
            'userid'=>2
        );
        $result = $this->_service->getFriendRequest($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function acceptinviteAction(){
        $data = array(
            'friendid'=>2,
            'planid'=>1
        );
        $result = $this->_service->acceptInvite($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function deleteplanAction(){
        try{
            $result = $this->_service->deletePlan($this->_postdata);
            $arr['result'] = 1;
        }catch(Exception $e){
            $arr['result'] = 0;
            $arr['msg'] = $e->getMessage();
        }
        $this->_helper->json->sendJson($arr);
    }

}