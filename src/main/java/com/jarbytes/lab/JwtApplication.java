package com.jarbytes.lab;

import com.jarbytes.lab.auth.AuthDynamicFeature;
import com.jarbytes.lab.auth.AuthValueFactoryProvider;
import com.jarbytes.lab.beans.User;
import com.jarbytes.lab.configuration.JwtApplicationConfiguration;
import com.jarbytes.lab.resources.AuthResource;
import com.jarbytes.lab.resources.SecuredResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import jwt4j.JWTHandler;
import jwt4j.JWTHandlerBuilder;

import java.util.stream.Stream;

public class JwtApplication extends Application<JwtApplicationConfiguration>
{
    @Override
    public void initialize(Bootstrap<JwtApplicationConfiguration> bootstrap)
    {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
    }

    @Override
    public void run(final JwtApplicationConfiguration jwtApplicationConfiguration,
                    final Environment environment) throws Exception
    {
        final JWTHandler<User> jwtHandler = getJwtHandler(jwtApplicationConfiguration);
        final JerseyEnvironment jerseyEnvironment = environment.jersey();

        Stream.of(new AuthResource(jwtHandler),
                new SecuredResource(),
                new AuthDynamicFeature(jwtApplicationConfiguration, jwtHandler),
                new AuthValueFactoryProvider.Binder()).forEach(jerseyEnvironment::register);
    }

    private JWTHandler<User> getJwtHandler(JwtApplicationConfiguration jwtApplicationConfiguration)
    {
        return new JWTHandlerBuilder<User>()
                .withSecret(jwtApplicationConfiguration.getAuthSalt().getBytes())
                .withDataClass(User.class)
                .build();
    }

    public static void main(String[] args) throws Exception
    {
        new JwtApplication().run(args);
    }
}