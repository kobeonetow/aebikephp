<?php
/*
 * Define the Http Client for webservice communication.
 * Http Method will always be "POST"
 * 
 * Use call method to call to webserives
 * 
 * For every webservices, the host name and secret key name should be
 * given in initialisation and must be same as in webservices.ini config file
 * under /application/config folder.
 */
class Application_Model_HttpClient
{
    const HTTP_METHOD = "POST";
    
    protected $client;
    protected $secretKey;
    protected $appName;
    protected $URI;
    protected $options;


    public function __construct($webHostName, $secretName) {
        $this->secrets = new Zend_Config_Ini('../application/configs/webservices.ini',APPLICATION_ENV);
        $this->secretName = $secretName;
        $this->appName = $this->secrets->appName;
        $this->options = $this->secrets->services->toArray();
        $this->secretKey = $this->options[$this->appName][$secretName];
        $this->URI = $this->options[$this->appName][$webHostName];
        $this->client = new Zend_Http_Client();
        $this->init();
    }
    
    public function init(){

    }
    
    public function call($method, $rawpost){
        $this->client->setUri($method);
        //$this->client->setRawData($rawpost, 'text/xml');
        $signature = $this->signature($rawpost);
        $this->client->setHeaders(array(
            'Content-Type' => 'application/x-www-form-urlencoded; charset=UTF-8',
            'X-CapDyn-AppName' => $this->appName,
            'X-CapDyn-MACHASH' => $signature
        ));
        $result = $this->client->request(self::HTTP_METHOD);
        return Zend_Json_Decoder::decode($result->getBody());  
    }

    public function getRawPostStr($arr){
        $rawpost = "";
        $count = count($arr);
        if($count > 0){
            foreach($arr as $key=>$val){
                $rawpost .= $key.'='.urlencode($val);
                $count = $count - 1;
                if($count > 0){
                    $rawpost .= '&';
                }
            }
        }
        return $rawpost;
    }
    
    public function signature($arr){
        $str = '';
        foreach($arr as $key=>$val){
            $str .= $key.$val;
        }
        return md5($this->secretKey.$str.$this->secretKey);
    }
}

