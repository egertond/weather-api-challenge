"use client"

import * as React from 'react';
import dayjs from 'dayjs';
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import { styled } from '@mui/material/styles';
import { debounce } from '@mui/material/utils';

import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';

import { DataGrid, GridColDef, GridValueGetterParams } from '@mui/x-data-grid';

import { BarChart } from '@mui/x-charts/BarChart';

import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { TimePicker } from '@mui/x-date-pickers/TimePicker';
import { renderTimeViewClock } from '@mui/x-date-pickers/timeViewRenderers';

import LocationOnIcon from '@mui/icons-material/LocationOn';

import { sensorApiUrl, SensorInfo, HistoryInfo } from '../utils/common';

export default function TasksPage() {
  const [filters, setFilters] = React.useState({"sensor": null, "startDate": null, "endDate": null});
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
      setOptions(filters.sensor ? [filters.sensor] : []);
      return undefined;
    }

    fetchInfo({ input: inputValue }, (results?: readonly SensorInfo[]) => {
      if (active) {
        let newOptions: readonly SensorInfo[] = [];
        if (results) newOptions = results;
        else if (filters.sensor) newOptions = [filters.sensor];
        setOptions(newOptions);
      }
    });

    return () => { active = false };
  }, [filters, inputValue, fetchInfo]);

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
          Sensor History Dashboard
        </Typography>
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'row',
            justifyContent: 'left',
            alignItems: 'left',
          }}
        >
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <Autocomplete
            id="sensor-selector"
            sx={{ margin: '0 15px 0 0', width: 400 }}
            getOptionLabel={(option) =>
              typeof option === 'string' ? option : option.name
            }
            filterOptions={(x) => x}
            options={options}
            autoComplete
            includeInputInList
            filterSelectedOptions
            value={filters.sensor}
            noOptionsText="No Sensors"
            onChange={(event: any, newValue: SensorInfo | null) => {
              setOptions(newValue ? [newValue, ...options] : options);
              setFilters({...filters, "sensor": newValue});
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
          <DatePicker
            label="Start Date"
            sx={{ margin: '0 15px 0 0' }}
            format="YYYY-MM-DD"
            value={filters.startDate}
            onChange={(newDate) => { setFilters({...filters, "startDate": newDate}) }}
            disableFuture
          />
          <DatePicker
            label="End Date"
            format="YYYY-MM-DD"
            value={filters.endDate}
            onChange={(newDate) => { setFilters({...filters, "endDate": newDate}) }}
            disableFuture
          />
        </LocalizationProvider>
        </Box>
      </Box>
      {renderForm(filters, setFilters)}
    </Container>
  );
}

function renderForm(filters, setFilters) {
  const [form, setForm] = React.useState<HistoryInfo>({});
  const [averageData, setAverageData] = React.useState(null);
  const [gridData, setGridData] = React.useState<HistoryInfo[]>([]);

  const fetchAverages = React.useMemo(
    () =>
      debounce(
        (
          request: { sensorId: string, startDate: string, endDate: string },
          callback: (results?: readonly HistoryInfo[]) => void,
        ) => {
          const options = {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
          };
          var url = sensorApiUrl
            + (request.sensorId == null ? '' : `/${request.sensorId}`)
            + `/averages?endDate=${request.endDate}&startDate=${request.startDate}`;
          fetch(url, options)
            .then((res) => res.json())
            .then((json) => callback(json));
        },
        400,
      ),
    [],
  );

  const fetchGridInfo = React.useMemo(
    () =>
      debounce(
        (
          request: { sensorId: string, startDate: string, endDate: string },
          callback: (results?: readonly HistoryInfo[]) => void,
        ) => {
          const options = {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
          };
          var url = sensorApiUrl
            + (request.sensorId == null ? '' : `/${request.sensorId}`)
            + `/history?endDate=${request.endDate}&startDate=${request.startDate}`;
          fetch(url, options)
            .then((res) => res.json())
            .then((json) => callback(json));
        },
        400,
      ),
    [],
  );

  React.useEffect(() => {
    let active = true;

    var newForm = {
      "name": filters?.sensor?.name,
      "description": filters?.sensor?.description,
      "sensorId": filters?.sensor?.id,
      "startDate": (filters?.startDate ?? dayjs().subtract(180, 'day')).format('YYYY-MM-DD'),
      "endDate": (filters?.endDate ?? dayjs()).format('YYYY-MM-DD'),
    }
    fetchGridInfo(
      { sensorId: newForm.sensorId, startDate: newForm.startDate, endDate: newForm.endDate },
      (results?: readonly SensorInfo[]) => {
        if(active) {
          setGridData(results ?? []);
          setForm(newForm);
        }
      }
    );
    fetchAverages(
      { sensorId: newForm.sensorId, startDate: newForm.startDate, endDate: newForm.endDate },
      (results?: readonly SensorInfo[]) => {
        if(active) {
          setAverageData(results);
        }
      }
    );

    return () => { active = false };
  }, [filters]);

  return (
    <Container>
      <Box
        component="form"
        sx={{
          '& > :not(style)': { m: 1, width: '50ch' },
        }}
        noValidate
        autoComplete="off"
      >
        <TextField
          label="Sensor Name"
          value={form.name == null ? '' : form.name + ", " + form.description}
          disabled
          variant="filled"
        />
        <TextField
          label="Sensor ID"
          value={form.sensorId ?? ""}
          disabled
          variant="filled"
        />
      </Box>
      { renderData(averageData, gridData) }
    </Container>
  );
}

function renderData(averageData, gridData) {
  if (averageData == null || (gridData?.length ?? 0) == 0) return (<></>);

  return (
    <Box sx={{ flexGrow: 1 }}>
      <Grid container spacing={2}>
        <Grid item xs={6}>
          { renderChart(averageData.data, "Rainfall (mm)", (value: number) => `${value}mm`) }
        </Grid>
        <Grid item xs={6}>
          { renderChart(averageData.data, "Snowfall (cm)", (value: number) => `${value}cm`) }
        </Grid>
        <Grid item xs={6}>
          { renderChart(averageData.data, "Temperature (°C)", (value: number) => `${value}°C`) }
        </Grid>
        <Grid item xs={6}>
          { renderChart(averageData.data, "Wind Direction (°)", (value: number) => `${value}°`) }
        </Grid>
        <Grid item xs={6}>
          { renderChart(averageData.data, "Wind Speed (km/h)", (value: number) => `${value}km/h`) }
        </Grid>
        <Grid item xs={12}>
          { renderGrid(gridData) }
        </Grid>
      </Grid>
    </Box>
  )
}

function renderChart(data, chartMetric, valueFormatter) {
  let chartSettings = {
    xAxis: [{ scaleType: 'band', dataKey: 'key' }],
    yAxis: [{ label: chartMetric }],
    series: [
      { dataKey: 'minValue', label: 'Minimum', valueFormatter },
      { dataKey: 'maxValue', label: 'Maximum', valueFormatter },
      { dataKey: 'meanValue', label: 'Mean', valueFormatter }
    ],
    height: 400,
  };
  let dataset = [];
  for(let key of Object.keys(data)){
     let metric = data[key].find(x => x.metric === chartMetric);
     dataset.push({ ...metric, key: key });
  }

  return (
    <BarChart dataset={dataset} {...chartSettings} />
  )
}

function renderGrid(data) {
  let gridColumns: GridColDef[] = [
    {
      field: 'name',
      headerName: 'Sensor',
      width: 150,
      valueGetter: (params: GridValueGetterParams) =>
        `${params.row.sensor.name}, ${params.row.sensor.description}`
    },
    { field: 'recordDate', headerName: 'Record Date', width: 120 },
    { field: 'sunrise', headerName: 'Sunrise', width: 150 },
    { field: 'sunset', headerName: 'Sunset', width: 150 },
    { field: 'rainfallSum', headerName: 'Rainfall (mm)', width: 120 },
    { field: 'snowfallSum', headerName: 'Snowfall (cm)', width: 120 },
    { field: 'temperatureMin', headerName: 'Temp Min (°C)', width: 120 },
    { field: 'temperatureMax', headerName: 'Temp Max (°C)', width: 120 },
    { field: 'temperatureMean', headerName: 'Temp Mean (°C)', width: 130 },
    { field: 'windDirection', headerName: 'Wind Direction (°)', width: 130 },
    { field: 'windSpeedMax', headerName: 'Max Wind Speed (km/h)', width: 170 }
  ];

  return (
    <DataGrid
      rows={data}
      columns={gridColumns}
      initialState={{
        pagination: {
          paginationModel: {
            pageSize: 20,
          },
        },
      }}
      pageSizeOptions={[20]}
      checkboxSelection
      disableRowSelectionOnClick
    />
  )
}