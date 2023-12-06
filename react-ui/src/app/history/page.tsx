"use client"

import * as React from 'react';
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import { debounce } from '@mui/material/utils';

import Autocomplete from '@mui/material/Autocomplete';
import Button from '@mui/material/Button';
import Snackbar from '@mui/material/Snackbar';
import TextField from '@mui/material/TextField';

import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { TimePicker } from '@mui/x-date-pickers/TimePicker';
import { renderTimeViewClock } from '@mui/x-date-pickers/timeViewRenderers';

import LocationOnIcon from '@mui/icons-material/LocationOn';

import { sensorApiUrl, SensorInfo, HistoryInfo } from '../utils/common';

export default function HistoryPage() {
  const [location, setLocation] = React.useState<SensorInfo | null>(null);
  const [inputValue, setInputValue] = React.useState('');
  const [options, setOptions] = React.useState<readonly SensorInfo[]>([]);

  const fetchInfo = React.useMemo(
    () =>
      debounce(
        (
          request: { input: string },
          callback: (results?: readonly SensorInfo[]) => void,
        ) => {
          const options = {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
          };
          fetch(`${sensorApiUrl}?query=${request.input}`, options)
            .then((res) => res.json())
            .then((json) => callback(json));
        },
        400,
      ),
    [],
  );

  React.useEffect(() => {
    let active = true;

    if (inputValue === '') {
      setOptions(location ? [location] : []);
      return undefined;
    }

    fetchInfo({ input: inputValue }, (results?: readonly SensorInfo[]) => {
      if (active) {
        let newOptions: readonly SensorInfo[] = [];
        if (results) newOptions = results;
        else if (location) newOptions = [location];
        setOptions(newOptions);
      }
    });

    return () => { active = false };
  }, [location, inputValue, fetchInfo]);

  return (
    <Container>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'left',
          alignItems: 'left',
          borderBottom: '2px grey solid',
          paddingBottom: '10px',
          marginBottom: '10p'
        }}
      >
        <Typography variant="h4" gutterBottom>
          Add Sensor History Data
        </Typography>
        <Autocomplete
          id="sensor-selector"
          sx={{ width: 600 }}
          getOptionLabel={(option) =>
            typeof option === 'string' ? option : option.name
          }
          filterOptions={(x) => x}
          options={options}
          autoComplete
          includeInputInList
          filterSelectedOptions
          value={location}
          noOptionsText="No Sensors"
          onChange={(event: any, newValue: SensorInfo | null) => {
            setOptions(newValue ? [newValue, ...options] : options);
            setLocation(newValue);
          }}
          onInputChange={(event, newInputValue) => {
            setInputValue(newInputValue);
          }}
          renderInput={(params) => (
            <TextField {...params} label="Select Sensor to add history" fullWidth />
          )}
          renderOption={(props, option) => {
            if(props.key != null) delete props.key;
            return (
              <li key={props.id} {...props}>
                <Grid container alignItems="center">
                  <Grid item sx={{ display: 'flex', width: 44 }}>
                    <LocationOnIcon sx={{ color: 'text.secondary' }} />
                  </Grid>
                  <Grid item sx={{ width: 'calc(100% - 44px)', wordWrap: 'break-word' }}>
                    <Box component="span" sx={{ fontWeight: 'bold' }}>
                      {option.name}
                    </Box>
                    <Typography variant="body2" color="text.secondary">
                      {option.description}
                    </Typography>
                  </Grid>
                </Grid>
              </li>
            );
          }}
        />
      </Box>
    {renderForm(location, setLocation)}
    </Container>
  );
}

function renderForm(location: SensorInfo | null, setLocation) {
  const [form, setForm] = React.useState<HistoryInfo>({});
  const [disableButton, setDisableButton] = React.useState(false);
  const [showMessage, setShowMessage] = React.useState([false, null]);

  React.useEffect(() => {
    if (location != null && form.sensorId != location.id) {
      setForm({"sensorId": location.id});
    }
  }, [location]);

  const handleSubmit = (event) => {
    event.preventDefault();
    setDisableButton(true);
    var recordDate = form.recordDate.format('YYYY-MM-DD');
    var submitForm = {
      ...form,
      "recordDate": recordDate,
      "sunrise": `${recordDate}T${form.sunrise.format('HH:mm')}`,
      "sunset": `${recordDate}T${form.sunset.format('HH:mm')}`
    };
    delete submitForm.sensorId;
    const options = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(submitForm)
    };
    fetch(`${sensorApiUrl}/${form.sensorId}/history`, options)
      .then((res) => {
        if (res.status == 200) return res.json();
        else throw new Error("Failed to add Sensor history!")
      })
      .then((json) => {
        setShowMessage([true, "Sensor history successfully added"]);
        setLocation(null);
        setForm({});
      },
      (err) => {
        setShowMessage([true, err.message]);
      });
  };

  return (
    <Box
      component="form"
      sx={{
        '& > :not(style)': { m: 1, width: '50ch' },
      }}
      noValidate
      autoComplete="off"
    >
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <TextField
          label="Sensor ID"
          value={form.sensorId ?? ""}
          disabled
          variant="filled"
        />
        <DatePicker
          label="Record Date"
          format="YYYY-MM-DD"
          value={form.recordDate ?? ""}
          onChange={(newDate) => { setForm({...form, "recordDate": newDate}) }}
          InputLabelProps={{ shrink: (form.recordDate != null) }}
          required
          disableFuture
          error={form.recordDate == ''}
          helperText={form.recordDate == '' ? 'Record Date is a required field!' : ''}
        />
        <TextField
          label="Rainfall (mm)"
          value={form.rainfallSum ?? ""}
          onChange={(event) => { setForm({...form, "rainfallSum": parseFloat(event.target.value)}) }}
          InputLabelProps={{ shrink: (form.rainfallSum != null) }}
          required
          error={form.rainfallSum < 0 || form.rainfallSum > 100}
          helperText={form.rainfallSum < 0 || form.rainfallSum > 100 ? 'Rainfall is a required field (between 0 and 100)!' : ''}
          type="number"
          InputProps={{ inputProps: { max: 100, min: 0 } }}
        />
        <TextField
          label="Snowfall (cm)"
          value={form.snowfallSum ?? ""}
          onChange={(event) => { setForm({...form, "snowfallSum": parseFloat(event.target.value)}) }}
          InputLabelProps={{ shrink: (form.snowfallSum != null) }}
          required
          error={form.snowfallSum < 0 || form.snowfallSum > 100}
          helperText={form.snowfallSum < 0 || form.snowfallSum > 100 ? 'Snowfall is a required field (between 0 and 100)!' : ''}
          type="number"
          InputProps={{ inputProps: { max: 100, min: 0 } }}
        />
        <TimePicker
          label="Sunrise"
          ampm={false}
          format="HH:mm"
          value={form.sunrise ?? ''}
          onChange={(newTime) => { setForm({...form, "sunrise":  newTime}) }}
          InputLabelProps={{ shrink: (form.sunrise != null) }}
          required
          error={form.sunrise == ''}
          helperText={form.sunrise == '' ? 'Sunrise is a required field!' : ''}
          viewRenderers={{
            hours: renderTimeViewClock,
            minutes: renderTimeViewClock,
          }}
        />
        <TimePicker
          label="Sunset"
          ampm={false}
          format="HH:mm"
          value={form.sunset ?? ''}
          onChange={(newTime) => { setForm({...form, "sunset":  newTime}) }}
          InputLabelProps={{ shrink: (form.sunset != null) }}
          required
          error={form.sunset == ''}
          helperText={form.sunset == '' ? 'Sunset is a required field!' : ''}
          viewRenderers={{
            hours: renderTimeViewClock,
            minutes: renderTimeViewClock,
          }}
        />
        <TextField
          label="Temperature Mean (°C)"
          value={form.temperatureMean ?? ""}
          onChange={(event) => { setForm({...form, "temperatureMean": parseFloat(event.target.value)}) }}
          InputLabelProps={{ shrink: (form.temperatureMean != null) }}
          required
          error={form.temperatureMean < -50 || form.temperatureMean > 50}
          helperText={form.temperatureMean < -50 || form.temperatureMean > 50 ? 'Temperature Mean is a required field (between -50 and 50)!' : ''}
          type="number"
          InputProps={{ inputProps: { max: 50, min: -50 } }}
        />
        <TextField
          label="Temperature Min (°C)"
          value={form.temperatureMin ?? ""}
          onChange={(event) => { setForm({...form, "temperatureMin": parseFloat(event.target.value)}) }}
          InputLabelProps={{ shrink: (form.temperatureMin != null) }}
          required
          error={form.temperatureMin < -50 || form.temperatureMin > 50}
          helperText={form.temperatureMin < -50 || form.temperatureMin > 50 ? 'Temperature Min is a required field (between -50 and 50)!' : ''}
          type="number"
          InputProps={{ inputProps: { max: 50, min: -50 } }}
        />
        <TextField
          label="Temperature Max (°C)"
          value={form.temperatureMax ?? ""}
          onChange={(event) => { setForm({...form, "temperatureMax": parseFloat(event.target.value)}) }}
          InputLabelProps={{ shrink: (form.temperatureMax != null) }}
          required
          error={form.temperatureMax < -50 || form.temperatureMax > 50}
          helperText={form.temperatureMax < -50 || form.temperatureMax > 50 ? 'Temperature Max is a required field (between -50 and 50)!' : ''}
          type="number"
          InputProps={{ inputProps: { max: 50, min: -50 } }}
        />
        <TextField
          label="Wind Direction (°)"
          value={form.windDirection ?? ""}
          onChange={(event) => { setForm({...form, "windDirection": parseFloat(event.target.value)}) }}
          InputLabelProps={{ shrink: (form.windDirection != null) }}
          required
          error={form.windDirection <= 0 || form.windDirection > 360}
          helperText={form.windDirection <= 0 || form.windDirection > 360 ? 'Wind Direction is a required field (between 0 and 360)!' : ''}
          type="number"
          InputProps={{ inputProps: { max: 360, min: 0 } }}
        />
        <TextField
          label="Max Wind Speed (km/h)"
          value={form.windSpeedMax ?? ""}
          onChange={(event) => { setForm({...form, "windSpeedMax": parseFloat(event.target.value)}) }}
          InputLabelProps={{ shrink: (form.windSpeedMax != null) }}
          required
          error={form.windSpeedMax < 0 || form.windSpeedMax > 100}
          helperText={form.windSpeedMax < 0 || form.windSpeedMax > 100 ? 'Max Wind Speed is a required field (between 0 and 100)!' : ''}
          type="number"
          InputProps={{ inputProps: { max: 100, min: 0 } }}
        />
      </LocalizationProvider>
      <Button disabled={disableButton} onClick={handleSubmit}>Submit</Button>
      <Snackbar
        anchorOrigin={{vertical: 'bottom', horizontal: 'right'}}
        open={showMessage[0]}
        autoHideDuration={6000}
        onClose={() => {
          setShowMessage([false, null]);
          setDisableButton(false);
        }}
        message={showMessage[1]}
      />
    </Box>
  );
}