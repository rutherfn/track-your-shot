# Technical Design Document 

## Overview 

This document is used the technical design document for the project <b><i>Track My Shot </i></b>. Track My Shot, is a native Android mobile application that allows you to track your shooting percantage by two ways: 

1. <b><i>Manually Importing Shooting Percentage </i> </b>
2. <b><i> Tracking Your Live Shooting Percntage through Voice Commands  </i></b>

A technical design document aims to aid in the critcal analysis of a problem and the proposed solution, while also communicating priority, effort, and impact! 

## Problem Statement 

Based on other mobile apps out in the indusitry, I predict that Track My Shot will be a tool to increase basektball enagement over a peroid of time. 

## How Will It Work? 

Users will download the <b> Track My Shot  </b> mobile Android application on the Google Play Store, and will then follow these said steps: 

1. <b> Add A Player In The App </b>
2. <b> Once player is added, choose between either manual or live importing shooting makes and misses </b>
3. <b> if the user chooses to manual enter data, they will be asked the following </b>
   1. Where was the shot taken?(they will be able to select any shot you can possibly take on the basektball court). 
   2. How many shots did they attempt? 
   3. How many shots they did make out of there attempted shots 
   4. What date were these shots attempted on? 
4. <b> If the user chooses to live import said data, they will follow the following actions </b>
   1. Adding where are they are currently going to take said shot 
   2. Asking for a made voice command. 
      1. User will be prompted to record a voice over that they will say when they make the shot. If the user makes a shot they will say that said voice command, and it will trigger as a shot made towards there overall attempts 
      2. User will get asked for said permissions if not enabled. 
      3. User will record voice prompt 
      4. Oncce they are statisfy they will continue through there workflow 
         1. If the user is not statisfy with there voice command, they can make aother said voice command 
   3. Ask for a missed voice commnad 
      1. User will be prompted to record a voice over that they will say when they missed the shot. If the user misses the shot they will say that said voice command, and it will trigger as a shot missed towards there overall attempts 
      2. User will get asked for said permissions if not enabled. 
      3. User will record voice prompt 
      4. Once they are statisfy they will conitune there workflow 
         1. If the user is not statisfy with there voice command, they can make another said voice command 
   4. Once voice commands are recorded, user will be asked for how long they want to take there shots for? In which they can set a timer, and when the timer expires it will then record shots made or missed from that certain spot. 
   5. User will press the start button, and will record via voice shots made or missed until time expires 
   6. Once time expires, user will have there data added towards the app 
   7. User then will be able to compare abd view stats in the app(to do feature)

## Development Timeline 

1. Month 1: Setting up project stucture --  feature<b> Ability to create, edit, view, and delete a person </b>
2. Month 2: -- feature <b> Ability to add stats for given player manually </b>
3. Month 3: -- feature <b> Ability to add stats for given player live </b>
4. Month 4: TBD 

<h2> Development Milestones </h2>

1. Milestone 1: Ability to create person 
2. Milestone 2: Ability to edit person 
3. Milestone 3: Ability to view person 
4. Milestone 4: Ability to delete person 
5. Milestone 5: Ability to record given stats for player manually 
6. Milestone 6: Ability to record given stats for player live
7. Milestone 7: Ability to view those stats 
8. Milestone 8: TBD 

<h3> How Is Success Measured </h3> 

Success for this project is measured through small actionable wins that will be recorded in [Trello](https://trello.com/b/UAh3A6Yj/project-track-my-shot) these will be recorded in Github through pull requests≥ From there, milestones completition will also be recorded under epics. 

<h2> Libraries To Be Used For Said Project </h2>

There are a long range list of based libraries used for the project, and the guideline will be used to follow all Libraries used and what for: 

1. <b> Coil </b> : An image loading library for Android backed by Kotlin Coroutines 
2. <b> Coroutines </b>: Concurrency design pattern to simplify code pattern and excutes asynchronously. 
3. <b> Dagger </b> : Is a dependency injection framework that is stricly generated implmentation. 
4. <b> Room </b> : Is  a persistance library provides a abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite. 
5. <b> Flows </b> : Used from coroutines, its a type tha can emit multiple values sequentitally as opposed to suspend functions that only return one single value. 
6. <b> Compose </b>: Moden toolkit for building native UI 
7. TBD As I add them 

<h2> Implementation Intro / Pattern </h2>

TBD 

<h2> Launch Plan </h2>

TBD 