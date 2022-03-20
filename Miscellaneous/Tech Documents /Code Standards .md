# <u>Code Standards</u> 

## Introduction 

This document is used to define code standards for the project <b><i>Track My Shot </i></b>. Track My Shot, is a native Android mobile application that allows you to track your shooting percantage by two ways: 

1. <b><i>Manually Importing Shooting Percentage </i> </b>
2. <b><i> Tracking Your Live Shooting Percntage through Voice Commands  </i></b>

A code standard document ensures a apperance and design written approach, for code by a developer(aka myself). It improves readablity of said code and also reduces complexiity. In addition, it helps code become reused when needed by following these patterns. These patterns, will be split up into said categories; to allow for a easy to read and or follow; document. 

## Kotlin Style Guide 

For this project were will be following the Kotlin Style Guide found here: [Kotlin Style Guide](https://developer.android.com/kotlin/style-guide). 

## MVVM(Model-View-ViewModel) with Jetpack Compose 

MVVM is a software design pattern that is sstructed to sperate program logic and user interface controls. We do this in most cases, with the Fragment, ViewModel, data class, and Layout strucutre. 

- <b> Fragment </b> : Used to listen to data updates made by the <I>ViewModel </i>to then get inside the Fragment, which has direct access to the Layout by using data binding. 
- <b> ViewModel </b>: Handles all of the business / program logic It will send updates to the model data classes or declartions, to which will be observed by the Fragment and Layout to make said UI changes. 
- <b> Data </b> : Used to declare different type of data stored in said 
- <b> Layout </b> : Direct user interface of said screen. 

With knowing the 4 most important tiers to follow when using this software pattern, here are a couple of things to follow under said stucture. 

- Business logic should be contained in ViewModels and tested 
- Name of the ViewModels methods should reflection the action it is being done, in said function 
  - Example: `setPersonData()` is a function, that would be responsible for setting a person attributes. Reading the function hint, abstracts that definition of what the function does outside of someone explaning it(functions name should be read, to know what they are actually doing). 

- Name of `onClick` functions in the viewModel should start with the `on` keyword, and follow with `click` . 
- ViewModels shold be injected with [Dagger](https://developer.android.com/codelabs/android-dagger#0)
- VeiewModels depdencies should be injected in the constructor; so that way they can be mocked and used for Unit testing.  
- Fragments should observe `Flows` based on `viewLifeCycleOwner` 
- ViewModels should not contain any Android framework dependecies 
- A ViewModel should never reference Views context to avoid memory leaks(thats the Fragments job). 
- ViewModels should include `ViewState` interface which then has a `impl` data class, which gets set from the ViewModel. The ViewModel, should then pass in the ViewState to the view to change UI when necessary. 
- Keep View classes towards a minium 
- Components written in Jetpack Compose, should take in viewModel in order to set data(while the Fragment is responsible, for building out said Components when necessary) its a middlewear for Compose and the ViewModel 

## Extension Functions 

Extension functions are used, when we don't want repeat functionality across multiple instances. Ext functions are good for a number of reasons, and will be introduced in a `:ext-functions` module; that will bue used when necessary. 

## Modules 

Android Modules, are good to keep code organized and properly defined. Each feature will have a module, that will be used when building out said features. 

