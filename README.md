## Movies App – TUT0301-03

This repository contains a Java desktop application that allows users to explore movies, 
manage a personal watchlist, rate movies, and leave comments.  
The application is built with Java Swing and integrates with the TMDB (The Movie Database) API
to fetch real-time movie data.

## Features

- **User Accounts**
  - Create a new account
  - Log in with username and password
  - Stay logged in while using the main tabbed interface

- **Movie Browsing & Search**
  - View popular and recent movies on the home page
  - Search for movies by keyword
  - Open a detailed movie view with description and rating

- **Watchlist**
  - Add movies to a personal watchlist
  - Remove movies from the watchlist
  - View your watchlist in a dedicated “My Watchlist” tab

- **Ratings & Comments**
  - Rate movies and update your rating
  - Post comments for a specific movie
  - View comments related to a movie

## Architecture Overview

The project follows a layered / clean architecture style with clear separation of concerns:

- `entity` – Core business entities such as `User`, `Movie`, `Watchlist`, `Comment`
- `use_case` – Application use cases (login, create account, search, watchlist, rating, comments, etc.)
- `interface_adapter` – Controllers, presenters, and view models that adapt use cases to the UI
- `data_access` – Data access layer for users, comments, watchlists, and TMDB API access
- `view` – Swing UI components (login screen, home page, movie list, watchlist, comment views, etc.)

## Screenshots

- Login screen
  - ![app0.png](docs/screenshots/app0.png)
- Home / movie list view
  - ![app1.png](docs/screenshots/app1.png)
- Watchlist view
  - ![app2.png](docs/screenshots/app2.png)
- Movie details
  - ![app3.png](docs/screenshots/app3.png)
- Comment / rating views
  - ![app4.png](docs/screenshots/app4.png)
  - ![app5.png](docs/screenshots/app5.png)

