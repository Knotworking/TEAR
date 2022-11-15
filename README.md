# TEAR App

>I wish I could say that my personal projects reflected my best work, but unfortunately it takes time to develop production quality apps and whilst working as a full-time android developer I often need to spend my remaining time on other pursuits. As they are just for my use, there's not the usual amount of attention paid to testing and polish that one would want for an app in the playstore, and I mostly use them to play around with different approaches.

A distance tracker for my 2022 hike across Europe.

As well as being very useful to me on my walk, it was also a great opportunity to play around with some of the latest Android development practices outside of my job. Whilst I used a lot of these approaches/frameworks professionally as well, its nice to be able to experiment with them more within the context of a smaller scale non-production project.

### App Features
* **Distance along the trail** - Using the GPS and a set of waypoints, I could find my closest point along the route and use that to calculate my total distance so far and by extension my progress as a percentage.
* **Update my location** - [My blog home page](https://benhikes.eu/) had a map of my planned route including a marker showing my postion. I added an API to be able to set the position of this marker and its text from the app. This way people could easily follow my progress along the way.

### Key Project Features

This project was written in late 2021, and so the various libraries and frameworks for this project use versions from that time, often in an experimental or beta state. I'm sure these practices will have progressed since then.
* **Compose** - This seems (or at least seemed at the time) to be the way Android views were/are going. Professionally I'd done a few projects with Flutter before this, and the move from Flutter UIs to Compose was fairly straight forward.
* **Coroutines** - Nothing too special here. I really like Kotlin coroutines, the code is often very concise and readable. I hadn't done too much with coroutine flow before this, but having used RxJava a lot in the past, the concepts were similar and easy to pick up.
* **Clean Architecture** - A similar architectural approach I've used for most of my career, so not much of an experiment. The project has the architecture layers (App/view - Domain - Data) separated into distinct modules so as to be completely decoupled. Within those modules the files are then organised in folders acording to feature.


### Main Screen
<img src="https://user-images.githubusercontent.com/25524912/201879351-08ea074a-6483-4309-a658-a58bd1ec9584.png" width="300"/>

### Info Dialog
<img src="https://user-images.githubusercontent.com/25524912/201879850-d0c34713-bfa7-4989-a40a-7fa260ae8450.png" width="300"/>

### On the website
![Screenshot 2022-11-15 at 10 20 59](https://user-images.githubusercontent.com/25524912/201880778-aa83d3ca-1419-4eed-9b57-03614dbb7023.png)

### Links

* [My blog](https://benhikes.eu/) - Documenting some of my previous hikes.
* [Blog post specifically about the app](https://benhikes.eu/tear/mobile-app) - This isn't a technical post, it's more about the context in which I created it.
