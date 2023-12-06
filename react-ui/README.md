# Weather API Coding Challenge

## React UI Frontend

Not a required delivery, but thought this would help to better understand the idea behind 
how I had envisaged the API being used.

#### Launch the WeatherAPI React-UI

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

## UI Overview

#### Home Page

Simple page that really use repeats the README summary of the project

#### Register Sensor

Form that allows the use to fill in the details of a new Sensor. These details can be looked
up via a third-party API integration that has been included in the backend API. Submitting 
the form will add a new sensor in the database. this is then used to link history sensor data.

Another feature, is the ability to preload sensor history data when registering the new sensor. 
This has the benefit fo allow the user to better able to visualise the possibilities.

#### Add History

A form to simply capture new sensor history data. The more information captured, will result
in better metrics displayed.

#### Dashboard

Bringing together all the information captured for the sensors, the Dashboard tries to visualise
this in two ways.

1. Displaying the averaged sensor history in simple graphs.

2. Full extract of the history data in a simple data-grid.

The dashboard additionally has a few filtering options that allow the visualisation to be refined
to return history data for a specific sensor or time period. 
