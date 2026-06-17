package com.rudra.WeatherInfo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;

import tools.jackson.databind.node.ObjectNode;

import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins="*")
public class WeatherInfoController {
	@Autowired
	WeatherInfoService service;

	@PostMapping("/gct")
	public Mono<ObjectNode> gct(@RequestBody ObjectNode input) {
		return service.gct(input);
	}

}
