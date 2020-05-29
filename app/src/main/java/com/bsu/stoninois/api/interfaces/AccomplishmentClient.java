package com.bsu.stoninois.api.interfaces;

import com.bsu.stoninois.api.inteceptors.JWTAuthenticatorInterceptor;
import com.bsu.stoninois.api.models.AccomplishmentModel;
import com.bsu.stoninois.api.models.CommonHealthProfileModel;
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
public interface AccomplishmentClient {


    @Get("http://{ip}:{port}/api/accomplishments")
    List<AccomplishmentModel> getAll(@Path String ip, @Path String port);

    @Get("http://{ip}:{port}/api/accomplishments/{criteria}")
    List<AccomplishmentModel> getAllByCriteria(@Path String ip, @Path String port,@Path String criteria);

    @Get("http://{ip}:{port}/api/accomplishment/{id}")
    AccomplishmentModel get(@Path String ip, @Path String port,@Path int id);

    @Get("http://{ip}:{port}/api/accomplishments/id/{title}")
    Integer getID(@Path String ip, @Path String port,@Path String title);

    @Post("http://{ip}:{port}/api/accomplishment")
    void add(@Path String ip, @Path String port,@Body AccomplishmentModel model);

    @Put("http://{ip}:{port}/api/accomplishment/{id}")
    void edit(@Path String ip, @Path String port,@Path int id, @Body AccomplishmentModel model);

    @Delete("http://{ip}:{port}/api/accomplishment/{id}")
    void delete(@Path String ip, @Path String port,@Path Integer id);
}
