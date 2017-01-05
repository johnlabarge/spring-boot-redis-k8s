# Redis + Spring Boot POC

## Components:
### 1. Cache
####  a. Redis (latest)
####  b. Redis Sentinel
### 2. API
#### a. spring boot
#### b. redis connector : Lettuce
### 3. Frontend
#### a. angular
#### b. nginx
### 4. Load Balancer
#### a. GKE ingress (https)
### 5. Other
#### a. combined fe+api vs. separate
#### b. self signed cert


## The app

The api provides a random subset of 4 out of 200 "Event" objects.
The service method called is annotated with @Cacheable therefore the same 4 events should be returned
upon subsequent calls to the api until the cache is evicted.

The angular application - "frontend" provides a straightforward view of this list of event objects.  Alternatively Postman or the like can be
used to call hte api directly.

## GKE resources:
### 1. App & Frontend Service - boot-app and fe-service in app.yml, fe.yml and combined in combined.yml
###  a. Dockerfile src/main/docker/Dockerfile and web-frontend/Dockerfile
#### b. deploy docker files with gradle tasks:
##### i. deployFeContainer
##### ii. deployBootContainer
### 2. Cache redis-master.yml, redis-sentinel.yml and redis-slave.yml stateful sets of redis containers
#### a. Dockerfile redis-docker/Dockerfile
#### b. redis.conf - base configuration
#### c. docker-entrypoint - adds configuration and entrypoint based on SENTINEL/MASTER/SLAVE status

## Get it working in GKE

#### 1. Update gradle source with proper project name at line:
```groovy
group = "gcr.io/cloudjlb-eventer"
```
#### 2. Deploy docker containers to your projects repository
```
$ gradle deployFEContainer
$ gradle deployBootContainer
$ gradle deployRedisContainer
```
#### 3. Deploy redis cache pods
```
$ kubectl create -f kubernetes/redis/redis-master.yml
$ kubectl create -f kubernetes/redis/redis-slave.yml
$ kubectl create -f kubernetes/redis/redis-sentinel.yml
```

#### 4. (Optional) Deploy combined app & fe
```
$ kubectl create -f kubernetes/combined/combined.yml
```

#### 5. (Or) Deploy app & fe separately
```
$ kubectl create -f kubernetes/frontend/fe.yml
$ kubectl create -f kubernetes/app/app.yml
```
#### 6. Deploy load balancer
```
$ kubectl create -f kubernetes/self-signed-tls-secret.yml
$ kubectl create -f kubernetes/app-ingress.yml
```
### 7. Visit app

```
$ kubectl get ing
NAME           HOSTS     ADDRESS          PORTS     AGE
boot-ingress   *         35.186.209.205   80        9m
```
Type in browser : https://<ADDRESS above>
Note it is a self signed certificate so you will need to dismiss the warning and allow the browser to proceed to the site.

This will present the angular app.  Alternatively you can send postman to
https://<ADDRESS above>/api/events

### 8. Create mahem with redis and ensure still cached
Example:
```
kubectl exec -it redis-master -- kill 1
```
Or just use chaoskube:
```
kubectl create -f kubernetes/chaos/chaos.yml
```
