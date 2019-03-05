package com.eu.client.registration.integration

import groovy.text.SimpleTemplateEngine
import org.junit.Rule


class WireMockIntegrationSpec extends IntegrationSpec {

    SimpleTemplateEngine templateEngine = new SimpleTemplateEngine()

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

}
