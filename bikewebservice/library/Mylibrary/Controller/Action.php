<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
class Mylibrary_Controller_Action extends Zend_Controller_Action{
    protected $_appName;
    protected $_appKey;
    protected $_body;
    protected $_postdata = array();
    protected $_logger;

    public function preDispatch() {
        $this->_logger = Zend_Registry::get('logger');
        $this->getResponse()->setHeader('Content-type', 'application/json;charset=UTF-8');
        $this->getHeaders();
        $this->getPostData();
        if(!$this->callAccepted()) {
            $returnpacket = array(
                'status' => 0,
                'errmsg' => 'Either your private key is invalid or the data has been tampered with'
            );
            $this->_helper->json->sendJson($returnpacket);
        }
//        
//        $posttokens = explode("&", $this->_body);
//        $count = 1;//The calification
//        foreach ($posttokens as $posttoken) {
//            if($count == 1){
//                if(strpos(urldecode($posttoken),"empty=empty") !== False){
//                    $count++;
//                    continue;
//                }
//            }
//            $rawpost = explode("=", $posttoken);
//            if(count($rawpost) < 2) break;
//            for($i = 0;$i < count($rawpost); $i += 2) {
//                $this->_postdata[$rawpost[$i]] = urldecode($rawpost[$i + 1]);
//            }
//        }
        parent::preDispatch();
    }
    
    public function getPostData(){ 
        $posttokens = explode("&", $this->_body);
        $count = 1;//The calification
//        Zend_Registry::get("logger")->info("The size of array receive is :".count($posttokens));
        foreach ($posttokens as $posttoken) {
            if($count == 1){
                if(strpos(urldecode($posttoken),"empty=empty") !== False){
                    $count++;
                    continue;
                }
            }
            $rawpost = explode("=", $posttoken);
            if(count($rawpost) < 2) break;
            for($i = 0;$i < count($rawpost); $i += 2) {
                $this->_postdata[$rawpost[$i]] = urldecode($rawpost[$i + 1]);
            }
        }
        $_POST = $this->_postdata;
    }
    
    public function getHeaders() {
        //return $this->_request->getHeader('Host');
        $this->_appName = $this->_request->getHeader('X-CapDyn-AppName');
        $this->_appKey = $this->_request->getHeader('X-CapDyn-MACHASH');
        $this->_body = @file_get_contents('php://input');
        
        $this->_logger->info("contain........".$this->_body);
         $this->_logger->info("appName:".$this->_appName);
         $this->_logger->info("appKey:".$this->_appKey);
    }
    
     public function callAccepted() {
        $secrets = new Zend_Config_Ini(APPLICATION_PATH.'/configs/application.ini',APPLICATION_ENV);
        $options = $secrets->services->toArray();
        $key = '';
        if($this->_appName !== NULL && strlen(trim($this->_appName)) > 0 && array_key_exists($this->_appName,$options)) {
            $key = $options[$this->_appName]['secret'];
        }
        $signature = $this->getMD5FromPost($key);
        
        $this->_logger->info("generatedKey:".$signature);
        
        $this->_logger->info("hash is from device:".$this->_appKey ." AND from this is ".$signature);
        if($signature === $this->_appKey) {
            $this->_logger->info("Key Verify!!");
            return true;
        }
        $this->_logger->info("Key Verification fail!!");
        return false;
    }
    
    public function getMD5FromPost($secret){
        $str = '';
        foreach($_POST as $key=>$val){
            $this->_logger->info("Key:".$key." Value:".$val);
            $str .= $key.$val;
//            Zend_Registry::get("logger")->info("Pair: ". $key. "=".$val);
        }
        return md5($secret.$str.$secret);
    }
    
    public function getPost(){
        return $_POST;
    }
    
    public function getAppname() {
        return $this->_appName;
    }
}
?>
