package com.demo.tutorial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class ExampleComponentTest {

	@Autowired
	private MockMvc mvc;

	@RegisterExtension
	static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
	.options(wireMockConfig().port(9092))
	.build();

	@Test
	public void testDemo() throws Exception
	{
		ResultActions result = mvc.perform(get("/demo"));
		result.andExpect(status().isOk());

	}

	@Test
	public void testApi() throws Exception
	{
		wireMockExtension.givenThat(WireMock.get(anyUrl())
				.willReturn(aResponse().withStatus(200).withBody("Mocked response against third party API.")));

		ResultActions result = mvc.perform(get("/api"));
		result.andExpect(status().isOk());

	}
}
