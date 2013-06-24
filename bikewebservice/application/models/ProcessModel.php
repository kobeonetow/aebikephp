<?php

class Application_Model_ProcessModel extends Application_Model_DbAdapter
{

    /**
     * 创建一个用户
     * @param Array $arr  Need to provide username,password,name,emailaddress
     * @return $id Id of the created user
     * @throws Exception
     */
    public function createUser($arr){
        $this->beginTxn();
        try{
            $usermapper = new Application_Model_Useraccountmapper();
            $data = array(
                'username'=>$arr['username'],
                'password'=>$arr['password'],
                'name'=>$arr['name'],
                'emailaddress'=>$arr['emailaddress']
            );
            $id = $usermapper->insert($data);
            $this->commitTxn();
            return $id;
        }catch(Exception $e){
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000001 Create user account fail. ".$e->getMessage());
        }
    }
    
    /**
     * 创建一个计划
     * @param Array $arr. Need to provide plantype,startlocation,endlocation,userid,name,distance,expectedtime.
     * <br>Normal的话需要额外的pplgoing,pplexpected,remark,starttime,endtime
     * <br>Challenge的话需要pplgoing,pplexpected,remark,starttime,endtime,sponsor,prizes
     * @return $planid 返回创建的plan的id
     * @throws Exception
     */
    public function createPlan($arr){
        $this->beginTxn();
        try {
            $planmapper = new Application_Model_Planmapper();
            $data = array(
                'type' => $arr['plantype'],
                'startlocation' => $arr['startlocation'],
                'endlocation' => $arr['endlocation'],
                'userid' => $arr['userid'],
                'name' => $arr['name'],
                'distance' => $this->getAttribute($arr, "distance", ""),
                'pplgoing' => $this->getAttribute($arr, "pplgoing", 0),
                'pplexpected' => $this->getAttribute($arr, "pplexpected", 2),
                'expecttime' => $this->getAttribute($arr, "expecttime", "")
            );
            
//            if (strcmp($arr['plantype'], 'N') == 0) {
//                $dataN = array(
//                    'pplgoing' => $this->getAttribute($arr, "pplgoing", ""),
//                    'pplexpected' => $this->getAttribute($arr, "pplexpected", ""),
//                    'description' => $this->getAttribute($arr, "remark", ""),
//                    'starttime' => $this->getAttribute($arr, "starttime", ""),
//                    'endtime' => $this->getAttribute($arr, "endtime", "")
//                );
//                $data = array_merge($data, $dataN);
//            } else 
            
//             if (strcmp($arr['plantype'], 'C') == 0) {
//                $dataC = array(
//                    'pplgoing' => $this->getAttribute($arr, "pplgoing", 1),
//                    'pplexpected' => $this->getAttribute($arr, "pplexpected", 2),
//                    'description' => $this->getAttribute($arr, "remark", ""),
//                    'starttime' => $this->getAttribute($arr, "starttime", ""),
//                    'endtime' => $this->getAttribute($arr, "endtime", ""),
//                    'sponsor' => $this->getAttribute($arr,"sponsor","")
//                );
//                $data = array_merge($data, $dataC);
//            }
            if (!empty($arr['achievementid'])) {
                $data['achievementid'] = $arr['achievementid'];
            }
            $planid = $planmapper->createPlan($data);
            $this->commitTxn();
            return $planid;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000002 Create plan fail. ".$e->getMessage());
        }
    }
    
    /**
     * 提取完成了的计划
     * @param Array $arr 需要pagenumber,lotsize
     * @return $plans 返回计划数组，每一个$plans[n]是一个计划
     */
    public function getPlanList($arr){
        try{
            $planmapper = new Application_Model_Planmapper();
            $list = $planmapper->getFinishedPlanList($arr['pagenumber'],$arr['lotsize']);
            $plans = array();
            if($list !== False){
                foreach ($list as $model){
                    $str = $model->toKeyValueArray();
                    array_push($plans, $str);
                }
            }
            return $plans;
        }catch(Exception $e){
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000003 Get completed plan list fail. ".$e->getMessage());
        }
    }
    
    /**
     * 提取用户创建的计划
     * @param Array $arr 需要userid
     * @return $plans 返回计划数组，每一个$plans[n]是一个计划
     */
    public function getOwnPlanList($arr){
        try{
            $planmapper = new Application_Model_Planmapper();
            $list = $planmapper->getOwnPlanList($arr['userid']);
            $plans = array();
            if($list !== False){
                foreach ($list as $model){
                    $str = $model->toKeyValueArray();
                    array_push($plans, $str);
                }
            }
            return $plans;
        }catch(Exception $e){
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000004 Get own plan list fail. ".$e->getMessage());
        }
    }
    
    /**
     * 提交计划总结
     * @param Array $arr 需要提供以下数据<br>
     * (userid,planid,timecost,distancebybattery,distancetravelled,batteryusage,batterytimeon,remark)
     * @return $id 建立的总结的ID
     * @throws Exception
     */
    public function submitSummary($arr){
        $this->beginTxn();
        try {
            $summarymapper = new Application_Model_Plansummarymapper();
            $data = array(
                'userid'=>$arr['userid'],
                'planid'=>$arr['planid'],
                'timecost'=>$arr['timecost'],
                'distancebybattery'=>$arr['distancebybattery'],
                'distancetravelled'=>$arr['distancetravelled'],
                'batteryusage'=>$arr['batteryusage'],
                'batterytimeon'=>$arr['batterytimeon'],
                'remarks'=>$arr['remark']
            );
            $id = $summarymapper->createSummary($data, $arr['endtime']);
            $this->commitTxn();
            return $id;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000005 Submit summary fail. ".$e->getMessage());
        }
    }
    
    /**
     * 返回用户各项数据总结的和
     * <br>
     * @param Array $arr 需要提供userid
     * @return $result (totaldistance,totaltime,totalbatterytime,totalbatteryused)2D数组
     * @throws Exception
     */
    public function getSummaryUpdateInfo($arr){
        try {
            $summarymapper = new Application_Model_Plansummarymapper();
            $userid = $arr['userid'];
            $result = $summarymapper->getSummaryUpdateInfo($userid);
            return $result;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000006 get summary fail. ".$e->getMessage());
        }
    }
    
    /**
     * 接受一个被邀请的计划，或者接新计划
     * @param Array $arr 需要提供userid, planid
     * @return $id 返回用户接受的新计划的记录id
     * @throws Exception
     */
    public function accpetPlan($arr){
        $this->beginTxn();
        try {
            $assignmapper = new Application_Model_Planassignmentmapper();
            $plan = new Application_Model_Planmapper();
            $data = array(
                'userid' => $arr['userid'],
                'planid' => $arr['planid']
            );
            $id = $assignmapper->assignPlan($data);
            $plan->joinPlan($data['planid']);
            $this->commitTxn();
            return $id;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000007 accept plan fail. ".$e->getMessage());
        }
    }
    
    /**
     * 获取计划的状态统计，统计一个计划的不同状态的人员数
     * <br>
     * @param Array $arr
     * @return Array 数组，每一组分别有两个值(status,count)
     */
    public function getMemberForPlan($arr){
        try {
            $assignmapper = new Application_Model_Planassignmentmapper();
            $data['planid'] = $arr['planid'];
            $membercount = $assignmapper->getMemberCount($data['planid']);
            return $membercount;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000008 get member for plan fail. " . $e->getMessage());
        }
    }
    
    /**
     * 开始一个计划，计划id和用户id要提供
     * @param Array $arr
     * @return $count 返回更新了的计划数
     */
    public function startPlan($arr){
        try {
            $assignmapper = new Application_Model_Planassignmentmapper();
            $data = array(
                'userid' => $arr['userid'],
                'planid' => $arr['planid'],
                'starttime' => $arr['starttime']
            );
            $count = $assignmapper->startPlan($data);
            return $count;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000009 start plan fail. " . $e->getMessage());
        }
    }
    
    /**
     * 返回用户参加过的计划资料
     * @param Array $arr 用户userid需要提供
     * @return Array (planid,planname,planstatus,userplanstatus)2D数组
     */
    public function getPlanHistory($arr){
        try {
            $assignmapper = new Application_Model_Planassignmentmapper();
            $userid = $arr['userid'];
            $result = $assignmapper->getHistoryForUser($userid);
            return $result;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000010 get history fail. " . $e->getMessage());
        }
    }
    
    /**
     * 获取一个计划模型
     * @param Array $arr 需要提供planid
     * @return \Application_Model_Plan|boolean 返回找到的计划模型
     * @throws Exception
     */
    public function getPlanById($arr){
        try {
            $plan = new Application_Model_Planmapper();
            $planid = $arr['planid'];
            $result = $plan->findPlan($planid);
            return $result->toKeyValueArray();
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000011 get plan fail. ".$arr['planid'] . " ".$e->getMessage());
        }
    }
    
    /**
     * 获取计划得历史记录
     * @param Array $arr 需要提供planid,userid
     * @return Array (plan column headers, joinuserid, planstarttime,planendtime,assignedplanstatus)2D数组
     * @throws Exception
     */
    public function getHistoryByPlanId($arr){
        try {
            $assignmapper = new Application_Model_Planassignmentmapper();
            $planid = $arr['planid'];
            $userid = $arr['userid'];
            $result = $assignmapper->getHisotryByPlanId($planid, $userid);
            return $result;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000012 get plan history fail. ".$arr['planid'] . " ".$e->getMessage());
        }
    }
    
    /**
     * 新建成就
     * @param Array $arr 需要提供(name,description,level)
     * @return $result 新建成就的ID
     */
    public function createAchievement($arr){
        $this->beginTxn();
        try {
            $achievementmapper = new Application_Model_Achievementmapper();
            $data = array(
                'name'=>$arr['name'],
                'description'=>$arr['description'],
                'level'=>$arr['level']
            );
            $result = $achievementmapper->createAchievement($data);
            $this->commitTxn();
            return $result;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000013 create achievement fail. ".$arr['planid'] . " ".$e->getMessage());
        }
    }
    
    /**
     * 分配成就到计划和用户
     * @param Array $arr 需要提供userid,planid,achievementid
     * @return $result 分配的成就的分配id
     * @throws Exception
     */
    public function assignAchievement($arr){
        try {
            $this->beginTxn();
            $achievementassignmapper = new Application_Model_Achievementassignmentmapper();
            $userid = $arr['userid'];
            $achievementid = $arr['achievementid'];
            $planid = $arr['planid'];
            $result = $achievementassignmapper->assignAchievement($userid, $planid, $achievementid);
            $this->commitTxn();
            return $result;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000014 assign achievement fail. ".$e->getMessage());
        }
    }
    
    /**
     * 返回用户的成就信息
     * @param Array $arr 提供userid
     * @return Array 2d数组<br>
     * (achievement column headers, planid, achievementassignstatus)
     * @throws Exception
     */
    public function getAchievements($arr){
        try {
            $achievementassignmapper = new Application_Model_Achievementassignmentmapper();
            $userid = $arr['userid'];
            $result = $achievementassignmapper->getAchievements($userid);
            return $result;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000015 get achievement fail. ".$e->getMessage());
        }
    }
    
    /**
     * 将分配的成就状态变为获得
     * @param Array $arr 需要提供userid,planid,achievementid
     * @return $result 更新的记录行数
     * @throws Exception
     */
    public function grabAchievement($arr){
        try {
            $this->beginTxn();
            $achievementassignmapper = new Application_Model_Achievementassignmentmapper();
            $userid = $arr['userid'];
            $achievementid = $arr['achievementid'];
            $planid = $arr['planid'];
            $result = $achievementassignmapper->grapAchievement($userid, $planid, $achievementid);
            $this->commitTxn();
            return $result;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000016 grab achievement fail. ".$e->getMessage());
        }
       
    }
    
    /**
     * 获取成就
     * @param Array $arr 需要提供achievementid
     * @return Array 成就的所有属性
     * @throws Exception
     */
    public function getAchievementById($arr){
        try {
            $achievementmapper = new Application_Model_Achievementmapper();
            $achievementid = $arr['achievementid'];
            $result = $achievementmapper->getAchievementById($achievementid);
            return $result->toKeyValueArray();
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000017 get achievement ".$arr['achievementid']." fail. ".$e->getMessage());
        }
    }
    
    /**
     * 获取朋友list
     * @param Array $arr
     * @return $result 2d数组<br>
     * (friendid, name)
     * @throws Exception
     */
    public function getFriendList($arr){
        try {
            $friendmapper = new Application_Model_Friendmapper();
            $userid = $arr['userid'];
            $startRow = 0;
            if(!empty($arr['startRow']))
                $startRow = $arr['startRow'];
            $result = $friendmapper->getFriends($userid, $startRow);
            return $result;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000018 get friends fail. ".$e->getMessage());
        }
    }
    
    /**
     * 获取用户信息
     * @param Array $arr 需要提供userid
     * @return $result 数组<br>
     * (usertravelexp column headers, username, name, phonenumber, province,city, age, sex,onlinestatus)
     * @throws Exception
     */
    public function getUserDetail($arr){
        try {
            $accountmapper = new Application_Model_Useraccountmapper();
            $friendid = $arr['userid'];
            $result = $accountmapper->find($friendid);
            return $result->toKeyValueArray();
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000019 get user detail fail. ".$e->getMessage());
        }
    }
    
    /**
     * 邀请朋友参加计划
     * @param Array $arr 需要提供planid, friends(用逗号分开),userid
     * @return boolean 邀请发送成功侧返回true
     */
    public function inviteFriends($arr){
        try {
            $this->beginTxn();
            $invitemapper = new Application_Model_Invitefriendmapper();
            $planid = $arr['planid'];
            $friends = explode(",", $arr['friends']);
            $userid = $arr['userid'];
            $result = $invitemapper->inviteFriends($userid, $friends, $planid);
            $this->commitTxn();
            return $result;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000020 invite friends fail. ".$e->getMessage());
        }
    }
    
    /**
     * 移除朋友
     * @param type $arr 需要userid, friendid
     * @return $result 成功侧返回true
     */
    public function removeFriend($arr){
        try {
            $this->beginTxn();
            $friendmapper = new Application_Model_Friendmapper();
            $userid = $arr['userid'];
            $friendid = $arr['friendid'];
            $result = $friendmapper->removeFriend($userid, $friendid);
            $this->commitTxn();
            return $result;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000021 remove friend fail. ".$e->getMessage());
        }
    }
    
    /**
     * 接受朋友请求
     * @param Array $arr 需要提供userid, requestid
     * @return $result 新创建的朋友id，对方加你的那个
     */
    public function acceptFriend($arr){
        try {
            $this->beginTxn();
            $friendmapper = new Application_Model_Friendmapper();
            $userid = $arr['userid'];
            $requestid = $arr['requestid'];
            $result = $friendmapper->acceptFriend($userid, $requestid);
            $this->commitTxn();
            return $result;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000022 accept friend fail. ".$e->getMessage());
        }
    }
    
    /**
     * 查询计划邀请
     * @param Array $arr 需要userid
     * @return Array 数组
     * <br>(invitefriends column header)
     */
    public function getInvites($arr){
        try {
            $invitemapper = new Application_Model_Invitefriendmapper();
            $userid = $arr['userid'];
            $result = $invitemapper->getInvites($userid);
            return $result;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000023 get invites fail. ".$e->getMessage());
        }
    }
    
    /**
     * 拒绝计划邀请
     * @param Array $arr 需要inviteid
     * @return boolean 拒绝成功侧返回true
     */
    public function rejectInvite($arr){
        try {
            $this->beginTxn();
            $invitemapper = new Application_Model_Invitefriendmapper();
            $inviteid = $arr['inviteid'];
            $result = $invitemapper->rejectInvite($inviteid);
            $this->commitTxn();
            return $result;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000024 reject invite fail. ".$e->getMessage());
        }
    }
    
    /**
     * 拒绝朋友请求
     * @param Array $arr 需要提供requestid
     * @return boolean 如果成功，返回true
     */
    public function rejectFriendRequest($arr){
        try {
            $this->beginTxn();
            $friendmapper = new Application_Model_Friendmapper();
            $requestid = $arr['requestid'];
            $result = $friendmapper->rejectRequest($requestid);
            $this->commitTxn();
            return $result;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000025 reject friend request fail. ".$e->getMessage());
        }
    }
    
    /**
     * 发送添加朋友请求
     * @param Array $arr 需要提供userid, friendid
     * @return $id 返回发送的请求的id
     */
    public function addFriend($arr){
        try {
            $this->beginTxn();
            $friendmapper = new Application_Model_Friendmapper();
            $userid = $arr['userid'];
            $friendid  = $arr['friendid'];
            $result = $friendmapper->addFriend($userid, $friendid);
            $this->commitTxn();
            return $result;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000026 send friend request fail. ".$e->getMessage());
        }
    }
    
    /**
     * 更新用户数据信息
     * @param Array $arr 需要提供userid
     * @return $result 如果信息不存在则返回新建的id，否则返回更新记录数量
     * @throws Exception
     */
    public function updateUserTravelExp($arr){
        try {
            $this->beginTxn();
            $usertravelexpmapper = new Application_Model_Usertravelexpmapper();
            $userid = $arr['userid'];
            $result = $usertravelexpmapper->updateTravelExpForUser($userid);
            $this->commitTxn();
            return $result;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000027 update user travel experience fail. ".$e->getMessage());
        }
        
    }

    /**
     * 获取朋友请求
     * @param Array $arr 需要提供userid
     * @return $result 2d数组<br>
     * (friend column header)
     */
    public function getFriendRequest($arr){
        try {
            $friendmapper = new Application_Model_Friendmapper();
            $userid = $arr['userid'];
            $result = $friendmapper->getFriendRequest($userid);
            return $result;
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000028 get friend request fail. ".$e->getMessage());
        }
    }
    
    /**
     * 接受邀请计划请求
     * @param Array $arr 需要friendid, planid
     * @return $result 匹配的计划得匹配id
     */
    public function acceptInvite($arr){
        try {
            $this->beginTxn();
            $invitemapper = new Application_Model_Invitefriendmapper();
            $friendid = $arr['friendid'];
            $planid = $arr['planid'];
            $result = $invitemapper->acceptInvite($friendid,$planid);
            $this->commitTxn();
            return $result;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000029 accept invite fail. ".$e->getMessage());
        }
    }
    
    /**
     * 更新计划总结状态，确认计划完成
     * @param Array $arr userid, planid
     * @return $rows 返回更新了的记录数量
     */
    public function confirmPlanSummary($arr){
        try {
            $this->beginTxn();
            $summarymapper = new Application_Model_Plansummarymapper();
            $userid = $arr['userid'];
            $planid = $arr['planid'];
            $result  = $summarymapper->confirmPlanSummary($userid, $planid);
            $this->commitTxn();
            return $result;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000030 confirm plan fail. ".$e->getMessage());
        }
    }
    
    /**
     * 终结一个计划
     * @param Array $arr userid, planid
     * @return $rows 更新的记录数量
     */
    public function terminatePlanSummary($arr){
        try {
            $this->beginTxn();
            $summarymapper = new Application_Model_Plansummarymapper();
            $userid = $arr['userid'];
            $planid = $arr['planid'];
            $result  = $summarymapper->terminatePlanSummary($userid, $planid);
            $this->commitTxn();
            return $result;
        } catch (Exception $e) {
            $this->rollbackTxn();
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000031 terminate plan fail. ".$e->getMessage());
        }
    }
    
    /**
     * 登录用户，如果成功侧返回用户资料，否则返回result=0
     * @param Array $arr （username,password)
     * @return Array|0
     * @throws Exception
     */
   public function login($arr){
        try {
            $useraccountmapper = new Application_Model_Useraccountmapper();
            $username = $arr['username'];
            $password = $arr['password'];
            $result  = $useraccountmapper->login($username, $password);
//            Zend_Registry::get("logger")->info($username." = ".$password);
            if($result === NULL){
                return "0";
            }else{
                return $result->toKeyValueArray();
            }
        } catch (Exception $e) {
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000032 login fail. ".$e->getMessage());
        }
    }
    
    /**
     * 提取新建的计划
     * @param Array $arr 需要pagenumber,lotsize
     * @return $plans 返回计划数组，每一个$plans[n]是一个计划
     */
    public function getCurrentPlanList($arr){
        try{
            $planmapper = new Application_Model_Planmapper();
            $planassignmapper = new Application_Model_Planassignmentmapper();
            $list = $planmapper->getCurrentPlanList($arr['pagenumber'],$arr['lotsize']);
            $plans = array();
            if($list !== False){
                foreach ($list as $model){
                    $str = $model->toKeyValueArray();
                    $userid = $arr['userid'];
                    $planid = $str['id'];
                    $assignment = $planassignmapper->getAssignment($planid, $userid);
                    if($assignment === False){
                        $str['assignstatus'] = STATUS_PLAN_NOT_ASSIGN;
                    }else{
                        $str['assignstatus'] = $assignment->getStatus();
                    }
                    array_push($plans, $str);
                }
            }
            return $plans;
        }catch(Exception $e){
            Zend_Registry::get('logger')->err($e->getTraceAsString());
            throw new Exception("P000033 Get current plan list fail. ".$e->getMessage());
        }
    }
    
    /**
     * 删除一个计划和所有关于这个计划的人员
     * @param Array $arr Contais Userid and Planid
     * @return int Return 1 for success 0 for fail
     * @throws Exception
     */
    public function deletePlan($arr){
        $this->beginTxn();
        try{
            $planid = $this->getAttribute($arr, "planid", "0");
            $userid = $this->getAttribute($arr, "userid", "0");
            $planassignmentmapper = new Application_Model_Planassignmentmapper();
            $planmapper = new Application_Model_Planmapper();
            if($planmapper->isOwnerOfPlan($planid,$userid)){
                if($planassignmentmapper->isAssignmentDeletable($planid)){
                    $planassignmentmapper->deleteAssignment($planid,$userid);
                    $planmapper->deletePlan($planid,$userid);
                }else{
                    $this->rollbackTxn();
                    return 0;
                }
            }else{
                $this->rollbackTxn();
                return 0; 
            }
            $this->commitTxn();
            return 1;
        }catch(Exception $e){
           $this->rollbackTxn();
           throw new Exception("P000034 Delete Plan id:".$this->getAttribute($arr, "planid", "null")."fail.".$e->getMessage());
        }
    }
    
    /**
     * 退出一个计划
     * @param Array $arr Contais Userid and Planid
     * @return int Return 1 for success 0 for fail
     * @throws Exception
     */
    public function quitPlan($arr){
        $this->beginTxn();
        try{
            $planid = $this->getAttribute($arr, "planid", 0);
            $userid = $this->getAttribute($arr, "userid", 0);
            $planassignmentmapper = new Application_Model_Planassignmentmapper();
            $plan = new Application_Model_Planmapper();
            $planassignmentmapper->deleteAssignment($planid,$userid);
            
            $plan->quitPlan($planid);
            $this->commitTxn();
            return 1;
        }catch(Exception $e){
           $this->rollbackTxn();
           throw new Exception("P000035 Quit Plan id:".$this->getAttribute($arr, "planid", "null")."fail.".$e->getMessage());
        }
    }
    
    public function getAttribute(Array $arr, $name, $default){
        if(isset($arr[$name])){
            return $arr[$name];
        }else{
            return $default;
        }
    }
}

