# eu-clients-service
Demo service to register EU clients
Provides integration with 3d party 'restcountries' service
    
Used technologies : Java 11, Spring, Swagger, JGraphT, H2, Spock, Wiremock

Please use the next links

http://localhost:8080/h2-console/ -- to work with in-memory db

http://localhost:8080/swagger-ui.html -- to review and test implemented api documentation

Please note user data is only be accessible to user who submitted it. 
Please use basic authentication to work with user endpoint, 
submitted user's email must be used for name in auth form
submitted user's password must be used for password in auth form

The app can be tested through swagger ui or 3d party apps like Postman. 