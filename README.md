# Movie Database API

## Project Overview

The **Movie Database API** is a RESTful web service designed to manage a comprehensive collection of movie data. It provides functionality for managing movies, actors, and genres, including features such as searching, filtering, and performing CRUD operations. Built with Spring Boot, the API is optimized for efficiency, scalability, and ease of use, using an SQLite database for lightweight storage.

### Features
- **CRUD Operations** for Movies, Actors, and Genres
- Search functionality by:
  - Movie title
  - Actor name
  - Genre
- Filtering by release year and associated actors
- **Pagination** for manageable data responses
- SQLite database for simple and lightweight storage
- Basic error handling and input validation for robust API management

## Setup and Installation Instructions

### Prerequisites
Before you begin, ensure you have the following installed:
- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) or later
- [Maven](https://maven.apache.org/download.cgi) for building the project
- [SQLite](https://www.sqlite.org/download.html) for database management

### Clone the Repository
Clone the repository to your local machine:

 - git clone https://github.com/jyrgenandessalu/Movie-database.git

### Build the program
Navigate to the project directory and build the program:
 - mvn clean install

### Run the program
You can run the application two ways:
1. Use Maven to run the Spring Boot application directly:
 - mvn spring-boot:run
2. Run the main class MovieDatabaseApiApplication.java directly in your IDE.

### Database configuration
 - The application uses SQLite for the database. Ensure SQLite is installed and configured correctly in the application.properties file located under src/main/resources.
 - The database file (movie_database.db) should be present in the project root. If not, it will be created automatically when the application runs.


 ## Usage Guide

 ### Accessing the API
Once the application is running, you can access the API at http://localhost:8080. Below are some example endpoints. Use Postman to test API endpoints:

Movies:

- POST /api/movies: Create a new movie entry.  

Example:
  
 ```json
{
        "title": "The Conjuring",
        "releaseYear": 2013,
        "duration": 112,
        "actors": [2],
        "genres": [
            {
                "id": 4
            },
            {
                "id": 1
            }
        ]
    }
```
- GET /api/movies?page={page}&size={size}: Retrieve movies (supports pagination).
- GET /api/movies/{id}: Retrieve a specific movie by ID.
- PATCH /api/movies/{id}: Update a specific movie partially, add or remove actors by id.  

Example:
```json
{
    "actors": [2]
    }
```
- DELETE /api/movies/{id}: Delete a movie .

Actors:

- POST /api/actors: Create a new actor profile.  

Example:
```json
{
        "name": "Ryan Gosling",
        "birthDate": "1980-11-12"
    }
```
- GET /api/actors?page={page}&size={size}: Retrieve actors (supports pagination).
- GET /api/actors/{id}: Retrieve a specific actor by ID.
- PATCH /api/actors/{id}: Update actor details.
- DELETE /api/actors/{id}: Delete an actor profile.

Genres:

- POST /api/genres: Add a new genre.
- GET /api/genres?page={page}&size={size}: List all genres (supports pagination).
- GET /api/genres/{id}: Get details of a specific genre.
- PATCH /api/genres/{id}: Update genre information.
- DELETE /api/genres/{id}: Remove a genre.

### Importing the Postman Collection
You can import the Postman collection included in this repository.
After cloning the repository, you can find the Postman collection JSON file in the `/Postman collection` folder.

Steps to Import:
1. Open Postman.
2. Click the **Import** button in the top left corner of Postman.
3. Choose the **File** tab.
4. Navigate to the location of the repository on your local machine and select `Postman collection/Movie Database API.postman_collection.json`.
5. Click **Import**. The collection will now appear in your Postman workspace.
