package com.reporting.prometheus.config;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

// import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class MicrometerConfiguration {
	
	@Bean @Lazy
	MeterRegistryCustomizer meterRegistryCustomizer(MeterRegistry meterRegistry)
	{
	//	org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration
		return meterRegistry1 -> {
			meterRegistry.config()
			.commonTags("application", "micormeter-youtube-example");
		};
	}

}
