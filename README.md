# LivefrontProject
A sample project for Livefront.

It consumes a simple API that returns a list of characters from comics, tv shows, 
and movies (things like DC, Marvel, Star Trek, Star Wars, ect) and lets the user look them
up and see more information about them.

## Structure 
The project uses MVVM and is in a single module. 
For a larger project I would likely have used multiple modules and stored the data in a room database
but it didn't seem like something that was required in this case.

## Libraries used 
 * Dependency injection : Hilt
 * Networking : Retrofit
 * Serialization : Moshi
 * Testing : Mockk

| HomeScreen | Detail Screen |
|---|---|
 <img src="https://github.com/OlegSemenenkHome/LivefrontProject/blob/main/assets/HomeScreen.png" width="350">| <img src="https://github.com/OlegSemenenkHome/LivefrontProject/blob/main/assets/Batman.png" width="350"> |
