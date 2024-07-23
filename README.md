# Mini Sofascore
Sofascore Android Academy 2024

## Table of Contents

- [Overview](#overview)
- [Built With](#built-with)
- [Features](#features)
- [Contact](#contact)

## Overview

![mini_sofascore_display](https://github.com/user-attachments/assets/ae25b8b9-4c01-4395-b9ed-bee44fbcac3d)

An app that mimics the real Sofascore app for displaying sports results with limited features.

This project helped me the most with getting familiar with Kotlin and all the popular Android components and libraries.
Other than that I have gotten much better with structuring and organizing my code, separating the business logic from the UI and network calls.
It also gave me confidence to pursue Android development as more than just a hobby, I'm excited to keep improving and developing bigger Android apps.

### Built With

- MVVM Architecture
  + ViewModels are used as a connection between the data (model) and the UI (view)
- Android Jetpack Components
  + LiveData is used for passing data from ViewModels to Fragments and Activites
  + Paging3 Library is used for paging long lists such as all tournament matches and team matches
  + AppCompat is used for changing App language and theme
  + NavigationUI is used for navigation between fragments in activities
- Async
  + Kotlin Coroutines are used for handling async actions
  + Flow is used mostly with the Paging functionality
- Network
  + API calls are made to a REST API with the Retrofit client
  + Returning JSON objects are parsed into model classes, some with custom JSON deserializers
  + Image loading implemented with Coil library
- Version control and Code review
  + This repo was used exclusively for the project versioning
  + Code reviews were done on a weekly basis in PRs with Sofascore Mentors

## Features

- Main List
  + Shows matches for selected day (all matches from the API are on weekends)
  + Date rail for changing selected day
  + Tabs for changing sport (Football, Basketball and American Football supported)
- Tournament
  + Shows details for selected tournament or league (eg. Premier League)
  + Matches are displayed with paging for each match round
  + Tournament standings are displayed in the other tab (each sport has its own table contents)
- Team Details
  + Shows details and some stats for selected sports team/club (eg. Liverpool)
  + Matches are displayed similarly to the tournament matches with paging
  + Standings for where the team stands in the tournament table
  + Team squad with all the players in the team/club
- Event Details
  + Shows incidents that happened in each match
  + Different UI for each state of event (not stared, live, finished)
- Leagues
  + Shows all the tournaments and leagues available to view
  + Same tabs to select sports as from Main List
- Settings
  + Allows some basic settings such as changing app theme and language (Croatian and English supported)
  + Date format can be changed to American Format (MM/DD/YYYY)
  + Some information about the app

## Contact

For any questions about the app please feel free to contact me 
- Email: matko.papic@gmail.com
- LinkedIn: www.linkedin.com/in/matko-papic-19230725b
