package com.jarbytes.lab.auth;

import com.jarbytes.lab.auth.annotation.AuthRequired;
import com.jarbytes.lab.beans.Privilege;
import com.jarbytes.lab.beans.User;
import com.jarbytes.lab.configuration.JwtApplicationConfiguration;
import jwt4j.JWTHandler;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

public class AuthDynamicFeature implements DynamicFeature
{
    private final JwtApplicationConfiguration configuration;

    private final JWTHandler<User> jwtHandler;

    public AuthDynamicFeature(final JwtApplicationConfiguration configuration,
                              final JWTHandler<User> jwtHandler)
    {
        this.configuration = configuration;
        this.jwtHandler = jwtHandler;
    }

    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext)
    {
        final Method resourceMethod = resourceInfo.getResourceMethod();
        if (resourceMethod != null) {
            Stream.of(resourceMethod.getParameterAnnotations())
                    .flatMap(Arrays::stream)
                    .filter(annotation -> annotation.annotationType().equals(AuthRequired.class))
                    .map(AuthRequired.class::cast)
                    .findFirst()
                    .ifPresent(authRequired -> featureContext.register(getAuthFilter(authRequired.value())));
        }
    }

    private ContainerRequestFilter getAuthFilter(final Privilege[] requiredPrivileges)
    {
        return containerRequestContext -> {
            final String authHeader = containerRequestContext.getHeaderString(configuration.getAuthHeader());
            if (authHeader == null) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
            final User user;
            try {
                user = jwtHandler.decode(authHeader);
            } catch (Exception e) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
            if (!user.getPrivileges().containsAll(Arrays.asList(requiredPrivileges))) {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }
            containerRequestContext.setProperty("user", user);
        };
    }
}