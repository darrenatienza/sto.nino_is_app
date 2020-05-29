package com.bsu.stoninois.api.interfaces;

import com.bsu.stoninois.api.inteceptors.JWTAuthenticatorInterceptor;
import com.bsu.stoninois.api.models.AuthModel;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import static com.bsu.stoninois.api.interfaces.Constants.BaseURL;


@Rest(converters = { MappingJackson2HttpMessageConverter.class }, interceptors = {JWTAuthenticatorInterceptor.class})
public interface AuthClient {
    @Get("/test")
    String getTest();

    @Post("http://{ip}:{port}/api/token/auth")
    String login(@Path String ip, @Path String port, @Body AuthModel model);

}
