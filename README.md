# Weather API Coding Challenge

**This is my attempt to deliver the following challenge...**

Imagine that we are building a service that provides weather data from various sensors that report metrics
such as temperature, humidity, wind speed, etc. Your task in this coding challenge is building an application
that solves part of this problem: We want you to build a REST API that allows to register sensor readings and
then query the sensor data.

## Application Requirements

- Ability to register sensor data via an API call. You may assume that each sensor has a unique ID and that
  each sensor produces the same weather metrics.

- Ability to query sensor data:

    * Average metric value (e.g. temperature) for all sensors in a specific date range.

    * Average metric value for a specific sensor in a specific date range.

## Constraints

- The programming language has to be Java. You may use any Java libraries that you want.

- You only need to write the REST API, there is no need for a UI.

- Application data must be persisted in some kind of database/storage.

- You may share the code via public repository such as GitHub. You can also share your code via email.

- Consider this a proof of concept. The implementation does not have to be perfect.

## Notes

- You may limit metrics to just temperatures to keep it simple. Further metrics are a bonus and not a hard requirement.

- Focus on the REST API and not the database.

- You should add comments to your code where you find them useful but we don't expect full documentation.

- Implement unit/integration testing as you find necessary while building the application.
  We don't expect complete test coverage.

- If you have questions or clarifications, please contact your recruiter.

- We will discuss the code you submitted during the technical interview.

- Have fun!

## Local Environment Setup

### Clone the repo

1. This code can be found at: https://github.com/egertond/weather-api-challenge.

2. Open a new terminal window and change to a location where you wish to clone the repo.
   Use the following command to clone the latest code:

```bash
$ git clone https://github.com/egertond/weather-api-challenge
```

### Launch the WeatherAPI Spring-Boot services

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

### Launch the WeatherAPI React-UI

**Prerequisites:** Node 18 or later

1. Open a new terminal window and navigate to the React-UI folder. This would be something like:

```bash
$ cd weather-api-challenge/react-ui
```

2. Next, run the following two NPM commands to build and launch the React-UI:

```bash
$ npm install
$ npm run dev
```

3. The UI is now running and can be access through your browser at: http://localhost:3000
