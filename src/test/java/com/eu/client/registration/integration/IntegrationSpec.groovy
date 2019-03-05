package com.eu.client.registration.integration

import com.eu.client.registration.RegistrationApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ActiveProfiles('test')
@SpringBootTest(classes = [RegistrationApplication], webEnvironment = RANDOM_PORT)
@TestPropertySource('/application-test.yml')
class IntegrationSpec extends Specification {

    @LocalServerPort
    int serverPort

    String getApiBasePath() {
        return "http://127.0.0.1:$serverPort/api"
    }

}
