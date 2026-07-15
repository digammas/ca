package co.digamma.ca

import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.client.MockMvcWebTestClient

interface ControllerTestBase {

    var mockMvc: MockMvc

    val client
        get(): WebTestClient =
            MockMvcWebTestClient.bindTo(mockMvc)
                .baseUrl("/graphql")
                .build()

    val tester
        get(): HttpGraphQlTester =
            HttpGraphQlTester.create(client)
}