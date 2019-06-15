# Workout Timer

My cardio kickboxing workout class is gone, but I still want to continue doing it.  Here's my attempt at a replacement.

The workout is made up of a number of rounds that include an alternating interval of activity and a rest periods.  The application uses the Web Speech API to perform text-to-speech so that the user doesn't have to watch the screen during their workout.

## Development

Once the server-side and client-side applications are started, the application will be available at [http://localhost:3000](http://localhost:3000)

### Backend

Start the server-side application by entering the REPL with `lein repl`, then run `(start)`.

If you are not planning on making client-side changes, you can run `(start-fw)` to start the client-side appliation.

### Frontend

Start the client-side application by entering the with `lein figwheel`.

Note: If the `cljs.user=>` prompt does not show up, close the tab with the application and open a new one.

If you are not planning on making server-side changes, you can also just run `lein run` to start the server-side application.


## Building for Deployment

To create a deployable jar file, execute the `build.sh` script.  This will output the cardio-kickboxing.jar file under the target/uberjar directory.

The application can be started using:
`export DATABASE_URL="jdbc:h2:./cardio_kickboxing_dev.db.mv.db"`
`java -jar target/uberjar/cardio-kickboxing.jar`.

The server will be available at [http://localhost:3000](http://localhost:3000)
