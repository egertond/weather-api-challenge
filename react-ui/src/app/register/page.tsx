"use client"

import * as React from 'react';
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import { debounce } from '@mui/material/utils';

import Autocomplete from '@mui/material/Autocomplete';
import Button from '@mui/material/Button';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';
import Snackbar from '@mui/material/Snackbar';
import TextField from '@mui/material/TextField';

import LocationOnIcon from '@mui/icons-material/LocationOn';

import { locationApiUrl, LocationInfo, sensorApiUrl } from '../utils/common';

export default function RegisterPage() {
  const [location, setLocation] = React.useState<LocationInfo | null>(null);
  const [inputValue, setInputValue] = React.useState('');
  const [options, setOptions] = React.useState<readonly LocationInfo[]>([]);

  const fetchInfo = React.useMemo(
    () =>
      debounce(
        (
          request: { input: string },
          callback: (results?: readonly LocationInfo[]) => void,
        ) => {
          const options = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: request.input })
          };
          fetch(locationApiUrl, options)
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

    fetchInfo({ input: inputValue }, (results?: readonly LocationInfo[]) => {
      if (active) {
        let newOptions: readonly LocationInfo[] = [];
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
          Register New Sensor
        </Typography>
        <Autocomplete
          id="location-selector"
          sx={{ width: 600 }}
          getOptionLabel={(option) =>
            typeof option === 'string' ? option : option.name + ", " + (option.region == null ? "" : option.region + ", ") + option.countryCode
          }
          filterOptions={(x) => x}
          options={options}
          autoComplete
          includeInputInList
          filterSelectedOptions
          value={location}
          noOptionsText="No locations"
          onChange={(event: any, newValue: LocationInfo | null) => {
            setOptions(newValue ? [newValue, ...options] : options);
            setLocation(newValue);
          }}
          onInputChange={(event, newInputValue) => {
            setInputValue(newInputValue);
          }}
          renderInput={(params) => (
            <TextField {...params} label="(Optional) Search for a location" fullWidth />
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
                      {option.region == null ? "" : option.region + ", "}
                      {option.countryCode}
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

function renderForm(location: LocationInfo | null, setLocation) {
  const [form, setForm] = React.useState<LocationInfo>({});
  const [disableButton, setDisableButton] = React.useState(false);
  const [showMessage, setShowMessage] = React.useState([false, null]);

  React.useEffect(() => {
    if (location != null && (form.name != location.name || form.countryCode != location.countryCode || form.region != location.region)) {
      location.loadSensorData = false;
      setForm(location);
    }
  }, [location]);

  const handleSubmit = (event) => {
    event.preventDefault();
    setDisableButton(true);
    form.description = (form.region == null ? "" : form.region + ", ") + form.countryCode;
    const options = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form)
    };
    fetch(sensorApiUrl, options)
      .then((res) => {
        if (res.status == 200) return res.json();
        else throw new Error("Failed to register sensor!")
      })
      .then((json) => {
        setShowMessage([true, "Sensor registered successfully"]);
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
      <TextField
        label="Name"
        value={form.name ?? ""}
        onChange={(event) => { setForm({...form, "name": event.target.value}) }}
        InputLabelProps={{ shrink: (form.name != null) }}
        required
        error={form.name == ''}
        helperText={form.name == '' ? 'Name is a required field!' : ''}
      />
      <TextField
        label="Region"
        value={form.region ?? ""}
        onChange={(event) => { setForm({...form, "region": event.target.value}) }}
        InputLabelProps={{ shrink: (form.region != null) }}
        required
        error={form.region == ''}
        helperText={form.region == '' ? 'Region is a required field!' : ''}
      />
      <TextField
        label="Country Code"
        value={form.countryCode ?? ""}
        onChange={(event) => { setForm({...form, "countryCode": event.target.value}) }}
        InputLabelProps={{ shrink: (form.countryCode != null) }}
        required
        error={form.countryCode == ''}
        helperText={form.countryCode == '' ? 'Country Code is a required field!' : ''}
      />
      <TextField
        label="Time Zone"
        value={form.timeZone ?? ""}
        onChange={(event) => { setForm({...form, "timeZone": event.target.value}) }}
        InputLabelProps={{ shrink: (form.timeZone != null) }}
        required
        error={form.timeZone == ''}
        helperText={form.timeZone == '' ? 'Time Zone is a required field!' : ''}
      />
      <TextField
        label="Elevation (m)"
        value={form.elevation ?? ""}
        onChange={(event) => { setForm({...form, "elevation": parseFloat(event.target.value)}) }}
        InputLabelProps={{ shrink: (form.elevation != null) }}
        required
        error={form.elevation <= 0 || form.elevation > 9000}
        helperText={form.elevation <= 0 || form.elevation > 9000 ? 'Elevation is a required field (between 0 and 9000)!' : ''}
        type="number"
        InputProps={{ inputProps: { max: 9000, min: 0 } }}
      />
      <TextField
        label="Longitude (°)"
        value={form.longitude ?? ""}
        onChange={(event) => { setForm({...form, "longitude": parseFloat(event.target.value)}) }}
        InputLabelProps={{ shrink: (form.longitude != null) }}
        required
        error={form.longitude <= -180 || form.longitude >= 180}
        helperText={form.longitude <= -180 || form.longitude >= 180 ? 'Longitude is a required field (between -180 and 180)!' : ''}
        type="number"
        InputProps={{ inputProps: { max: 180, min: -180 } }}
      />
      <TextField
        label="Latitude (°)"
        value={form.latitude ?? ""}
        onChange={(event) => { setForm({...form, "latitude": parseFloat(event.target.value)}) }}
        InputLabelProps={{ shrink: (form.latitude != null) }}
        required
        error={form.latitude <= -90 || form.latitude >= 90}
        helperText={form.latitude <= -90 || form.latitude >= 90 ? 'Latitude is a required field (between -90 and 90)!' : ''}
        type="number"
        InputProps={{ inputProps: { max: 90, min: -90 } }}
      />
      <FormControlLabel
        label="Load Sensor History"
        control={<Checkbox checked={form.loadSensorData ?? false} onChange={(event) => { setForm({...form, "loadSensorData": event.target.checked}) }} />}
      />
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