# Virtual White Board

## The Participant

Hi, my name is T and for the purposes of this case I have played the role of backend developer. I have developed most of the features related to Login, and Role Management as well as most of the Virtual Board tasks nok relating to the frontend specifically.

It is possible to submit, view and delete posts and comments. All resources are authenticated and the Admin (user: sam@virtualboard.com, pass: adminpass) is able to view everything as well as add new regular users and delete other people's post.

## Tech stack

The API is built with Spring Boot and secured with Spring Security. For storage the Java-based database H2 is used. The requirements to build the code is JDK 1.8+ and Gradle, it can be run from your IDE by running the SpringBootApiApplication::main method or it can be built as a .jar with the gradle jar command. Any API client, such as Postman, should be able to access the API for manual testing.

## Data Storage

For simplicity all data storage is done in-memory, this goes for both authentication, which is implemented using Spring's InMemoryUserDetailsManager, as well as the backend database which uses H2. Clearly neither choice is suitable for a production environment where each responsibility should be handled by external services. It could also be argued that conceptually a NoSQL database would be better suited for the purposes of persisting posts on a virtual white board, but on the other hand the current design lends itself better to analyzing comment statistics.
