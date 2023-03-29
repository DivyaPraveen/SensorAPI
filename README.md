# Sensor API

## Functionalities covered are:
    - Save a sensorData ( temperature and humidity)
        It accepts the field based on com/app/weather/models/SensorRequest.java
    - Get list of sensor data by sensor id.
    - Query sensor metric (min,max and average) of a date range by sensor id
    - Unit test- 92% code coverage
    - Postman collection is at docs/weather-api-postman.json

## Libraries and Technologies used
    - Docker
    - Java 8 & Maven
    - Mapstruct mapping library
    - DynamoDb interacting with LocalStack with Spring Data
    - GitHub

 **This application uses docker container localstack/localstack.**

## Step 1
 To spin up local stack container, run this

```
docker run --rm -it -p 4566:4566 -p 4510-4559:4510-4559 localstack/localstack
```

## Step:2 
Run your connection config for container. Execute the following in command line

```
export AWS_ACCESS_KEY_ID="test"
export AWS_SECRET_ACCESS_KEY="test"
export AWS_DEFAULT_REGION="us-east-1"
```

#Create dynamo db table **SensorDataInfo** with the following script

```
aws  --endpoint-url=http://localhost:4566 \
dynamodb create-table --table-name SensorDataInfo \
--attribute-definitions AttributeName=uuid,AttributeType=S \
AttributeName=sensorId,AttributeType=S \
AttributeName=createTs,AttributeType=S \
--key-schema \
AttributeName=uuid,KeyType=HASH \
AttributeName=sensorId,KeyType=RANGE \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
--global-secondary-indexes 'IndexName=sensoridCreateTs,KeySchema=[{AttributeName=sensorId,KeyType=HASH},{AttributeName=createTs,KeyType=RANGE}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=10,WriteCapacityUnits=5}'
```
You can see the entries in the table with these script

```
aws --endpoint-url=http://localhost:4566 --region=us-east-1 dynamodb scan --table-name SensorDataInfo
```

## Step 3:
```
mvn clean install
mvn spring-boot:run 
```
Date format is default : yyyy-MM-dd
No-null inclusions have been done for the responses when string is null

### Example URls

POST http://localhost:8080/v0/sensor

GET http://localhost:8080/v0/A001/date?dateFrom=2023-03-27&dateTo=2023-03-30

//If you don't pass any values, current date metrics are fetched

GET http://localhost:8080/v0/A001/date?dateFrom=&dateTo= 

GET http://localhost:8080/v0/id?sensorId=134

Incomplete scenarios:
   -  WindSpeed Metric yet to be added
   -  Sum is not calculated



