# aad-team-20-animation-challenge


This is an open source android project which offers information on the movies
and gives information about movies, the description of movies, ratings and reviews.
The app makes use of data fetched from IMDB API.


This app is a group effort with the following contributors:
// name the contributors

To fetch data from IMDB api Retrofit2 library is used.
Retrofit is a REST Client for Java Android. It makes it relatively easy to retrieve and upload JSON (or other structured data)
via a REST based webservice, IMDB API for this case.
Retrofit 2 by default leverages OkHttp as the networking layer and is built on top of it.
Retrofit automatically serialises the JSON response using a POJO(Plain Old Java Object) which must be defined in advanced for the JSON Structure.

Once the movies have been fetched from IDMB, they are displayed in a RecyclerView.
Movies are divided into three categories: Popular, top rated and  upcoming

Displaying movies utilizes master-detail navigation

Various animations are used in the application.
 Loading the movies into the activity
 Rotating the options menu
 Loading content of the detail view in the master-detail navigation
 Loading the splashscreen

The application has been developed using MVC pattern, i.e. Model View Controller







