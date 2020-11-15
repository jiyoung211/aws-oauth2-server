# 작업 내역
## 201108

**ssh key 충돌**
* Host key fingerprint is ssh-rsa 3072 1d:3b:82:0c:4d:36:e4:0e:ac:3b:d3:dc:c8:ff:23:6d. Authentication failed.


**ssh key를 삭제**
* ssh-keygen -R <서버 IP>

* /home/ec2-user/.ssh/authorized_keys

* ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCAGIP7luQ0IZHnKaCo3m+xWlLSRUXQRBqVeOBfVpVRe4GFVEkNHKE7WSvtcbmgJnV9M+2WhaCP9aIqOyXoSDL+wxfpsSox2DHzR5nFTxK69OrSbcmZlczInFpEHHFMUlpCS/S7zCAeDBsxfunNKwbS9jgg5edUZ7dBVtRiv4FkUDSx1RSYDjGrNlpr6Qbe9QmJ/wHFFZ2hNt4BdYaOxlMoTNMHXN+Vz9ufLCQi36ZvF3Pg5KXJljzWdTGYAVfewFQ0cMs8PV48cJOvYBsnSJmJf+B7xrznyuntw9RmMG52u6K90bcFsUofjKVQXhASb/IP/DmCmraKhgcQM/NdM/Rl apigw


**pttygen** 
* key comment에 key-pair 이름 입력



**루트계정 활성화** 
>출처 : https://goddaehee.tistory.com/193


**ec2 인스턴스 지역 변경**
* AWS EC2 인스턴스 지역 변경하기 (이미지를 활용한 방법)
>출처: https://ndb796.tistory.com/257 [안경잡이개발자]

## 201115
** java 수동 설치 오류
-bash: ./javac: No such file or directory

https://velog.io/@woounnan/LINUX-No-such-file-or-directory 
??

** yum으로 자바 openjdk 설치하기
1.$ yum list java*jdk-devel
2.$ sudo yum install java-1.8.0-openjdk-devel.x86_64
3.$ java -version
4.$ which javac
5.$ readlink -f /usr/bin/javac   
6.$ sudo vi /etc/profile
 
https://altongmon.tistory.com/916

** 프롬프트 전체경로 
vi /etc/bashrc
45     [ "$PS1" = "\\s-\\v\\\$ " ] && PS1='[\u@\h $PWD]\\$'


** pom.xml 프로젝트 절대경로 설정
${auth.project.basedir}

** aws 인바운드 규칙 추가
보안그룹 > 사용자 지정 TCP	TCP	8080	0.0.0.0/0	-

** deploy 에러

SEVERE: Unable to deploy collapsed ear in war StandardEngine[Catalina].StandardHost[localhost].StandardContext[/OauthServer]
org.apache.openejb.OpenEJBException: Unable to load servlet class: demo.wssec.server.GreeterImpl: null
        at org.apache.openejb.config.WsDeployer.processPorts(WsDeployer.java:234)
        at org.apache.openejb.config.WsDeployer.deploy(WsDeployer.java:72)
        at org.apache.openejb.config.ConfigurationFactory$Chain.deploy(ConfigurationFactory.java:420)
        at org.apache.openejb.config.ConfigurationFactory.configureApplication(ConfigurationFactory.java:1037)
        at org.apache.tomee.catalina.TomcatWebAppBuilder.startInternal(TomcatWebAppBuilder.java:1281)
        at org.apache.tomee.catalina.TomcatWebAppBuilder.configureStart(TomcatWebAppBuilder.java:1125)
        at org.apache.tomee.catalina.GlobalListenerSupport.lifecycleEvent(GlobalListenerSupport.java:133)
        at org.apache.catalina.util.LifecycleBase.fireLifecycleEvent(LifecycleBase.java:123)
        at org.apache.catalina.core.StandardContext.startInternal(StandardContext.java:5053)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183)
        at org.apache.catalina.core.ContainerBase.addChildInternal(ContainerBase.java:743)
        at org.apache.catalina.core.ContainerBase.addChild(ContainerBase.java:719)
        at org.apache.catalina.core.StandardHost.addChild(StandardHost.java:705)
        at org.apache.catalina.startup.HostConfig.deployWAR(HostConfig.java:970)
        at org.apache.catalina.startup.HostConfig$DeployWar.run(HostConfig.java:1840)
        at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
        at java.util.concurrent.FutureTask.run(FutureTask.java:266)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)
Caused by: java.lang.NullPointerException
        at org.apache.openejb.config.WsDeployer.readWsdl(WsDeployer.java:450)
        at org.apache.openejb.config.WsDeployer.getWsdl(WsDeployer.java:426)
        at org.apache.openejb.config.WsDeployer.processPorts(WsDeployer.java:209)
        ... 19 more

** tomcat 기동오류
jax api 충돌
catalina_home/conf/catalina.properties
tomcat.util.scan.StandardJarScanFilter.jarsToSkip
jax api 주석처리



Nov 15, 2020 4:54:02 PM org.apache.tomee.catalina.TomEEClassLoaderEnricher validateJarFile
WARNING: jar '/home/ec2-user/apache-tomee-plus-1.7.5/servers/OauthServer/webapps/OauthServer/WEB-INF/lib/javax.annotation-api-1.3.jar' contains offending class: javax.annotation.PostConstructbut: You provide javax.annotation API 1.2 so we'll tolerate new classes but it should surely be upgraded in the container
Nov 15, 2020 4:54:02 PM org.apache.catalina.loader.WebappClassLoaderBase validateJarFile
INFO: validateJarFile(/home/ec2-user/apache-tomee-plus-1.7.5/servers/OauthServer/webapps/OauthServer/WEB-INF/lib/javax.servlet-api-3.1.0.jar) - jar not loaded. See Servlet Spec 3.0, section 10.7.2. Offending class: javax/servlet/Servlet.class
Nov 15, 2020 4:54:02 PM org.apache.tomee.catalina.TomEEClassLoaderEnricher validateJarFile
WARNING: jar '/home/ec2-user/apache-tomee-plus-1.7.5/servers/OauthServer/webapps/OauthServer/WEB-INF/lib/javax.ws.rs-api-2.1.jar' contains offending class: javax.ws.rs.Pathbut: You provide JAXRS 2 API in the webapp, we tolerate it to support some advanced feature but if you expect TomEE to provide it you should remove it

** tomcat  JarScanner 설정

* catalina.properties
tomcat.util.scan.StandardJarScanFilter.jarsToSkip=\
*.jar

* context.xml
<JarScanner>
    <JarScanFilter
        pluggabilityScan="${tomcat.util.scan.StandardJarScanFilter.jarsToScan},
                       my_pluggable_feature.jar"/>
  </JarScanner>

https://tomcat.apache.org/tomcat-9.0-doc/config/jar-scan-filter.html
