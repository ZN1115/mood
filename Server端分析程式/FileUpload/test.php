<?php 
 $data=$_POST['data'];//宣告post的變數key是data,value由使用者傳送
 $command='python test.py';
 $command2='python test2.py';
 $command3='python normal_pretreatment2.py';#常模預處理 a
 $command4='python pretreatment1.py';#預處理 資料夾
 $command5='python mood_analysis.py';#音檔分析 b
 $command6='python main.py';#語料庫比對 c
 $command7='python azure_analysis.py';#azure分析 d
 $command8='python final_analysis.py';#整合所有分數 test
 #$command4='python normal_pretreatment.py';
 if($_POST['data']=="title2")
 {
    $output = shell_exec($command);
 }
 else if($_POST['data']=="pro")
 {
   $output = shell_exec($command3);
 }
 else if($_POST['data']=="promood")
 {
   $output = shell_exec($command4);
 }
 else if($_POST['data']=="moodanalysis")
 {
   $output = shell_exec($command5);
   $output2 = shell_exec($command6);
   $output3 = shell_exec($command7);
 }
 else if($_POST['data']=="final")
 {
   $output = shell_exec($command8);
 }
 //echo "data = ".$data;//返回印出data = 使用者傳送key為data的value
?>