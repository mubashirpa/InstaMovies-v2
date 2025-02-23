# Insta Movies

Millions of movies, TV shows and people to discover. Explore now.

Welcome to Insta Movies! This app allows you to browse movies and TV shows using data from The Movie
Database (TMDb). It's packed with features and built with the latest technologies.

## **Download the App**

Insta Movies is currently available on the **Amazon Appstore**.

<a href='https://www.amazon.com/gp/product/B0DT4PWM5X'>
<picture>
  <source media="(prefers-color-scheme: dark)" srcset="https://images-na.ssl-images-amazon.com/images/G/01/mobile-apps/devportal2/res/images/amazon-appstore-badge-english-black.png">
  <source media="(prefers-color-scheme: light)" srcset="https://images-na.ssl-images-amazon.com/images/G/01/mobile-apps/devportal2/res/images/amazon-appstore-badge-english-white.png">
  <img alt="Get it on Amazon Appstore" src="https://images-na.ssl-images-amazon.com/images/G/01/mobile-apps/devportal2/res/images/amazon-appstore-badge-english-white.png" height='66px'>
</picture>
</a>

## Screenshots

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="screenshots/screenshot-dark.png">
  <source media="(prefers-color-scheme: light)" srcset="screenshots/screenshot.png">
  <img alt="App Screenshot" src="screenshots/screenshot.png">
</picture>

<picture>
  <source media="(prefers-color-scheme: dark)" srcset="screenshots/screenshot-tablet-dark.png">
  <source media="(prefers-color-scheme: light)" srcset="screenshots/screenshot-tablet.png">
  <img alt="App Screenshot" src="screenshots/screenshot-tablet.png">
</picture>

## Features

- **Dynamic window resizing**:
  The [WindowSizeClass](https://developer.android.com/reference/kotlin/androidx/compose/material3/windowsizeclass/WindowSizeClass)
  allows us to get to know about current device size and configuration and observe any changes in
  device size in case of orientation change or unfolding of device.

- **Dynamic fold detection**:
  The [WindowLayoutInfo](https://developer.android.com/reference/kotlin/androidx/window/layout/WindowLayoutInfo)
  let us observe all display features including Folding Postures real-time whenever fold state
  changes to help us adjust our UI accordingly.

- **Multiple Screens**: The app provides a seamless experience across different Android device
  screen sizes and orientations.

- **Dark Theme**: Enjoy a dark-themed interface that's easy on the eyes, especially in low-light
  conditions.

- **Material Design 3**: The app follows the latest Material Design guidelines for a modern and
  user-friendly UI.

- **Dynamic Colors**: The app dynamically adapts to different color schemes for a personalized
  experience.

## Architecture

The IMDb-like Android app is built with a robust architecture to ensure maintainability,
scalability, and clean code. We've used the MVVM pattern with Clean Architecture:

- **MVVM (Model-View-ViewModel)**: Separates the user interface from the business logic and data,
  making the app easy to test and maintain.

- **Clean Architecture**: Ensures a clear separation of concerns, making it easier to make changes
  in the future without disrupting the entire app.

## Technologies Used

- **Jetpack Compose**: The latest UI toolkit for Android to create a modern, reactive user
  interface.

- **Kotlin**: The programming language for Android development, known for its conciseness and
  safety.

- **TMDb API**: To fetch movie and TV show data.

## Getting Started

To run the app locally, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in Android Studio.
3. Build and run the app on an Android emulator or a physical device.

## Contributing

Contributions are welcome! If you'd like to contribute to this project, please fork the repository,
create a branch, make your changes, and submit a pull request.

## License

```
Copyright 2021 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

Feel free to explore the code and use this project as a reference for your own Android development
endeavors. If you have any questions or need assistance, please don't hesitate to reach out.

Happy coding!
