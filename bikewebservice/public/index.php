<?php

// Define path to application directory
defined('APPLICATION_PATH')
    || define('APPLICATION_PATH', realpath(dirname(__FILE__) . '/../application'));

// Define application environment
defined('APPLICATION_ENV')
    || define('APPLICATION_ENV', (getenv('APPLICATION_ENV') ? getenv('APPLICATION_ENV') : 'development'));

//Define the status
defined('STATUS_PLAN_NOT_ASSIGN')
    ||define('STATUS_PLAN_NOT_ASSIGN','0');
defined('STATUS_NEW')
    || define('STATUS_NEW', '1');
defined('STATUS_PLAN_FINISH')
    || define('STATUS_PLAN_FINISH', '2');
defined('STATUS_PLAN_ACCEPT')
    || define('STATUS_PLAN_ACCEPT', '3');
defined('STATUS_PLAN_INTEREST')
    || define('STATUS_PLAN_INTEREST', '4');
defined('STATUS_PLAN_START')
    || define('STATUS_PLAN_START', '5');
defined('STATUS_PLAN_TERMINATED')
    || define('STATUS_PLAN_TERMINATED', '6');
defined('STATUS_ACHIEVEMENT_GRAB')
    || define('STATUS_ACHIEVEMENT_GRAB', '7');
defined('STATUS_NEW_FRIEND')
    || define('STATUS_NEW_FRIEND', '8');
defined('STATUS_INVITE_FRIEND')
    || define('STATUS_INVITE_FRIEND', '9');
defined('STATUS_REQUEST_FRIEND')
    || define('STATUS_REQUEST_FRIEND', 'A');

//Define the plan type Q:QUICK, N:NORMAL, C:CHALLENGE
defined('PLAN_TYPE_QUICK')
    || define('PLAN_TYPE_QUICK','Q');
defined('PLAN_TYPE_NORMAL')
    || define('PLAN_TYPE_NORMAL','N');
defined('PLAN_TYPE_CHALLENGE')
    || define('PLAN_TYPE_CHALLENGE','C');

// Ensure library/ is on include_path
set_include_path(implode(PATH_SEPARATOR, array(
    realpath(APPLICATION_PATH . '/../library'),
    get_include_path(),
)));

/** Zend_Application */
require_once 'Zend/Application.php';

// Create application, bootstrap, and run
$application = new Zend_Application(
    APPLICATION_ENV,
    APPLICATION_PATH . '/configs/application.ini'
);
$application->bootstrap()
            ->run();
