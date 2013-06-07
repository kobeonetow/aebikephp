<?php

class Application_Model_GetSetModel
{
    public function __construct(array $options = NULL) {
        if(is_array($options)) {
            $this->setOptions($options);
        }
    }
    
    public function setOptions(array $options) {
        $methods = get_class_methods($this);
        foreach ($options as $key => $value) {
            $method = 'set'.ucfirst($key);
            if(in_array($method, $methods)) {
                $this->$method($value);
            }
        }
        return $this;
    }
    
    public function __set($name, $value) {
        $method = 'set'.$name;
        if(('mapper' == $name) || !method_exists($this, $method)) {
            throw new Exception('Invalid Staff property called');
        }
        $this->$method($value);
    }
    
    public function __get($name) {
        $method = 'get'.$name;
        if(('mapper' == $name) || !method_exists($this, $method)) {
            throw new Exception('Invalid Staff property called');
        }
        return $this->$method();
    }
    
    /**
     * Convert all fields to Key=>Value array if getter is
     * defined. Only not null values returned
     */
    public function toKeyValueArray(){
        $methods = get_class_methods($this);
        $ret = array();
        foreach ($methods as $method) {
            $issetmethod = substr($method, 0, 3);
            if(strcmp($issetmethod,"get")!=0)
                    continue;
            $name = strtolower(substr($method, 3));
            $value = $this->$method();
            if(!is_null($value))
                $ret[$name] = $value;
        }
        return $ret;
    }
}
