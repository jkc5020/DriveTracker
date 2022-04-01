# DriveTracker
## Overview
This Android app is intended to be used to calculate a 
user's estimated cost of gas by tracking drives and logging them

## Components/Technologies used
The app uses several key components to function
- FE
  - RecyclerView/RecylerAdapter - Used to log all the previous drives
  - SharedPreferences - saves state of app to SharedPreferences
- BE
  - Foreground service - The foreground service is used during each drive and performs several tasks. This information is then stored in a trip object containing 
    the total miles, total gallons, and total time
  1. FusedLocationProviderClient - A part of Google Play services, this returns the user location based on the current most reliable option
  2. Runnable - Runs a stopwatch on the background thread to track the duration of the drive
  
  - API Call using Volley - Using the Volley library developed by Google, the app makes use of the [FuelEconomy.gov API](https://www.fueleconomy.gov/feg/ws/) to 
    retrieve the mpg for the user's vehicle, which is specified in the UI
  
## Current bugs
- Formatting issue with mile count once drive has commenced - simple String formatting change
- After a car is added, information about car is displayed prior to API response, so the app falsely displays the mpg of the car as 0.   

## References
- [Volley library tutorial](https://www.geeksforgeeks.org/volley-library-in-android/)
- [More Volley info](https://www.geeksforgeeks.org/how-to-post-data-to-api-using-volley-in-android/)
- [Runnable Stopwatch tutorial](https://www.geeksforgeeks.org/how-to-create-a-stopwatch-app-using-android-studio/)
- [Dynamic views Tutorial](https://www.geeksforgeeks.org/how-to-add-views-dynamically-and-store-data-in-arraylist-in-android/)
- [Recyclerview Tutorial](https://www.geeksforgeeks.org/android-recyclerview/)
- [BottomNavigationView Tutorial](https://www.geeksforgeeks.org/bottomnavigationview-inandroid/)
- [User Location Tutorial](https://www.geeksforgeeks.org/how-to-get-user-location-in-android/)
- [Restful API call Video ](https://www.youtube.com/results?search_query=make+restful+api+call+android)
- [FusedLocationProvider Video](https://www.youtube.com/results?search_query=FusedLocationProviderClient+android)
