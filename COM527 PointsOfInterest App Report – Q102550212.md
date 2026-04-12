**COM527 PointsOfInterest App Report -- Q102550212**

Part 1

The first requirement for the app is to display a map of the user's
current location and surrounding area using the devices GPS location
displayed on Ramani Maps or osmdroid. To achieve this, the application
will need access to the user's precise location, which requires
requesting the ACCESS_FINE_LOCATION permission at runtime. This is
necessary because location data is considered sensitive by Android and
cannot be accessed without explicit user consent. I will then use a
ViewModel to store the user's current latitude and longitude as well as
the zoom level of the map. A ViewModel is beneficial here as it can be
updated and observed from anywhere within the app, meaning that we do
not need to constantly be passing location information between
functions. The information stored in the ViewModel will also persist
across recompositions in Compose. I will use a function called
onLocationChanged() to check the GPS location of the user's device, with
this information then being sent to the viewmodel. Finally a map will
display the latitude and longitude stored in the ViewModel.

Part 2

For requirement 2 I will need to create a separate composable for the
user to input location details from. I will then need to utilise a nav
host composable to host each composable screen, as well as a nav
controller to control the navigation between these screens. The separate
"add POI" composable will contain inputs for name, type and description
as well as a button to add the new location to the map and a button to
return to the map screen. The location will be marked on the map by a
maplibre symbol.
