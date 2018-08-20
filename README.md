Restaurants android app displays the best rated restaurants in a specific city.

 Features:
1. Searchable list of cities (worldwide).
2. A map that displays the location of the best rated restaurants (after selecting a city).
3. Clicking on a restaurant displays the details of the selected restaurant.

APIs
- Restaurants - https://developers.zomato.com/api 
- Cities - http://simplemaps.com/resources/world-cities-data 
- Maps - Apple MapKit / Google Maps

NetWork library : 
Fast Android Networking Library

Restrictions
- application doesn't check internet connection
- application doesn't handle network errors
- restaurants API provides information  for 24 countries only
- restaurants API and cities API have different coordinates of the city and sometimes different city and country name (e.g. United States vs United States of  America).
To be sure that application receives information by specific city, application checks city location responded from Restaurants API and chooses a most appropriate result in 10 km radius around sent city location (Cities API)
