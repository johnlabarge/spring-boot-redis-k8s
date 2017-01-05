#!/bin/bash
printf " THIS PART IS WORKING \n"
#EXIT ON NONZERO response
set -e
#TRACE
set -x


set -- $(which redis-server) /usr/local/etc/redis/redis.conf


case $TYPE in
SLAVE)
  set -- $@ --slaveof $MASTER 6379
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
esac

exec "$@"