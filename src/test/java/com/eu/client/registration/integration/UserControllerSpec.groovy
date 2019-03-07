package com.eu.client.registration.integration

import com.eu.client.registration.service.client.ClientDto
import com.eu.client.registration.service.client.CountryDto
import com.github.tomakehurst.wiremock.client.WireMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import spock.lang.Shared

import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNAUTHORIZED
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class UserControllerSpec extends WireMockIntegrationSpec {

    @Shared
    static final String MOCK_CLIENT_NAME = 'John'

    @Shared
    static final String MOCK_CLIENT_SURNAME = 'Doe'

    @Shared
    static final String MOCK_COUNTRY_CODE = 'lv'

    @Shared
    static final String MOCK_CLIENT_EMAIL = 'john.doe@test.com'

    @Shared
    static final String MOCK_CLIENT_PASSWORD = '12345678'

    @Shared
    static final Long MOCK_COUNTRY_POPULATION = 1961600

    @Shared
    static final BigDecimal MOCK_COUNTRY_AREA = 64559

    List<String> MOCK_COUNTRY_BORDERS = List.of("BLR", "EST", "LTU", "RUS")


    @Autowired
    TestRestTemplate testRestTemplate

    void setup() {
        String regionResponse = getRequestResourceText('region.json', ['region': 'Europe'])
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/alpha/$MOCK_COUNTRY_CODE?fields=region"))
                .willReturn(WireMock.aResponse()
                .withHeader('Content-type', APPLICATION_JSON_VALUE)
                .withBody(regionResponse)))

        String lvCountryResponse = getRequestResourceText('country.json', ['population': MOCK_COUNTRY_POPULATION, 'area': MOCK_COUNTRY_AREA, 'borders':MOCK_COUNTRY_BORDERS])
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/alpha/$MOCK_COUNTRY_CODE?fields=population;area;borders"))
                .willReturn(WireMock.aResponse()
                .withHeader('Content-type', APPLICATION_JSON_VALUE)
                .withBody(lvCountryResponse)))


        String createClientRequest = getRequestResourceText('create-client-request.json',
                ['name': MOCK_CLIENT_NAME, 'surname': MOCK_CLIENT_SURNAME, 'countryCode': MOCK_COUNTRY_CODE, 'email': MOCK_CLIENT_EMAIL, 'password': MOCK_CLIENT_PASSWORD])

        HttpHeaders headers = new HttpHeaders()
        headers.set('Content-Type', APPLICATION_JSON_VALUE)
        HttpEntity<String> request = new HttpEntity<>(createClientRequest, headers)
        testRestTemplate.postForEntity("http://127.0.0.1:$serverPort/api/clients", request, Void.class)
    }

    void "should successfully get authorized user info"() {
        given:
            String auth = MOCK_CLIENT_EMAIL + ":" + MOCK_CLIENT_PASSWORD
            String encoding = Base64.getEncoder().encodeToString(auth.getBytes())
            HttpHeaders headers = new HttpHeaders()
            headers.set('Authorization', "Basic " + encoding)
            headers.set('Content-Type', APPLICATION_JSON_VALUE)
        when:
            ResponseEntity<ClientDto> response = testRestTemplate.exchange("http://127.0.0.1:$serverPort/api/user/info", HttpMethod.GET, new HttpEntity<>(headers), ClientDto.class)
        then:
            assert response.getStatusCode() == OK
            ClientDto clientDto = response.getBody()
            with(clientDto) {
                assert name == MOCK_CLIENT_NAME
                assert surname == MOCK_CLIENT_SURNAME
                assert country == MOCK_COUNTRY_CODE
                assert email == MOCK_CLIENT_EMAIL
            }
    }


    void "should fail to get authorized user info due to absent authorization header"() {
        given:
            HttpHeaders headers = new HttpHeaders()
            headers.set('Content-Type', APPLICATION_JSON_VALUE)
        when:
            ResponseEntity<ClientDto> response = testRestTemplate.exchange("http://127.0.0.1:$serverPort/api/user/info", HttpMethod.GET, new HttpEntity<>(headers), ClientDto.class)
        then:
            assert response.getStatusCode() == UNAUTHORIZED
    }


    void "should fail to get authorized user info due to wrong authorization header"() {
        given:
            String auth = MOCK_CLIENT_EMAIL + ":12345"
            String encoding = Base64.getEncoder().encodeToString(auth.getBytes())
            HttpHeaders headers = new HttpHeaders()
            headers.set('Authorization', "Basic " + encoding)
            headers.set('Content-Type', APPLICATION_JSON_VALUE)
        when:
            ResponseEntity<ClientDto> response = testRestTemplate.exchange("http://127.0.0.1:$serverPort/api/user/info", HttpMethod.GET, new HttpEntity<>(headers), ClientDto.class)
        then:
            assert response.getStatusCode() == UNAUTHORIZED
    }


    void "should successfully authenticate user and get country info captured by the user registration"() {
        given:
            String auth = MOCK_CLIENT_EMAIL + ":" + MOCK_CLIENT_PASSWORD
            String encoding = Base64.getEncoder().encodeToString(auth.getBytes())
            HttpHeaders headers = new HttpHeaders()
            headers.set('Authorization', "Basic " + encoding)
            headers.set('Content-Type', APPLICATION_JSON_VALUE)
        when:
            ResponseEntity<CountryDto> response = testRestTemplate.exchange("http://127.0.0.1:$serverPort/api/user/country", HttpMethod.GET, new HttpEntity<>(headers), CountryDto.class)
        then:
            assert response.getStatusCode() == OK
            CountryDto countryDto = response.getBody()
            with(countryDto) {
                assert population == MOCK_COUNTRY_POPULATION
                assert area == MOCK_COUNTRY_AREA
                assert borderingCountries == MOCK_COUNTRY_BORDERS
            }
    }

    void "should fail to get country info due to wrong authorization header"() {
        given:
            String auth = MOCK_CLIENT_EMAIL + ":12345"
            String encoding = Base64.getEncoder().encodeToString(auth.getBytes())
            HttpHeaders headers = new HttpHeaders()
            headers.set('Authorization', "Basic " + encoding)
            headers.set('Content-Type', APPLICATION_JSON_VALUE)
        when:
            ResponseEntity<ClientDto> response = testRestTemplate.exchange("http://127.0.0.1:$serverPort/api/user/country", HttpMethod.GET, new HttpEntity<>(headers), ClientDto.class)
        then:
           assert response.getStatusCode() == UNAUTHORIZED
    }



}
