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

    public function preDispatch() {
        $this->getResponse()->setHeader('Content-type', 'application/json;charset=UTF-8');
        $this->getHeaders();
        if(!$this->callAccepted()) {
            $returnpacket = array(
                'status' => 0,
                'errmsg' => 'Either your private key is invalid or the data has been tampered with'
            );
            $this->_helper->json->sendJson($returnpacket);
        }
//        
//        Zend_Registry::get("logger")->info("The content is :".$this->_body);
        $posttokens = explode("&", $this->_body);
        $count = 1;//The calification
//        Zend_Registry::get("logger")->info("The size of array receive is :".count($posttokens));
        foreach ($posttokens as $posttoken) {
            if($count == 1){
                if(strpos(urldecode($posttoken),"$$$#$$$") !== False){
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
        parent::preDispatch();
    }
     public function getHeaders() {
        //return $this->_request->getHeader('Host');
        $this->_appName = $this->_request->getHeader('X-CapDyn-AppName');
        $this->_appKey = $this->_request->getHeader('X-CapDyn-MACHASH');
        $this->_body = @file_get_contents('php://input');
        
        $logger = Zend_Registry::get('logger');
        $logger->err("appName:".$this->appName);
        $logger->err("appKey:".$this->appKey);
        print_r($this->body);
    }
    
     public function callAccepted() {
        $secrets = new Zend_Config_Ini(APPLICATION_PATH.'/configs/application.ini',APPLICATION_ENV);
        $options = $secrets->services->toArray();
        $key = '';
        if($this->_appName !== NULL && strlen(trim($this->_appName)) > 0 && array_key_exists($this->_appName,$options)) {
            $key = $options[$this->_appName]['secret'];
        }
        $signature = $this->getMD5FromPost($key);
        $logger = Zend_Registry::get('logger');
        
        $logger->err("generatedKey:".$signature);
        
        $logger->info("hash is from device:".$this->_appKey ." AND from this is ".$signature);
        if($signature === $this->_appKey) {
            $logger->info("Key Verify!!");
            return true;
        }
        $logger->info("Key Verification fail!!");
        return false;
    }
    
    public function getMD5FromPost($secret){
        $str = '';
        foreach($_POST as $key=>$val){
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
