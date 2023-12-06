# Weather API Coding Challenge

## Sprint REST API Backend

Simple Spring-Boot application written in Java that provides sever API endpoints to 
facilitate the required Sensor data gathering.

#### Launch the WeatherAPI Spring-Boot services

**Prerequisites:** Java 17 or later

1. Open a new terminal window and navigate to the services folder. This would be something like:

```bash
$ cd weather-api-challenge/spring-rest
```

2. Next, run the Gradle command to build and launch the backend services:

```bash
$ ./gradlew bootRun
```

3. Rest services are now accessible at: http://localhost:8080

## API Documentation

#### Sensor Endpoints

<details>
 <summary><code>GET</code> <code><b>/api/sensors</b></code> <code>(Find all Sensors that match the provided query)</code></summary>

##### Parameters

> | name    | type     | data type | description                          |
> |---------|----------|-----------|--------------------------------------|
> | `query` | required | string    | filter used to find matching sensors |

##### Responses

> | http code | content-type       | response |
> |-----------|--------------------|----------|
> | `200`     | `application/json` | JSON     |
> | `500`     |                    | None     |

</details>

<details>
 <summary><code>GET</code> <code><b>/api/sensors/{sensorId}</b></code> <code>(Get the configuration of a single Sensor)</code></summary>

##### Parameters

> | name       | type     | data type | description                         |
> |------------|----------|-----------|-------------------------------------|
> | `sensorId` | required | UUID      | respective ID of the Sensor to find |

##### Responses

> | http code | content-type       | response |
> |-----------|--------------------|----------|
> | `200`     | `application/json` | JSON     |
> | `500`     |                    | None     |

</details>

<details>
 <summary><code>POST</code> <code><b>/api/sensors</b></code> <code>(Create a new Sensor)</code></summary>

##### Parameters

> | name      | type     | data type | description              |
> |-----------|----------|-----------|--------------------------|
> | `request` | required | JSON      | new sensor configuration |

##### Responses

> | http code | content-type       | response |
> |-----------|--------------------|----------|
> | `200`     | `application/json` | JSON     |
> | `500`     |                    | None     |

</details>

<details>
 <summary><code>POST</code> <code><b>/api/sensors/{sensorId}</b></code> <code>(Update an existing Sensor)</code></summary>

##### Parameters

> | name       | type     | data type | description                           |
> |------------|----------|-----------|---------------------------------------|
> | `sensorId` | required | UUID      | respective ID of the Sensor to update |
> | `request`  | required | JSON      | updated sensor configuration          |

##### Responses

> | http code | content-type       | response |
> |-----------|--------------------|----------|
> | `200`     | `application/json` | JSON     |
> | `500`     |                    | None     |

</details>

------------------------------------------------------------------------------------------

#### Sensor History Endpoints

<details>
 <summary><code>GET</code> <code><b>/api/sensors/history</b></code> <code>(History extract [for all Sensors] over the respective time period)</code></summary>

##### Parameters

> | name         | type     | data type  | description                        |
> |--------------|----------|------------|------------------------------------|
> | `startDate`  | optional | yyyy-MM-dd | start of the time period filter    |
> | `endDate`    | optional | yyyy-MM-dd | end of the time period filter      |

##### Responses

> | http code | content-type       | response |
> |-----------|--------------------|----------|
> | `200`     | `application/json` | JSON     |
> | `500`     |                    | None     |

</details>

<details>
 <summary><code>GET</code> <code><b>/api/sensors/{sensorId}/history</b></code> <code>(History extract [for specific Sensors] over the respective time period)</code></summary>

##### Parameters

> | name        | type       | data type  | description                         |
> |-------------|------------|------------|-------------------------------------|
> | `sensorId`  | required   | UUID       | respective ID of the Sensor to find |
> | `startDate` | optional   | yyyy-MM-dd | start of the time period filter     |
> | `endDate`   | optional   | yyyy-MM-dd | end of the time period filter       |

##### Responses

> | http code | content-type       | response |
> |-----------|--------------------|----------|
> | `200`     | `application/json` | JSON     |
> | `500`     |                    | None     |

</details>

<details>
 <summary><code>POST</code> <code><b>/api/sensors/{sensorId}/history</b></code> <code>(Add new Sensor History record)</code></summary>

##### Parameters

> | name       | type     | data type | description                           |
> |------------|----------|-----------|---------------------------------------|
> | `sensorId` | required | UUID      | respective ID of the Sensor to update |
> | `request`  | required | JSON      | new sensor history details            |

##### Responses

> | http code | content-type       | response |
> |-----------|--------------------|----------|
> | `200`     | `application/json` | JSON     |
> | `500`     |                    | None     |

</details>

------------------------------------------------------------------------------------------

#### Sensor Average Endpoints

<details>
 <summary><code>GET</code> <code><b>/api/sensors/averages</b></code> <code>(Averaged data [for all Sensors] over the respective time period)</code></summary>

##### Parameters

> | name        | type     | data type  | description                        |
> |-------------|----------|------------|------------------------------------|
> | `startDate` | optional | yyyy-MM-dd | start of the time period filter    |
> | `endDate`   | optional | yyyy-MM-dd | end of the time period filter      |

##### Responses

> | http code | content-type       | response |
> |-----------|--------------------|----------|
> | `200`     | `application/json` | JSON     |
> | `500`     |                    | None     |

</details>

<details>
 <summary><code>GET</code> <code><b>/api/sensors/{sensorId}/averages</b></code> <code>(Averaged data [for specific Sensor] over the respective time period)</code></summary>

##### Parameters

> | name        | type      | data type  | description                         |
> |-------------|-----------|------------|-------------------------------------|
> | `sensorId`  | required  | UUID       | respective ID of the Sensor to find |
> | `startDate` | optional  | yyyy-MM-dd | start of the time period filter     |
> | `endDate`   | optional  | yyyy-MM-dd | end of the time period filter       |

##### Responses

> | http code | content-type       | response |
> |-----------|--------------------|----------|
> | `200`     | `application/json` | JSON     |
> | `500`     |                    | None     |

</details>

------------------------------------------------------------------------------------------

#### Lookup Endpoints

<details>
 <summary><code>GET</code> <code><b>/countries</b></code> <code>(Utility endpoint to return a simple map of country code and name)</code></summary>

##### Parameters

> None

##### Responses

> | http code | content-type       | response |
> |-----------|--------------------|----------|
> | `200`     | `application/json` | JSON     |
> | `500`     |                    | None     |

</details>

<details>
 <summary><code>GET</code> <code><b>/timezones</b></code> <code>(Utility endpoint to return a simple map of time-zone code and descriptor)</code></summary>

##### Parameters

> None

##### Responses

> | http code | content-type       | response |
> |-----------|--------------------|----------|
> | `200`     | `application/json` | JSON     |
> | `500`     |                    | None     |

</details>

<details>
 <summary><code>GET</code> <code><b>/locations</b></code> <code>(Utility endpoint to return a list of locations that matched the provided query)</code></summary>

##### Parameters

> | name      | type     | data type | description            |
> |-----------|----------|-----------|------------------------|
> | `request` | required | JSON      | location query request |

##### Responses

> | http code | content-type       | response |
> |-----------|--------------------|----------|
> | `200`     | `application/json` | JSON     |
> | `500`     |                    | None     |

</details>
