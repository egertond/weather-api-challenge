import * as React from 'react';
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';

export default function HomePage() {
  return (
    <Container>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'left',
          alignItems: 'left',
        }}
      >
        <Typography variant="h4" gutterBottom>
          Weather API Coding Challenge
        </Typography>
        <Typography component="div" variant="body1" gutterBottom>
          <strong>This is my attempt to deliver the following challenge...</strong>
          <br/><br/>
          Imagine that we are building a service that provides weather data from various sensors that
          report metrics such as temperature, humidity, wind speed, etc. Your task in this coding
          challenge is building an application that solves part of this problem: We want you to build a
          REST API that allows to register sensor readings and then query the sensor data.
        </Typography>
        <Typography variant="h5" gutterBottom>
          Application Requirements
        </Typography>
        <Typography component="div" variant="body1" gutterBottom>
          <ul>
            <li>Ability to register sensor data via an API call. You may assume that each sensor has a unique ID and that each sensor produces the same weather metrics.</li>
            <li>Ability to query sensor data:
              <ul>
                <li>Average metric value (e.g. temperature) for all sensors in a specific date range.</li>
                <li>Average metric value for a specific sensor in a specific date range.</li>
              </ul>
            </li>
          </ul>
        </Typography>
        <Typography variant="h5" gutterBottom>
          Constraints
        </Typography>
        <Typography component="div" variant="body1" gutterBottom>
          <ul>
            <li>The programming language has to be Java. You may use any Java libraries that you want.</li>
            <li>You only need to write the REST API, there is no need for a UI.</li>
            <li>Application data must be persisted in some kind of database/storage.</li>
            <li>You may share the code via public repository such as GitHub. You can also share your code via email.</li>
            <li>Consider this a proof of concept. The implementation does not have to be perfect.</li>
          </ul>
        </Typography>
        <Typography variant="h5" gutterBottom>
          Notes
        </Typography>
        <Typography component="div" variant="body1" gutterBottom>
          <ul>
            <li>You may limit metrics to just temperatures to keep it simple. Further metrics are a bonus and not a hard requirement.</li>
            <li>Focus on the REST API and not the database.</li>
            <li>You should add comments to your code where you find them useful but we don't expect full documentation.</li>
            <li>Implement unit/integration testing as you find necessary while building the application. We don't expect complete test coverage.</li>
            <li>If you have questions or clarifications, please contact your recruiter.</li>
            <li>We will discuss the code you submitted during the technical interview.</li>
            <li>Have fun!</li>
          </ul>
        </Typography>
        <Typography variant="h5" gutterBottom>
          Local Environment Setup
        </Typography>
        <Typography variant="h6" gutterBottom>
          Clone the repo
        </Typography>
        <Typography component="div" variant="body1" gutterBottom>
          <ol>
            <li>This code can be found at: <a href="https://github.com/egertond/weather-api-challenge">https://github.com/egertond/weather-api-challenge</a>.</li>
            <li>
              Open a new terminal window and change to a location where you wish to clone the repo.
              Use the following command to clone the latest code:
              <div style={{fontFamily: 'monospace', fontSize: 'smaller', margin: '15px 0'}}>
                $ git clone https://github.com/egertond/weather-api-challenge
              </div>
            </li>
          </ol>
        </Typography>
        <Typography variant="h6" gutterBottom>
          Launch the WeatherAPI Spring-Boot services
        </Typography>
        <Typography component="div" variant="body1" gutterBottom>
          <strong>Prerequisites:</strong> Java 17 or later
          <ol>
            <li>
              Open a new terminal window and navigate to the services folder.
              This would be something like:
              <div style={{fontFamily: 'monospace', fontSize: 'smaller', margin: '15px 0'}}>
                $ cd weather-api-challenge/spring-rest
              </div>
            </li>
            <li>
              Next, run the Gradle command to build and launch the backend services:
              <div style={{fontFamily: 'monospace', fontSize: 'smaller', margin: '15px 0'}}>
                $ ./gradlew bootRun
              </div>
            </li>
            <li>
              Rest services are now accessible at:  <a href="http://localhost:8080">http://localhost:8080</a>
            </li>
          </ol>
        </Typography>
        <Typography variant="h6" gutterBottom>
          Launch the WeatherAPI React-UI
        </Typography>
        <Typography component="div" variant="body1" gutterBottom>
          <strong>Prerequisites:</strong> Node 18 or later
          <ol>
            <li>
              Open a new terminal window and navigate to the React-UI folder.
              This would be something like:
              <div style={{fontFamily: 'monospace', fontSize: 'smaller', margin: '15px 0'}}>
                $ cd weather-api-challenge/react-ui
              </div>
            </li>
            <li>
              Next, run the following two NPM commands to build and launch the React-UI:
              <div style={{fontFamily: 'monospace', fontSize: 'smaller', margin: '15px 0'}}>
                $ npm install
                <br/>
                $ npm run dev
              </div>
            </li>
            <li>
              The UI is now running and can be access through your browser at: <a href="http://localhost:3000">http://localhost:3000</a>
            </li>
          </ol>
        </Typography>
      </Box>
    </Container>
  );
}
