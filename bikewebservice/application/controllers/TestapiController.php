<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
class TestapiController extends Zend_Controller_Action
{
    protected $_service;
    protected $logger ;
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
        $str = "Connection successful ".STATUS_NEW;
        $this->_helper->json->sendJson($str);
    }
    
    public function loginAction(){
        $result = $this->_service->login($_POST);
        $arr['result'] = $result;
        $this->_helper->json->sendJson($arr);
    }
    
    public function createuserAction(){
        $data = array(
          'username'=>'jianxing',
            'password'=>'jianxing',
            'name'=>'Kobeonetow',
            'emailaddress'=>'jianxing@aebike.com'
        );
//        $data = array(
//          'username'=>'jianxing2',
//            'password'=>'jianxing',
//            'name'=>'Kobeonetow',
//            'emailaddress'=>'jianxing@aebike.com'
//        );
        $result  = $this->_service->createUser($data);
        $arr['result']= $result;
        $this->_helper->json->sendJson($arr);
    }
    
    public function createplanAction(){
        $data = array(
            'plantype' => 'C',
            'startlocation' => '101.03,3.001',
            'endlocation' => '101.03,3.500',
            'userid' => 3,
            'name' => 'Test Plan',
            'distance' => 132423423,
            'expectedtime' => 3123123,
            'pplgoing' => 40,
            'pplexpected' => 50,
            'remark' => 'remarks',
            'starttime' => '2012-10-09',
            'endtime' => '2012-10-15',
            'sponsor' => 'sopner ni ma',
            'prizes' => '1000'
        );
        $result = $this->_service->createPlan($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function getcurrentplanlistAction(){
        try{
            $data['pagenumber'] = "1";
            $data['lotsize'] = "10";
            $result = $this->_service->getCurrentPlanList($data);
            $arr['result'] = $result;
        }  catch (Exception $e){
            $arr['result'] = 0;
            $arr['msg'] = $e->getMessage();
            $this->logger->info($e->getMessage());
        }
        $this->_helper->json->sendJson($arr);
    }
    
    public function getplanlistAction(){
        $data = array(
           'pagenumber'=>'1',
           'lotsize'=>'10'
        );
        $result = $this->_service->getPlanList($data);
        $this->_helper->json->sendJson($result);
    }
    
    public function getownplanlistAction(){
         $data = array(
           'userid'=>'1'
        );
        $result = $this->_service->getOwnPlanList($data);
        $this->_helper->json->sendJson($result);
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
        $data = array(
            'userid'=>1,
            'planid'=>1,
        );
        $result['accpeted'] = $this->_service->accpetPlan($data);
        $result['memebers'] = $this->_service->getMemberForPlan($data);
        $this->_helper->json->sendJson($result);
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
        $data = array('planid'=>1); 
        $result = $this->_service->getPlanById($data);
        $this->_helper->json->sendJson($result);
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
    
    public function getfriendlistAction(){
        $data = array(
            'userid'=>1
        );
        $result = $this->_service->getFriendList($data);
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
    
    public function testisdeletableAction(){
        $planassignmapper = new Application_Model_Planassignmentmapper();
        $result = $planassignmapper->isAssignmentDeletable("15");
        $this->_helper->json->sendJson($result);
    }
}
?>
