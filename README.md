# eu-clients-service
Demo service to register EU clients
Provides integration with 3d party 'restcountries' service
    
Used technologies : Java 11, Spring, Lombok, Swagger, JGraphT, H2, Spock, Wiremock

Please use the next links

http://localhost:8080/h2-console/ -- to work with in-memory db

http://localhost:8080/swagger-ui.html -- to review and test implemented api documentation

Please note user data is only be accessible to user who submitted it. 
Please use basic authentication to work with user endpoint, 
submitted user's email must be used for name in auth form
submitted user's password must be used for password in auth form

The app can be tested through swagger ui or 3d party apps like Postman. 


Client registration endpoint
http://localhost:8080/api/clients

User basic info endpoint
http://localhost:8080/api/user/info

Data about users country captured at registration endpoint 
http://localhost:8080/api/user/counrty 

Search the shortest way between countries
http://localhost:8080/api/country?startCountry={$startCountry}&finishCountry={$finishCountry}
For countries codes must be used alpha3Code code like LVA, POL, PRT etc


Below is provided technical requirements for the task.

Create a REST services that would support the following functionality:

·         Client registration

o   user submits: name, surname, country, email, password

o   user registration is denied if country is outside of EU (integrate with https://restcountries.eu/ to determine country location)

·         User basic info

o   expose basic info submitted at registration

o   this data should only be accessible to user who submitted it

·         Detailed data about users country captured from https://restcountries.eu/ at the time of registration

o   service should return population, area and bordering countries

o   this data should only be accessible to user who submitted it

 

Optional bonus task:

                Create an endpoint which accepts 2 country codes and return any shortest path from one country to another based on their boarders with other countries

for example, LV and POL returns [LV, LTU, POL]

 

What will be evaluated:

ü  Functionality according to the requirements

ü  Code quality

ü  Test quality

ü  Ease of deployment

ü  Helper scripts - build, deployment, ...

ü  Input data validation

Technologies:
Java + Spring
Testing framework, DB and everything else is a personal choice