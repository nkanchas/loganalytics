# loganalytics

#Design & Approach:  
LogAnalytcis Can we done in 2 ways .
1) Real time log Analysis  
2) Query data from historical log    


Option 1: Real time log Analysis
1) Best way to do real time log analysis is to use  
a) Log Forwarders (Logstash, Fluentd, StatsD, CollectD)  
b) User ElasticSearch(with Kibana) or MongoDB  
c) For Monitoring & Alerting Use Sensu, Uchiwa and Pager Duty.  

Option 2: Query Data from log file  
I took simple approach using unix "awk", "cut" pipe commands to do the same.  

Below repository is for option 2 to query log file. (Used to do quick insight into data)  


#Steps 
1) copy access.log to /tmp  (Optional. If not copied access.log from repo will be considered)
2) git clone https://github.com/nkanchas/loganalytics.git  
3) cd loganalytics (log into project)  
4) mvn clean package  (compiles, run test cases, creates fat jar)  
5) java -jar target/loganalytics-0.0.1-SNAPSHOT-fat.jar  -conf src/main/resources/config.json  


More Information:  
Apache access log is copied to /tmp and same is defined in property file  
config.json  
   {  
     "logpath" : "/tmp/access.log"  
   }  



Below are curl calls to get data from access log  

# total number of GET requests
curl -X GET http://localhost:8080/log/GET/count
{"count":"  227639"}

#total number of POST requests
curl -X GET http://localhost:8080/log/POST/count
{"count":"   85615"}

# get top user agents
curl -X GET http://localhost:8080/log/useragents/top/5
{"Top-UserAgents":[{"hits":"102156","user-agent":"Mozilla/5.0"},{"hits":"36435","user-agent":"-"},{"hits":"31280","user-agent":"Go-http-client/1.1"},{"hits":"8001","user-agent":"Mozilla/5.0"},{"hits":"7260","user-agent":"Mozilla/5.0"}]}

# get top n user agents
curl -X GET http://localhost:8080/log/useragents/top/2
{"Top-UserAgents":[{"hits":"102156","user-agent":"Mozilla/5.0"},{"hits":"36435","user-agent":"-"}]}

#get top n URLs accessed
curl -X GET http://localhost:8080/log/topurls/10
{"TopUrls":[{"hits":"54177","url":"http://almhuette-raith.at/administrator/"},{"hits":"30441","url":"http://www.almhuette-raith.at/"},{"hits":"20307","url":"http://www.almhuette-raith.at/index.php?option=com_phocagallery&view=category&id=1&Itemid=53"},{"hits":"14385","url":"http://almhuette-raith.at/administrator/index.php"},{"hits":"4467","url":"http://almhuette-raith.at/"},{"hits":"3480","url":"http://www.almhuette-raith.at/index.php?option=com_phocagallery&view=category&id=4:ferienwohnung2&Itemid=53"},{"hits":"2672","url":"http://www.almhuette-raith.at/index.php?option=com_content&view=frontpage"},{"hits":"2556","url":"http://www.almhuette-raith.at/index.php?option=com_content&view=article&id=49&Itemid=55"},{"hits":"1990","url":"http://www.almhuette-raith.at/index.php?option=com_content&view=article&id=50&Itemid=56"},{"hits":"1604","url":"http://www.almhuette-raith.at/index.php?option=com_phocagallery&view=category&id=2:winterfotos&Itemid=53"}]}

#get top response codes
curl -X GET http://localhost:8080/log/top/responsecodes
{"User-Agents":[{"hits":"293847","http-code":"200"},{"hits":"16210","http-code":"404"},{"hits":"1962","http-code":"304"},{"hits":"1312","http-code":"206"},{"hits":"352","http-code":"301"},{"hits":"339","http-code":"500"},{"hits":"10","http-code":"405"},{"hits":"7","http-code":"303"},{"hits":"5","http-code":"403"},{"hits":"3","http-code":"412"},{"hits":"1","http-code":"501"},{"hits":"1","http-code":"400"}]}

# get all unique user-agents
curl -X GET http://localhost:8080/log/user-agents
{"User-Agents":[{"hits":"102156","user-agent":"Mozilla/5.0"},{"hits":"36435","user-agent":"-"},{"hits":"31280","user-agent":"Go-http-client/1.1"},{"hits":"8001","user-agent":"Mozilla/5.0"},{"hits":"7260","user-agent":"Mozilla/5.0"},{"hits":"6015","user-agent":"Mozilla/5.0"},{"hits":"5184","user-agent":"Mozilla/5.0"},{"hits":"3559","user-agent":"Mozilla/5.0"},{"hits":"2612","user-agent":"Mozilla/5.0"},{"hits":"2269","user-agent":"Opera/9.80"},{"hits":"1923","user-agent":"Mozilla/5.0"},{"hits":"1903","user-agent":"Mozilla/5.0"},{"hits":"1860","user-agent":"Mozilla/5.0"},{"hits":"1733","user-agent":"python-requests/1.2.3"},{"hits":"1417","user-agent":"Mozilla/5.0"},{"hits":"1378","user-agent":"Mozilla/5.0"},{"hits":"1337","user-agent":"Mozilla/5.0"},{"hits":"1324","user-agent":"Mozilla/5.0"},{"hits":"1320","user-agent":"Mozilla/5.0"},{"hits":"1291","user-agent":"Mozilla/4.0"},{"hits":"1290","user-agent":"Mozilla/5.0"},{"hits":"1272","user-agent":"Mozilla/5.0"},{"hits":"1236","user-agent":"Opera/9.80"},{"hits":"1201","user-agent":"Mozilla/5.0"},{"hits":"1138","user-agent":"Mozilla/5.0"},{"hits":"1116","user-agent":"Mozilla/5.0"},{"hits":"1088","user-agent":"Mozilla/5.0"},{"hits":"933","user-agent":"Mozilla/5.0"},{"hits":"924","user-agent":"Mozilla/5.0"},{"hits":"919","user-agent":"Mozilla/5.0"},{"hits":"850","user-agent":"Mozilla/5.0"},{"hits":"760","user-agent":"Mozilla/5.0"},{"hits":"731","user-agent":"Mozilla/5.0"},{"hits":"709","user-agent":"Mozilla/5.0"},{"hits":"667","user-agent":"Mozilla/5.0"},{"hits":"665","user-agent":"Mozilla/5.0"},{"hits":"663","user-agent":"Mozilla/5.0"},{"hits":"655","user-agent":"Mozilla/5.0"},{"hits":"650","user-agent":"Mozilla/5.0"},{"hits":"638","user-agent":"Mozilla/5.0"},{"hits":"603","user-agent":"Mozilla/5.0"},{"hits":"598","user-agent":"Mozilla/5.0"},{"hits":"598","user-agent":"Mozilla/5.0"},{"hits":"596","user-agent":"Mozilla/5.0"},{"hits":"595","user-agent":"Mozilla/5.0"},{"hits":"593","user-agent":"Mozilla/5.0"},{"hits":"593","user-agent":"Mozilla/5.0"},{"hits":"588","user-agent":"Mozilla/5.0"},{"hits":"576","user-agent":"Mozilla/5.0"},{"hits":"575","user-agent":"Mozilla/5.0"},{"hits":"572","user-agent":"Mozilla/5.0"},{"hits":"566","user-agent":"Mozilla/5.0"},{"hits":"550","user-agent":"Mozilla/5.0"},{"hits":"546","user-agent":"Mozilla/5.0"},{"hits":"544","user-agent":"Mozilla/5.0”}]}



