package com.reporting.prometheus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.annotation.Timed;

/*import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Summary;
*/
@RestController
@RequestMapping("/rest")
public class HelloController {

	// reference link - https://github.com/prometheus/client_java#counter
	/*
	 * static final Counter requests = Counter.build()
	 * .name("requests_total").help("Total requests.").register(); static final
	 * Gauge inprogressRequests = Gauge.build()
	 * .name("inprogress_requests").help("Inprogress requests.").register(); static
	 * final Summary receivedBytes = Summary.build()
	 * .name("requests_size_bytes").help("Request size in bytes.").register();
	 * static final Summary requestLatency = Summary.build()
	 * .name("requests_latency_seconds").help("Request latency in seconds.").
	 * register();
	 */
	@Timed(
			value = "prometheus.hello.request",
			histogram = true,
			percentiles = {0.95, 0.99},
			extraTags = {"version", "1.0"}
			)
	@GetMapping("/hello")
	public String hello()
	{
		/*
		 * Summary.Timer requestTimer = requestLatency.startTimer();
		 * inprogressRequests.inc(); requests.inc();
		 */
		System.out.println("In HelloController.isAppUp() !");
		/*
		 * inprogressRequests.dec();
		 * 
		 * requestTimer.observeDuration();
		 */
		return "Hello World !!";
	}

	@Timed(
			value = "prometheus.hello2.request",
			histogram = true,
			percentiles = {0.95, 0.99},
			extraTags = {"version", "1.0"}
			)
	@GetMapping("/hello2")
	public String hello2()
	{
		/*
		 * Summary.Timer requestTimer = requestLatency.startTimer();
		 * inprogressRequests.inc(); requests.inc();
		 */
		System.out.println("In HelloController.isAppUp() !");
		/*
		 * inprogressRequests.dec();
		 * 
		 * requestTimer.observeDuration();
		 */
		return "Hello 2 !!";
	}
	
}
