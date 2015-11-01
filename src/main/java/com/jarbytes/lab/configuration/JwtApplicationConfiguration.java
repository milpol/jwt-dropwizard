package com.jarbytes.lab.configuration;

import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

public class JwtApplicationConfiguration extends Configuration
{
    @NotNull
    private String authHeader;

    @NotNull
    private String authSalt;

    public String getAuthHeader()
    {
        return authHeader;
    }

    public void setAuthHeader(String authHeader)
    {
        this.authHeader = authHeader;
    }

    public String getAuthSalt()
    {
        return authSalt;
    }

    public void setAuthSalt(String authSalt)
    {
        this.authSalt = authSalt;
    }
}