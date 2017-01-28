#!/usr/bin/env python

import os

slaves=os.popen("kubectl get pods -o wide  | awk '{print $1\" \"$6}' | grep redis-").read()
slaves=slaves.split("\n")
slaves.pop()
slave_nodes=[]
for s in slaves:
  slave_node={}
  slave_node["name"], slave_node["ip"] = s.split(" ")
  slave_nodes.append(slave_node)

for s in slave_nodes:
  master_host_line=os.popen("kubectl exec -it %s redis-cli info | grep master_host" % s["name"]).read()
  if not master_host_line:
    s['master_host']='None'
  else:
    s['master_host']=master_host_line.split(":")[1].strip()

for s in slave_nodes:
  if (s["name"].startswith("redis-slave")):
    master_host=s["master_host"]
    if master_host[0].isdigit():
      master_host=(node["name"] for node in slave_nodes if node["ip"] == master_host).next()
    print "%s (%s) -> %s \n " % (s["name"], s["ip"], master_host)