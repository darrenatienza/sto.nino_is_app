package com.bsu.stoninois.api.interfaces;

import android.graphics.Bitmap;
import android.media.Image;

import com.bsu.stoninois.api.inteceptors.JWTAuthenticatorInterceptor;
import com.bsu.stoninois.api.models.AccomplishmentModel;
import com.bsu.stoninois.api.models.AuthModel;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Delete;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Put;
import org.androidannotations.rest.spring.annotations.RequiresAuthentication;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

import static com.bsu.stoninois.api.interfaces.Constants.BaseURL;

@Rest(converters = { MappingJackson2HttpMessageConverter.class }, interceptors = {JWTAuthenticatorInterceptor.class})
public interface UserClient {


    @Get("/user/image/get")
    @RequiresAuthentication
    Bitmap getImage();

    @Get("/accomplishments")
    List<AccomplishmentModel> getAll();

    @Get("/accomplishments/{criteria}")
    List<AccomplishmentModel> getAllByCriteria(@Path String criteria);

    @Get("/accomplishment/{id}")
    AccomplishmentModel get(@Path int id);

    @Get("/accomplishments/id/{title}")
    Integer getID(@Path String title);

    @Post("/accomplishment")
    void add(@Body AccomplishmentModel model);

    @Put("/accomplishment/{id}")
    void edit(@Path int id, @Body AccomplishmentModel model);

    @Delete("/accomplishment/{id}")
    void delete(@Path Integer id);

    void setBearerAuth(String token);

    /** Returns token*/
    @Post("http://7.240.230.157/api/token/auth")
    String login(@Body AuthModel authModel);
}
