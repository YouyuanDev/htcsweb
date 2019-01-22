#!/bin/sh



#本脚本必须放置于代码目录webapp/publish/下才能执行,
#取得当前执行的脚本文件的父目录

basepath=$(cd `dirname $0`; pwd)
echo $basepa
# 将目标war文件拷贝到服务器
#进入到本地 $basepath/目录：


basepath=$(dirname $basepath)
basepath=$(dirname $basepath)
basepath=$(dirname $basepath)
basepath=$(dirname $basepath)
echo $basepath

#公司服务器
#scp -r $basepath/target/htcsweb.war root@192.168.0.200:/usr/share/tomcat/apache-tomcat-8.5.28/webapps/

#阿里云服务器
scp -r $basepath/target/htcsweb.war root@116.62.17.42:/usr/share/tomcat/webapps/


#scp /Users/kurt/Downloads/coatinginspection_v92_legu_signed_zipalign.apk root@116.62.17.42:/usr/share/tomcat/webapps/upload/


#ios  https://itunes.apple.com/app/id1419485514

#Android http://www.topinspector.cn:8080/upload/hlcoatingapp_legu_signed_zipalign.apk