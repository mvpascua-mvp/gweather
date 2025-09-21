# gweather
Connection of Weather API to Android Studio
# app description and architecture
this app is created full kotlin with jetpack compose, ktor, room database, firebase, WindowSizeClass, hilt and dagger 
jetpack compose is used for tthe ui of the app
ktor for the network connection but we can also use retrofit
room database for the offline database - store the data offline, can also use shared preference
firebase for email and password authetication(login and register) since it is already secured but we can also create an api using asp.net abd use ktor to connect them(RESTful Api) and use bcrypt for password hashing and aes for extra security measures
hilt and dagger for dependency injection
WindowSizeClass for adaptability of the UI to be compatible and responsive in all screen sizes 

when the app starts up it will displlay the login/register page, upon sucessful authentication it will display the first tab your current location and the weather upon clicking the second tab it will display the history and time fetched of the api

## this is the setup of the app

The Firebase Authentication is already configured on my gmail

1. The Api key can be added to the local.properties
24b7c0be8995445413ad936923817770 - this is the api key that I used do not add quotation mark("") when adding the api key 



