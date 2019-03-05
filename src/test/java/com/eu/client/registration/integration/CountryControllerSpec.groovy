package com.eu.client.registration.integration

import com.eu.client.registration.service.country.ShortestPathDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

class CountryControllerSpec extends WireMockIntegrationSpec {

    @Autowired
    TestRestTemplate testRestTemplate

    void "should successfully find the shortest way between countries"() {
        when:
            ResponseEntity<ShortestPathDto> response = testRestTemplate.getForEntity("http://127.0.0.1:$serverPort/api/country?startCountry=LVA&finishCountry=PRT", ShortestPathDto.class)
        then:
            assert response.getStatusCode() == OK
            assert response.getBody().path == "[(LVA : BLR), (BLR : POL), (POL : DEU), (DEU : FRA), (FRA : ESP), (ESP : PRT)]"
    }

    void "should no find a way between countries due to no borders between some countries"() {
        when:
            ResponseEntity<ShortestPathDto> response = testRestTemplate.getForEntity("http://127.0.0.1:$serverPort/api/country?startCountry=LVA&finishCountry=JPA", ShortestPathDto.class)
        then:
            assert response.getStatusCode() == NOT_FOUND
    }

    void "should no find the shortest way between countries due to wrong input parameters"() {
        when:
            ResponseEntity<ShortestPathDto> response = testRestTemplate.getForEntity("http://127.0.0.1:$serverPort/api/country?startCountry=AAA&finishCountry=LVA", ShortestPathDto.class)
        then:
            assert response.getStatusCode() == BAD_REQUEST
    }


}
