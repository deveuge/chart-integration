package com.deveuge.integration.chart.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.deveuge.integration.chart.dto.ChartRequest;

public interface IQuickChartService {
	
	@POST
    @Path("/chart")
	@Consumes(MediaType.APPLICATION_JSON)
    public Response generateChart(final ChartRequest request);

}
