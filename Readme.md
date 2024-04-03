# Real Estate Manager

## Overview
Real Estate Manager is a comprehensive application designed to assist users in managing real estate properties.

## Getting Started
To run the Real Estate Manager app, follow one these methods:

### Method 1 Download the APK and install the App:
1. Navigate to the release or debug folder in the repository.
2. Download the APK file corresponding to your preferred build type. 
3. Transfer the downloaded APK file to your Android device. 
4. Open the APK file on your device to start the installation process. 
5. Follow the on-screen instructions to complete the installation.

### Build the app from android studio:
To enable location-based services, the app requires API key to access Google Maps API.

1. Obtain an API key from the Google Cloud Console.
2. Open the secrets.properties file in your top-level directory, and then add the following code: MAPS_API_KEY=YOUR_API_KEY
3. Create the local.defaults.properties file in your top-level directory, the same folder as the secrets.properties file, and then add the 
   following code:
   MAPS_API_KEY=DEFAULT_API_KEY
4. Build and run the app


## Features
- **Property Listings:** Browse a diverse range of real estate properties.
- **Location-Based Services:** Utilize Google Maps integration to view property locations.
- **Searches:** Search criteria for filtering.
