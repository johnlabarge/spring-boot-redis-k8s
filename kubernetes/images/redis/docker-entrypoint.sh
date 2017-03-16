#!/bin/bash

#EXIT ON NONZERO response
set -e
#TRACE
set -x


set -- $(which redis-server) /usr/local/etc/redis/redis.conf


case $TYPE in
SLAVE)
  master=$MASTER
  host_number=$(hostname | awk -F "." '{print $1}' | awk -F "-" '{print $3}')
  if [ "$host_number" -ne "0" ];
  then
    master_host_number=$((($host_number-1)))
    master=$(hostname | sed "s/$host_number/$master_host_number/")
  fi
  set -- $@ --slaveof $master 6379
;;
SENTINEL)
cat <<EOF >>/usr/local/etc/redis/redis.conf

sentinel monitor master $MASTER 6379 2
sentinel down-after-milliseconds master 300
sentinel failover-timeout master 300
sentinel parallel-syncs master 1
EOF
set -- $@ --port 26379 --sentinel
;;
MASTER)
echo "test"
cat <<EOF >>/usr/local/etc/redis/redis.conf
save 60 1
dbfilename redis.rdp
dir /var/redis-data
EOF
;;
esac

exec "$@"