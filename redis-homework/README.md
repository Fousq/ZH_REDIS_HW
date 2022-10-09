# REDIS TASK ABOUT PERFORMANCE IN CLUSTER

## MOST RECENT RESULT 

All of provided time is in milliseconds:

* String written in: 275
* String read in: 174
* Hash written in: 308
* Hash read in: 122
* Sorted set written in: 268
* Sorted set read in: 143
* List written in: 249
* List read in: 328

## REDIS CLUSTER CONFIG

cluster-enabled yes <br>
cluster-config-file nodes.conf <br>
cluster-node-timeout 5000 <br>
appendonly yes
