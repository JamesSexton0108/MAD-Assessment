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

Part 3

For part three, I will utilize the Room API in order to allow the user
to save all points of interest to an SQL database. This could be done
without Room, but using it means that all SQL logic can be kept away
from the main activity in such a way that is neat and tidy. To use Room
efficiently, I will need to create a DAO which will control all
interactions with the database. For this requirement, that will just be
inserting all POIs that have been added to the map. There will also need
to be a data entity, which essentially just defines the data that will
be put into the table. As I am using the given pointsofinterest.db
table, the data entity will have to match the pointsofinterest table
with columns for id, name, type, country, region, lon, lat, description
and recommendations. id will also use autoGenerate = true, meaning it
will match the autoincrement behaviour of the database. A database class
will also need to be created as that is what the Room DAO will operate
through; it encapsulates the database as a whole. The database will need
to exist as a companion object in order to ensure that multiple copies
of the database are never created. Room is also useful as it integrates
well with the preexisting viewmodel. This is because the LiveData list
of POIs can be obtained directly from the DAO and stored as a property
which can be observed.

Part 4

For part 3, I made a mistake when planning in that I assumed the
pointsofinterest.db table used for later web based tasks was the table
that would be accessed by Room in these tasks as opposed to a different,
local database being used. As such, part of the part 3 segment is wrong,
but I have kept it the same in the spirit of the assignment. As a result
of this confusion, I ended up implementing the first problem of part 4,
as the points of interest stored in the local database are automatically
loaded onto the map as symbols whenever the app starts. As described at
the end of part 3, this is done using the getAll() method in the DAO
which returns LiveData\<List\<POIEntity\>\> which is converted into a
list of PointOfInterest objects stored by the viewmodel, which is then
iterated through on the map screen, assigning each POI a symbol. onClick
can be used to detect when the user taps a symbol, which can then be
used in conjunction with AlertDialog to show the relevant POIs details.
A nullable state variable will be used to track which POI has been
tapped, defaulting to null when no marker has been selected. When a
marker is tapped its corresponding POI will be stored in this variable,
triggering the dialog to appear. The onDismissRequest parameter of
AlertDialog will handle the case where the user taps outside the dialog,
and a Close button will be provided as the confirmButton; both will
reset the state variable to null, dismissing the dialog.

Part 5

For part 5, I will create a new query method in the DAO which will
search the local database for points of interest with a desired type.
This can be achieved with the SQL statement "SELECT \* FROM
pointsofinterest WHERE type = :type". As with the first query method
getAll(), this will return LiveData\<List\<POIEntity\>\>, which can then
be converted to PointOfInterest objects by the viewModel ready to be
displayed on the map. Because LiveData is used, the map will update
automatically when results are delivered without any manual refresh
being needed. The POIs with the searched for types will then be
highlighted on the map using the circle instead of the marker. These
circles will of course retain the ability to be clicked, showing their
details as any other marker would.
