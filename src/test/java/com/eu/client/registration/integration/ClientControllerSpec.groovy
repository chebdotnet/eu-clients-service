package com.eu.client.registration.integration

import com.eu.client.registration.domain.ClientRepository
import com.github.tomakehurst.wiremock.client.WireMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import spock.lang.Shared

import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class ClientControllerSpec extends WireMockIntegrationSpec {

    @Shared
    static final String MOCK_CLIENT_NAME = 'John'

    @Shared
    static final String MOCK_CLIENT_SURNAME = 'Doe'

    @Shared
    static final String MOCK_COUNTRY = 'lv'

    @Shared
    static final String MOCK_CLIENT_EMAIL = 'john.doe@test.com'

    @Shared
    static final String MOCK_CLIENT_PASSWORD = '12345678'

    @Shared
    static final Long MOCK_COUNTRY_POPULATION = 1961600

    @Shared
    static final BigDecimal MOCK_COUNTRY_AREA = 64559

    List MOCK_COUNTRY_BORDERS = List.of("BLR", "EST", "LTU", "RUS")

    @Autowired
    TestRestTemplate testRestTemplate

    @Autowired
    ClientRepository repository

    void "should successfully add the first client and do no add the second client"() {
        given:

            String regionResponse = getRequestResourceText('region.json', ['region': 'Europe'])
            WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/alpha/$MOCK_COUNTRY?fields=region"))
                    .willReturn(WireMock.aResponse()
                    .withHeader('Content-type', APPLICATION_JSON_VALUE)
                    .withBody(regionResponse)));

            String lvCountryResponse = getRequestResourceText('lv-country.json',  ['population': MOCK_COUNTRY_POPULATION, 'area': MOCK_COUNTRY_AREA, 'borders':MOCK_COUNTRY_BORDERS])
            WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/alpha/$MOCK_COUNTRY?fields=population;area;borders"))
                    .willReturn(WireMock.aResponse()
                    .withHeader('Content-type', APPLICATION_JSON_VALUE)
                    .withBody(lvCountryResponse)));

            String createClientRequest = getRequestResourceText('create-client-request.json',
                    ['name': MOCK_CLIENT_NAME, 'surname': MOCK_CLIENT_SURNAME, 'country': MOCK_COUNTRY, 'email': MOCK_CLIENT_EMAIL, 'password': MOCK_CLIENT_PASSWORD])

            HttpHeaders headers = new HttpHeaders()
            headers.set('Content-Type', APPLICATION_JSON_VALUE)
            HttpEntity<String> request = new HttpEntity<>(createClientRequest, headers)

        when:
            ResponseEntity<Object> response = testRestTemplate.postForEntity("http://127.0.0.1:$serverPort/api/clients", request, Void.class)

        then:
            assert response.getStatusCode() == CREATED
            assert repository.findByEmail(MOCK_CLIENT_EMAIL).get().email == MOCK_CLIENT_EMAIL

        and: "should no add client and get error because client with submitted email is already exists"
            ResponseEntity<Object> newResponse = testRestTemplate.postForEntity("http://127.0.0.1:$serverPort/api/clients", request, Void.class)

        then:
            assert newResponse.getStatusCode() == BAD_REQUEST
    }


    void "should no add client and get error because client is outside of Europe"() {
        given:
        String regionResponse = getRequestResourceText('region.json', ['region': 'Asia'])
            WireMock.stubFor(WireMock.get(WireMock.urlEqualTo('/alpha/jp?fields=region'))
                    .willReturn(WireMock.aResponse()
                    .withHeader('Content-type', APPLICATION_JSON_VALUE)
                    .withBody(regionResponse)));


            String createClientRequest = getRequestResourceText('create-client-request.json',
                        ['name': MOCK_CLIENT_NAME, 'surname': MOCK_CLIENT_SURNAME, 'country': 'jp', 'email': MOCK_CLIENT_EMAIL, 'password': MOCK_CLIENT_PASSWORD])

            HttpHeaders headers = new HttpHeaders()
            headers.set('Content-Type', APPLICATION_JSON_VALUE)
            HttpEntity<String> request = new HttpEntity<>(createClientRequest, headers)

        when:
            ResponseEntity<Object> response = testRestTemplate.postForEntity("http://127.0.0.1:$serverPort/api/clients", request, Void.class)

        then:
            assert response.getStatusCode() == BAD_REQUEST
    }

}
