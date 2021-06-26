package com.deveuge.integration.chart.dto.types;

import java.util.List;

public class ChartData {

	private String[] labels;
	private List<ChartDataset> datasets;
	
	public ChartData() {
		super();
	}

	public ChartData(String[] labels, List<ChartDataset> datasets) {
		super();
		this.labels = labels;
		this.datasets = datasets;
	}

	public String[] getLabels() {
		return labels;
	}

	public void setLabels(String[] labels) {
		this.labels = labels;
	}
	
	public List<ChartDataset> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<ChartDataset> datasets) {
		this.datasets = datasets;
	}
}
