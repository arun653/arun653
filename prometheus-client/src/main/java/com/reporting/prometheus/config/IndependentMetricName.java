package com.reporting.prometheus.config;

import java.util.Arrays;
import java.util.function.ToDoubleFunction;

import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.BaseUnits;
import io.micrometer.core.instrument.binder.MeterBinder;

/*
 * This is a sample configuration class 
 * which enables to create custom metrics 
 * whithout depending on application requests 
 * eg. 
 * fetch count from a table
 * For more information : https://github.com/micrometer-metrics/micrometer/tree/e396f206b31e67f4fc2647d05fc75f830d72209e/micrometer-core
 * /src/main/java/io/micrometer/core/instrument/binder/db
 */

@Configuration
public class IndependentMetricName implements MeterBinder {

	private final String query = "Dummy Query";

	private final String dataSourceName = "Dummy DataSource Name";

	private final String dataSource = "Dummy DataSource";

	private String metricName = "dummy.testing.db.size";

	private final String tableName = "Dummy Table Name";

	private final Iterable<Tag> tags= Arrays.asList(Tag.of("DummyTags", "DummyTagsValue"));;

	ToDoubleFunction<String> totalRows = ds ->  (int) (100*Math.random());

	@Override
	public void bindTo(MeterRegistry registry) {
		// this is just for dummy setup to get dynamic values in each call for the metric name "dummy.testing.db.size"
		Gauge.builder(metricName, dataSource, totalRows).tags(tags).tag("db", dataSourceName)
		.tag("table", tableName).description("Number of rows in a database table")
		.baseUnit(BaseUnits.ROWS)
		.register(registry);
	}

}
