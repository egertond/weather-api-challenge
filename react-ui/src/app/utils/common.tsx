
export const locationApiUrl = "http://localhost:8080/api/lookup/locations";
export const sensorApiUrl = "http://localhost:8080/api/sensors";

export interface LocationInfo {
  name: string;
  region: string
  countryCode: string;
  timeZone: string;
  elevation: number;
  latitude: number;
  longitude: number;
  loadSensorData: boolean;
}

export interface SensorInfo {
  id: string;
  name: string;
  description: string;
  region: string
  countryCode: string;
  timeZone: string;
  elevation: number;
  latitude: number;
  longitude: number;
  loadSensorData: boolean;
}

export interface HistoryInfo {
  id: string;
  sensorId: string;
  recordDate: date;
  rainfallSum: number;
  snowfallSum: number;
  sunrise: date;
  sunset: date;
  temperatureMean: number;
  temperatureMin: number;
  temperatureMax: number;
  windDirection: number;
  windSpeedMax: number;
}
