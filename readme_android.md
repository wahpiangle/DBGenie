The folder structure is as follows:
- Propdash-android (Android app)
- backend (Node.js backend)

To run the api, navigate to the backend folder
`cd backend`

and run the following commands:
`docker-compose up --build`

Retrieve the IPV4 address of your current machine by running
`ipconfig` on windows or `ifconfig` on linux

Then open the `APIService.kt` file in the Android app and change the BASE_URL to the IPV4 address of your machine.
For example if the IPV4 address is `123.456.78.90`, change the BASE_URL to
`private const val BASE_URL = "http://123.456.78.90:8080/"`
as the port of the backend server is `8080`.

Prisma Studio (a tool to view the database) is also available on port `5556`
if you would like to view the database.

Then run the Android app.
