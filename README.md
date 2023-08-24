# LoremPicsum

## Architecture
The architecture is MVVM/MVI. 

## Tech stack
- Kotlin Coroutines and Flows
- Hilt
- Retrofit
- Room
- Compose
- DataStore

## Modules
- app: entry point
- core: contains common components and classes
- data: contains api, database, repositories
- domain: contains domain models and use cases
- images: feature module, contains image list screen

## Main components
- views: dispatch user events, observes data flow
- view model: handle events and delegate tasks to use cases, expose data flows
- use cases: execute common business logic and delegate mapping into domain models to mappers
- repository: synchronizes data between data sources, exposes data flows
- mappers: map objects

## Object types
- dto: represents data structure on the server
- table: represents data structure in database
- domain: represents data on the screen
