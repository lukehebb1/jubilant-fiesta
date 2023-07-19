# Weather Sensor API

## Overview:
This is a weather sensor API Java Spring Boot project that uses a MongoDB.

## Setup:
1. Clone this repo.
2. Import the `jubilant-fiesta` project into Intellij.
3. Run `docker-compose.yaml` located inside the root folder. The server port is set to 8081. Edit it if you would like to use another port.
4. Start up the newly created Docker containers.
5. Go to http://localhost:8081/ and create a database called 'sensordata'. 
6. Click into the newly created database and create a collection named 'sensorData'.
7. Run the `ApiApplication` class inside Intellij. The application will run on http://localhost:8080/.

## How to use:
The application can receive new metric values as the weather changes around the sensor via an API call. This can be done in two ways:

1. Without specifying a date and time (it will use the current date and time):
   ```shell
   curl --location 'http://localhost:8080/api/sensor/data' \
   --header 'Content-Type: application/json' \
   --data '{
       "sensorId": 1,
       "metricType": "temperature",
       "metricValue": 21
   }'
   ```

2. Specifying a date and time:
   ```shell
   curl --location 'http://localhost:8080/api/sensor/data' \
   --header 'Content-Type: application/json' \
   --data '{
       "sensorId": 1,
       "metricType": "temperature",
       "metricValue": 21,
       "dateTime": "2023-07-18T21:50:50"
   }'
   ```

The application allows querying of sensor data. A query defines:
- One or more sensors to include in results.
- The metrics (e.g. temperature and humidity) that the application should return.
- The statistic for the metric: min, max, sum, or average.
- A date range. If not specified, the latest data should be queried.

Example queries:
- Give me the average temperature and humidity for sensors 1 and 2 between the dates of 2023-07-15 at 12:00 and 2023-07-19 at 18:00.
   ```shell
   curl --location 'http://localhost:8080/api/sensor/data?sensorId=1%2C2&metric=temperature&statistic=avg&from=2023-07-15T12%3A00%3A00&to=2023-07-19T18%3A00%3A00'
   ```
- Give me the max temperature from the last day for sensor 1.
```shell
   curl --location 'http://localhost:8080/api/sensor/data?sensorId=1&metric=temperature&statistic=max&from=&to='
   ```
