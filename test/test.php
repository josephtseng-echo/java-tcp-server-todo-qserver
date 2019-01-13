<?php
$cmd = 0x0001; //无符号短整型 2 主机字节序
$version = 1;  //无符号字符 1
$datatype = 1; //无符号字符 1
$reserved = 0; //无符号长整型 4 主机字节序
$len = 0;      //无符号长整型 4 主机字节序
//

$json = json_encode(array(
                        "name" => "josephzeng",
                        "_todo" => "select", //select,insert,delete,update
                        "_source" => "hbase",  //hbase,hive,hadoop
                        "",
                    ), true);
$str = $json;
$strLen = strlen($str);
$strBuf = pack("L", $strLen + 1);
if ($strLen > 0)
{
    $strBuf .= $str;
}
$strBuf .= pack("C", 0);

$len = $strLen + 1 + 4;
$testBuf = "TT" . pack('S', $cmd) . pack('C', $version) . pack('C', $datatype) . pack('L', $reserved) . pack('L', $len);
$testBuf = $testBuf.$strBuf;
$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
$conn = socket_connect($socket, '127.0.0.1', 8090);
if (!$conn)
{
    socket_close($socket);
    exit();
}
$ret = socket_write($socket, $testBuf, strlen($testBuf));
var_dump($ret);

// while($conn){
//     $recvData = socket_read($socket,1024);
//     var_dump($recvData);
// }
socket_shutdown($socket);
socket_close($socket);
