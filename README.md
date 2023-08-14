# LivefrontProject
This is a sample Android application that allows users to search and view details of their favorite characters from various universes 
like DC, Marvel, Star Trek, Star Wars, and more. It fetches data from an external API to provide character details.

## Structure 
The app follows the MVVM (Model-View-ViewModel) architectural pattern, ensuring a clear separation of concerns and easier unit testing.

Although this project is structured as a single module, for more extensive projects, I would typically modularize different features and components. I also considered integrating a local database such as Room for caching, but given the scope of this sample project, it was deemed unnecessary.

## Tests
* Unit Tests:
These tests ensure the repository's data retrieval operations are seamless and accurate. The primary focus is on validating the correct functioning of data handling mechanisms within the repository.
* UI Tests:
These tests simulate standard user interactions to validate the application's main navigation pathways and the UI's responsiveness. By emulating typical user behaviors, the goal is to ensure a consistent and intuitive user experience.
* Continuous Integration:
GitHub Actions: 
To safeguard the codebase's quality, a GitHub Action has been configured. This action automatically triggers unit tests on every commit. 

## Libraries used 
 * Dependency injection: Hilt
 * Networking: Retrofit
 * Serialization: Moshi
 * Testing: Mockk, JUnit
 * Image Loading: Coil

## Preview 
| HomeScreen | Detail Screen |
|---|---|
 <img src="https://github.com/OlegSemenenkHome/LivefrontProject/blob/main/assets/HomeScreen.png" width="350">| <img src="https://github.com/OlegSemenenkHome/LivefrontProject/blob/main/assets/Batman.png" width="350"> |
