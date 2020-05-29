package com.bsu.stoninois.api.interfaces;

import com.bsu.stoninois.api.inteceptors.JWTAuthenticatorInterceptor;
import com.bsu.stoninois.api.models.AuthModel;
import com.bsu.stoninois.api.models.HealthDataBoardModel;

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

@Rest(converters = { MappingJackson2HttpMessageConverter.class }, interceptors = {JWTAuthenticatorInterceptor.class})
public interface HealthDataBoardClient {

    @Get("http://{ip}:{port}/api/healthdataboards/{criteria}/{year}")
    List<HealthDataBoardModel> getHealthDataBoards(@Path String ip, @Path String port,@Path String criteria, @Path int year);

    @Get("http://{ip}:{port}/api/healthdataboard/{id}")
    HealthDataBoardModel getHealthDataBoard(@Path String ip, @Path String port,@Path int id);

    @Post("http://{ip}:{port}/api/healthdataboard")
    void add(@Path String ip, @Path String port,@Body HealthDataBoardModel model);

    @Put("http://{ip}:{port}/api/healthdataboard/{id}")
    void edit(@Path String ip, @Path String port,@Path Integer id, @Body HealthDataBoardModel model);

    @Delete("http://{ip}:{port}/api/healthdataboard/{id}")
    void delete(@Path String ip, @Path String port,@Path Integer id);
}
