
### This Project has using Distributed Sharded MongoDB as Data Storage and Hadoop Map Reduce as Computing Framework.
####The system following these procedures:
 - Sharded MongoDB Design (Gurantee Availability, Load Balancing)
 - MongoDB Storing and Set Main Server and other Server addresses list as MongoClientsOptions
 - Hadoop Map-Reduce with MongoDB connector (Override Mapper and Reducer classes with BSON/MongoUpdateWritable format)
 
##Sharded MongoDB Configuration (This part cannot shown on codes so I just present it here.)

The following graph is the architecture of how I set three VMs with different port to simulate the real sharding pattern(which need 15 machines actually)




The following procedure is how I configured MongoDB on remote three VMs.

###1. Set up data path, config file and log file paths in each node with mongos 、config 、 shard1 、shard2、shard3 (directory name)
```
mkdir -p /data/mongos/log
sudo chmod -R 777 /data/mongos/log
mkdir -p /data/config/data
sudo chmod -R 777 /data/config/data
mkdir -p /data/config/log
sudo chmod -R 777 /data/config/log
mkdir -p /data/mongos/log
sudo chmod -R 777 /data/mongos/log
mkdir -p /data/shard1/data
sudo chmod -R 777 /data/shard1/data
mkdir -p /data/shard1/log
sudo chmod -R 777 /data/shard1/log
mkdir -p /data/shard2/data
sudo chmod -R 777 /data/shard2/data
mkdir -p /data/shard2/log
sudo chmod -R 777 /data/shard2/log
mkdir -p /data/shard3/data
sudo chmod -R 777 /data/shard3/data
mkdir -p /data/shard3/log
sudo chmod -R 777 /data/shard3/log
```

###2. Make a plan of port number and modify some config parameter in mongod.conf

```
mongod --configsvr --dbpath /data/config/data --port 27019 --logpath /data/config/log/config.log --fork


mongos  --configdb 45.55.188.234:27019,45.55.186.238:27019,104.131.106.22:27019  --port 27017   --logpath  /data/mongos/log/mongos.log --fork

##or 
## These codes can migration configuration 

rsync -az /data/configdb mongo-config1.example.net:/data/configdb
rsync -az /data/configdb mongo-config2.example.net:/data/configdb

nano etc/mongod.conf

###This is important!!
set bing_ip = 0.0.0.0; for remote login
set default mongod: 27019 
##!Otherwise your mongos port(27017) will be blocked
```

###3. Config sharding setting on each VM

```
### set up shards ports and dbpath and log path
mongod --shardsvr --replSet shard1 --port 22001 --dbpath /data/shard1/data  --logpath /data/shard1/log/shard1.log --fork --journal  --oplogSize 10
mongod --shardsvr --replSet shard2 --port 22002 --dbpath /data/shard2/data  --logpath /data/shard2/log/shard2.log --fork --journal  --oplogSize 10
mongod --shardsvr --replSet shard3 --port 22003 --dbpath /data/shard3/data  --logpath /data/shard3/log/shard3.log --fork --journal  --oplogSize 10


#Shard_1 in Node(45.55.188.234)
mongo  127.0.0.1:22001
use admin
config = { _id:"shard1", members:[
                     {_id:0,host:"45.55.188.234:22001"},
                     {_id:1,host:"45.55.186.238:22001"},
                {_id:2,host:"104.131.106.22:22001",arbiterOnly:true}
                ]
         }
rs.initiate(config);

#Shard_2 in Node(45.55.186.238)
mongo  127.0.0.1:22002
use admin
config = { _id:"shard2", members:[
                     {_id:0,host:"45.55.186.238:22002"},
                     {_id:1,host:"104.131.106.22:22002"},
                {_id:2,host:"45.55.188.234:22002",arbiterOnly:true}
                ]
         }
rs.initiate(config);

#Shard_3 in Node(104.131.106.22)
mongo  127.0.0.1:22003
use admin
config = { _id:"shard3", members:[
                     {_id:0,host:"104.131.106.22:22003"},
                     {_id:1,host:"45.55.188.234:22003"},
           {_id:2,host:"45.55.186.238:22003",arbiterOnly:true}
                ]
         }
rs.initiate(config);
## if you need to reconfig, please use Cmd( rs.reconfig(your_para) )
```

###4. Add Shard Config just in one of VMs
It seems no master mode concept in MongoDB. So just choose one of it. Config Sharding info in mongos; I also made the sharding part on different machines (e.g. Primary Shard1 on Server One, Primary Shard2 on Server Two).

```
## no specific addr/port 
mongo
use admin

db.runCommand({addshard : "shard1/45.55.188.234:22001,45.55.186.238:22001,104.131.106.22:22001"});

db.runCommand({addshard: "shard2/45.55.186.238:22002,104.131.106.22:22002,45.55.188.234:22002"});

db.runCommand({addshard : "shard3/104.131.106.22:22003,45.55.188.234:22003,45.55.186.238:22003"});

## if you need to reset your previous setting
## 
db.runCommand( { removeShard: "shard1" } )

#Test:
db.runCommand( { enablesharding :"stock"});
db.runCommand( { shardcollection : "stock.quotes",key : {"_id": 1} })

for (var i = 1; i <= 100000; i++) db.table1.save({id:i,"test1":"testval1"});


use admin
db.addUser('test','test')
db.auth('test','test')
```

###5. Some Commmands for check Database Status;
```
db.stats();

show databases

db.dropDatabase()

db.printShardingStatus()
```
###6. Misc.
```
## Sometimes export jar failed
 zip -d stockCrawler.jar META-INF/LICENSE
 jar tvf stockCrawler.jar | grep -i license
 
 ## HDFS manipulation
 
 hadoop fs -ls
 hadoop fs -mkdir /user/${adminName}   
 hadoop fs -touch test
 hdfs dfs -copyFromLocal ${fileName}
 hdfs dfs -cat ${fileName}
 hadoop fs -rmr output
 hadoop jar stockCrawler.jar
 
 ## Some query example:
 db.quotes.find({'historical_quotes.date':'2015-04-10'})
 
```
