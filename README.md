# BeerApp
Restaurants android app  displays the best rated restaurants  in a specific city. 
 Features:
1. Searchable list of cities (worldwide)
2. A map that displays the location of the best rated restaurants (after selecting a city) 
3.  Clicking on a restaurant displays the details of the selected  restaurant

APIs
- Restaurants - https://developers.zomato.com/api 
- Cities - http://simplemaps.com/resources/world-cities-data 
- Maps - Apple MapKit / Google Maps

NetWork library : 
Fast Android Networking Library

Restrictions
- application do not check internet connection
- application do not handle network errors
- API restaurants provides information  for 24 countries 
- API restaurants and API cities has different coordinates of the city and sometimes different city and country name (e.g. United States vs United States of  America). 
To be sure that application  receives information by specific city, Application checks response city location  from API Restorants  and chooses most appropriable result in radius 10 km from  sending City location (API Cities)
