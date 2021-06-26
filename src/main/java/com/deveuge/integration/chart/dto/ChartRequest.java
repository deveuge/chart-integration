package com.deveuge.integration.chart.dto;

import java.util.List;

import com.deveuge.integration.chart.dto.types.ChartDataset;
import com.deveuge.integration.chart.dto.types.ChartDefinition;
import com.deveuge.integration.chart.dto.types.ChartType;

public class ChartRequest {

	private ChartDefinition chart;
	private String format;
	
	public ChartRequest() {
		super();
	}
	
	public ChartRequest(String[] labels, List<ChartDataset> datasets, ChartType type) {
		super();
		this.chart = new ChartDefinition(type, labels, datasets);
		this.format = "png";
	}
	
	public ChartDefinition getChart() {
		return chart;
	}
	
	public void setChart(ChartDefinition chart) {
		this.chart = chart;
	}
	
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
}
