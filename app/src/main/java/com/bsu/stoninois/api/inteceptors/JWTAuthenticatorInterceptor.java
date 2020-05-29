package com.bsu.stoninois.api.inteceptors;

import android.util.Log;

import com.bsu.stoninois.api.interfaces.AuthClient;


import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class JWTAuthenticatorInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution execution) throws IOException {

        // do something
        Log.d("Request", request.getURI().getPath());

        return execution.execute(request, data);
    }
}
