package com.eu.client.registration.integration

import com.github.tomakehurst.wiremock.junit.WireMockRule
import groovy.text.SimpleTemplateEngine
import org.junit.Rule
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.util.SocketUtils

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

@ContextConfiguration(initializers = WireMockInitializer.class)
class WireMockIntegrationSpec extends IntegrationSpec {

    static final int WIRE_MOCK_PORT = SocketUtils.findAvailableTcpPort()

    SimpleTemplateEngine templateEngine = new SimpleTemplateEngine()

    @Rule
    WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(WIRE_MOCK_PORT), false)

    String getResourceText(String resourcePath, Map<String, ?> placeholders = [:]) {
        String resourceText = getClass().getResource(resourcePath).getText('UTF-8')
        if (!placeholders.isEmpty()) {
            resourceText = templateEngine.createTemplate(resourceText)
                    .make(placeholders)
                    .toString()
        }
        return resourceText
    }

    String getRequestResourceText(String resourceName, Map<String, ?> placeholders = [:]) {
        getResourceText("/requests/$resourceName", placeholders)
    }

    static class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "wiremockPort=$WIRE_MOCK_PORT"
            )
            values.applyTo(configurableApplicationContext)
        }
    }

}
