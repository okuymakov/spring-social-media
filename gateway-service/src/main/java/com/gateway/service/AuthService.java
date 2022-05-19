package com.gateway.service;


import com.gateway.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", configuration = FeignConfig.class)
public interface AuthService {

    @GetMapping(value = "/auth/authenticate")
    public String getJWTToken(@RequestHeader("access_token") String accessToken);

}
