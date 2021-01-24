# IP-service API Consumer

Create a REST service that takes IP addresses, checks which countries they are from and (if there are any) returns a list of country names from the northern hemisphere.

## IP-API  https://ip-api.com/docs ## 

## To run the service

```
mvn spring-boot:run
```


#   service port

●	The service is  running on port 8888



## Examples:

Invalid Request: 

```
http://localhost:8888/api/v1/northencountries?ip=119.18.X.3
```

Response: 
```
{
    "message": "HAS_INVALID_IP_ADDRESS",
    "httpStatus": "BAD_REQUEST",
    "timestamp": "2021-01-24T21:14:28.1093793Z"
}
```

 Valid Request: 

```
http://localhost:8888/api/v1/northencountries?ip=34.5.6.7&ip=8.8.8.8&ip=27.125.224.3
```

Response: 
```
[
    "Malaysia",
    "United States"
]
```