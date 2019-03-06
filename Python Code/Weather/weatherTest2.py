import pyowm

API_key = '2c4cfd9f46764d63df3915877c10b49a'

owm = pyowm.OWM(API_key)
obs = owm.weather_at_place('Wrightstown, NJ')
w = obs.get_weather()

print(w.get_temperature('fahrenheit'))
print(w.get_detailed_status())