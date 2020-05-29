package com.bsu.stoninois.api.interfaces;

import com.bsu.stoninois.api.inteceptors.JWTAuthenticatorInterceptor;
import com.bsu.stoninois.api.models.HealthDataBoardModel;
import com.bsu.stoninois.api.models.QuarterlyReportModel;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Delete;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Put;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

import static com.bsu.stoninois.api.interfaces.Constants.BaseURL;

@Rest(rootUrl = BaseURL, converters = { MappingJackson2HttpMessageConverter.class }, interceptors = {JWTAuthenticatorInterceptor.class})
public interface QuarterlyReportClient {

    @Get("http://{ip}:{port}/api/quarterlyreports/{criteria}/{year}/{quarter}")
    List<QuarterlyReportModel> getAll(@Path String ip, @Path String port,@Path String criteria, @Path int year, @Path int quarter);

    @Get("http://{ip}:{port}/api/quarterlyreport/{id}")
    QuarterlyReportModel get(@Path String ip, @Path String port,@Path int id);

    @Post("http://{ip}:{port}/api/quarterlyreport")
    void add(@Path String ip, @Path String port,@Body QuarterlyReportModel model);

    @Put("http://{ip}:{port}/api/quarterlyreport/{id}")
    void edit(@Path String ip, @Path String port,@Path Integer id, @Body QuarterlyReportModel model);

    @Delete("http://{ip}:{port}/api/quarterlyreport/{id}")
    void delete(@Path String ip, @Path String port,@Path Integer id);
}
