# Redis + Spring Boot POC

## Components:
##### 1. Cache
######  a. Redis (latest)
######  b. Redis Sentinel
##### 2. API
###### a. spring boot
###### b. redis connector : Lettuce
##### 3. Frontend
###### a. angular
###### b. nginx
##### 4. Load Balancer
###### a. GKE ingress (https)
##### 5. Other
###### b. self signed cert

## Prerequisites
1.  Docker up and running
2.  angular-cli installed (npm install -g angular cli (also requires npm and nodejs))
3.  gcloud config setup with proper region and logged into your GCP account
4.  A container engine cluster : https://cloud.google.com/sdk/gcloud/reference/container/clusters/create
```
$ gcloud container clusters create application-cluster --num-nodes=3
```
5.  Set the gcloud container cluster set : gcloud config set container/cluster application-cluster
6.  Make sure you have the right kubectl client.
```
$ curl -O https://storage.googleapis.com/kubernetes-release/release/v1.5.1/bin/darwin/amd64/kubectl
$ chmod +x kubectl
$ mv kubectl /usr/local/bin/kubectl
```
## The app

The api provides a random subset of 4 out of 200 "Event" objects.
The service method called is annotated with @Cacheable therefore the same 4 events should be returned
upon subsequent calls to the api until the cache is evicted.

The angular application - "frontend" provides a straightforward view of this list of event objects.  Alternatively Postman or the like can be
used to call hte api directly.

## GKE resources:
#### 1. App & Frontend Service - boot-app and fe-service in app.yml, fe.yml
##### a. Dockerfile src/main/docker/Dockerfile and web-frontend/Dockerfile
##### b. deploy docker files with gradle tasks:
###### i. deployFeImage
###### ii. deployBootImage
#### 2. Cache redis-master.yml, redis-sentinel.yml and redis-slave.yml stateful sets of redis containers
##### a. Dockerfile redis-docker/Dockerfile
##### b. redis.conf - base configuration
##### c. docker-entrypoint - adds configuration and entrypoint based on SENTINEL/MASTER/SLAVE status

## Get it working in GKE

#### 1. Update gradle source in build.gradle with proper project name at line:
```groovy
project.ext.gcp = "gcr.io/cloudjlb-eventer"
```

#### 2. Build and push docker images to your Google Container Registry
```
$ gradle deployFEImage
$ gradle deployBootImage
$ gradle deployRedisImage
```
Note: you will likely see some shell script errors in the FE Image deployment.  There are a couple issues with the NodeJs installation on that image.
However nodejs is only used to compile the angular app locally.  The angular app is static and served by nginx.

#### 3. Deploy to GKE (compressed)

```
$ gradle deployToGKE
```

##### Issues the following
```
$ kubectl create -f kubernetes-build/redis/
$ kubectl create -f kubernetes-build/frontend/
$ kubectl create -f kubernetes-build/app/
$ kubectl create -f kubernetes/load-balancer
```
### 4. Visit app

```
$ kubectl get ing
NAME           HOSTS     ADDRESS          PORTS     AGE
boot-ingress   *         35.186.209.205   80        9m
```
Note: may take a while for the ADDRESS to issue - repeat command until then

Type in browser : https://<ADDRESS above>
Note it is a self signed certificate so you will need to dismiss the warning and allow the browser to proceed to the site.

This will present the angular app.  Alternatively you can send postman to
https://<ADDRESS above>/api/events

### 5. Create mayhem with redis and ensure still cached
Example:
```
$ kubectl exec -it redis-master -- kill 1
```
Or just use chaoskube:

```
$ kubectl create -f kubernetes/chaos/chaos.yml
```

### 6.  Cleanup
To clean up first delete the running pods.

```
$ gradle deleteFromGKE
```
Next delete your kubernetes cluster.
```
$ gcloud container clusters list
$ gcloud conatiner clusters delete application-cluster
```
Deletes the resources created above from GKE