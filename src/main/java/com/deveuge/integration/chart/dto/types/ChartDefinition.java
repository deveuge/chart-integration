package com.deveuge.integration.chart.dto.types;

import java.util.List;

public class ChartDefinition {

	private ChartType type;
	private ChartData data;
	
	public ChartDefinition() {
		super();
	}
	
	public ChartDefinition(ChartType type, String[] labels, List<ChartDataset> datasets) {
		super();
		this.type = type;
		this.data = new ChartData(labels, datasets);
	}
	
	public ChartType getType() {
		return type;
	}
	
	public void setType(ChartType type) {
		this.type = type;
	}
	
	public ChartData getData() {
		return data;
	}
	
	public void setData(ChartData data) {
		this.data = data;
	}

}
