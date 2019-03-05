package com.eu.client.registration.integration

import com.eu.client.registration.service.client.ClientDto
import com.eu.client.registration.service.country.ShortestPathDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Shared

import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNAUTHORIZED

class UserControllerSpec extends WireMockIntegrationSpec {

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

    @Autowired
    TestRestTemplate testRestTemplate

    void setup() {

        String createClientRequest = getRequestResourceText('create-client-request.json',
                ['name': MOCK_CLIENT_NAME, 'surname': MOCK_CLIENT_SURNAME, 'country': MOCK_COUNTRY, 'email': MOCK_CLIENT_EMAIL, 'password': MOCK_CLIENT_PASSWORD])

        HttpHeaders headers = new HttpHeaders()
        headers.set('Content-Type', 'application/json')
        HttpEntity<String> request = new HttpEntity<>(createClientRequest, headers)
        testRestTemplate.postForEntity("http://127.0.0.1:$serverPort/api/clients", request, Void.class)
    }

    void "should successfully get authorized user info"() {
        given:
            String auth = MOCK_CLIENT_EMAIL + ":" + MOCK_CLIENT_PASSWORD
            String encoding = Base64.getEncoder().encodeToString(auth.getBytes())
            HttpHeaders headers = new HttpHeaders()
            headers.set('Authorization', "Basic " + encoding)
            headers.set('Content-Type', 'application/json')
        when:
            ResponseEntity<ClientDto> response = testRestTemplate.exchange("http://127.0.0.1:$serverPort/api/user/info", HttpMethod.GET, new HttpEntity<>(headers), ClientDto.class)
        then:
            assert response.getStatusCode() == OK
            ClientDto clientDto = response.getBody()
            with(clientDto) {
                assert name == MOCK_CLIENT_NAME
                assert surname == MOCK_CLIENT_SURNAME
                assert country == MOCK_COUNTRY
                assert email == MOCK_CLIENT_EMAIL
            }
    }


    void "should fail to get authorized user info due to absent authorization header"() {
        given:
            HttpHeaders headers = new HttpHeaders()
            headers.set('Content-Type', 'application/json')
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
            headers.set('Content-Type', 'application/json')
        when:
            ResponseEntity<ClientDto> response = testRestTemplate.exchange("http://127.0.0.1:$serverPort/api/user/info", HttpMethod.GET, new HttpEntity<>(headers), ClientDto.class)
        then:
            assert response.getStatusCode() == UNAUTHORIZED
    }

}
