package com.deveuge.integration.chart.dto.types;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ChartType {
	LINE,
	BAR,
	RADAR,
	DOUGHNUT,
	PIE;
	
	@JsonValue
	public String forJackson() {
	    return name().toLowerCase();
	}
}
