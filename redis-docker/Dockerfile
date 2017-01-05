FROM redis

COPY ["redis.conf", "/usr/local/etc/redis/"]
COPY ["docker-entrypoint.sh", "/usr/local/bin"]
RUN chmod +x /usr/local/bin/docker-entrypoint.sh
ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]




