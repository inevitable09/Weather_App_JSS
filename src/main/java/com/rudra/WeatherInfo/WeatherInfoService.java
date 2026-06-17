package com.rudra.WeatherInfo;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@Service
public class WeatherInfoService {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	WebClient webClient;

	public Mono<ObjectNode> gct(ObjectNode input) {
		System.out.println("Thread: " + Thread.currentThread().getName());

		String city = input.get("city").asText();

		String a1 = "https://api.openweathermap.org/data/2.5/weather";
		String a2 = "?q=" + city;
		String a3 = "&appid=0e5ddbe3e92c3ef32ce98c1da4382e7e";
		String a4 = "&units=metric";
		String url = a1 + a2 + a3 + a4;

		return webClient.get()
			.uri(url)
			.retrieve()
			.bodyToMono(String.class)
			.map(response -> {
				try {
					JsonNode jsonData = objectMapper.readTree(response);

					ObjectNode data = objectMapper.createObjectNode();

					String weather = jsonData.path("weather").get(0).path("main").asText("NA");
					double temp = jsonData.path("main").path("temp").asDouble(0);
					double humidity = jsonData.path("main").path("humidity").asDouble(0);
					data.put("status", "success");
					data.put("temp", temp);
					data.put("weather", weather);
					data.put("humidity", humidity);
					return data;
				} catch(Exception e) {
					ObjectNode err = objectMapper.createObjectNode();
					err.put("status", "failed");
					err.put("errmsg", e.getMessage() + 1);
					return err;
				}
			})
			.onErrorResume(e -> {
				ObjectNode err = objectMapper.createObjectNode();
				err.put("status", "failed");
				err.put("errmsg", e.getMessage() + 2);
				return Mono.just(err);
			});
	}

}
