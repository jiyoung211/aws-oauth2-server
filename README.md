

# OAuth2 Server
Authorization code Grant Type
- STEP1. Issue Authorization Code
- STEP2. Issue Access Token
- STEP3. Call Api
   1. REST
   2. SOAP
  
![enter image description here](./readme/image/oauth/img1.PNG)
![enter image description here](./readme/image/oauth/img2.PNG)
![enter image description here](./readme/image/oauth/img3.PNG)
![enter image description here](./readme/image/oauth/img4.PNG)
![enter image description here](./readme/image/oauth/img5.PNG)

# SAAJ 

- ����ڰ� ���� SOAP �޼�¡ ���ø����̼��� �ۼ��� �� �ִ� API
- JAX-RPC�� ������� �ʰ� SOAP �޽�¡ ���� ���α׷��� ���� �ۼ��ϵ��� ������ �� ����� ���ִ� API
- SAAJ�� ����Ͽ� �޼ҵ带 ȣ�������ν� ����ڴ� SOAP ��� XML �޼����� �а� �ۼ��� �� ������,  ���ͳ��� ���� �̷� Ÿ���� �޼������� �ְ���� ���� �ִ�.

1. soapEndpointUrl  : soap service url
2. soapAction : soap service action
3. Namespace  : ����� ���ǰ�
4. NamespaceURI  : service Class targetNamespace�� ���ǵ� �� 
5. elementName : wsdl operationName 
6. childElementName : WebMethod�� �Ķ���� name
7. childElementValue : WebMethod�� �Ķ���� value

![enter image description here](./readme/image/saaj/img1.PNG)
![enter image description here](./readme/image/saaj/img2.PNG)
![enter image description here](./readme/image/saaj/img3.PNG)


# Swagger
- ������(Swagger)�� Open Api Specification(OAS)�� ���� �����ӿ�ũ
- API���� ������ �ִ� ����(spec)�� ��, ������ �� �ִ� ������Ʈ



# JAXB


- JAXB�� �ڹ��� ��ü�� XML�� ����ȭ�Ͽ� Client���� �������ְ� Client�� ���� ��û XML�� �ٽ� �ڹ� ��ü�� ������ȭ���ִ� �ڹ� API
- Java ������̼��� ����Ͽ� XML�� Java ���� ������ ����
- JDK6 ~ 9 ������ JAXB�� ����Ǿ� �־� ���̺귯���� �߰� �� �ʿ䰡 �����ϴ�. 
- WebService�� VO(Value Object) Ŭ�������� JAXB Annotation ����Ͽ� �ۼ�


# WS-Security


- TCP/IP ������ ���� ������ ������ �ƴ� �޽��� ��ü�� ���� ������ ����
- �޽����� �Ϻθ��� ��ȣȭ
- ���� �������� ��� ����
- ���� ���� ���� ��ū ��� : ������ + ���̵�/�н�����
- ����-��-���� �޽��� ��� 
- PKI(Public Key Infrastructure, ���� Ű ���) ��� ��ȣȭ ���
- X509(������) ���
-  �޽��� ���� 


       ���� WS-Security ������ ����
       http://credemol.blogspot.com/2010/07/ws-security.html
       
       ���� Java Keytool�� Ű����� ���� �� ����Ű ����� Ű �� ����
       https://m.blog.naver.com/wndrlf2003/220649843082
       
       ����	Secure Web Services Message using WSE 2.0 ������ ��� �ڷ�
       https://www.kdata.or.kr/info/info_04_view.html?field=&keyword=&type=techreport&page=334&dbnum=126630&mode=detail&type=techreport
       
       ���� Spring CXF Webservice SSL ����
       http://blog.naver.com/PostView.nhn?blogId=catchbug&logNo=20129493671&redirect=Dlog&widgetTypeCall=true&directAccess=fals
