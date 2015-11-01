package com.jarbytes.lab.resources;

import com.jarbytes.lab.beans.Privilege;
import com.jarbytes.lab.beans.User;
import com.jarbytes.lab.beans.UserCredentials;
import jwt4j.JWTHandler;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource
{
    private static final Map<UserCredentials, User> USERS = Stream.of(
            new SimpleEntry<>(new UserCredentials("user", "123"),
                    new User("just-user", new HashSet<>(Arrays.asList(Privilege.USER)))),
            new SimpleEntry<>(new UserCredentials("moderator", "1234"),
                    new User("moderator", new HashSet<>(Arrays.asList(Privilege.USER, Privilege.MODERATOR)))),
            new SimpleEntry<>(new UserCredentials("administrator", "12345"),
                    new User("administrator", new HashSet<>(Arrays.asList(Privilege.values()))))
    ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

    private final JWTHandler<User> jwtHandler;

    public AuthResource(final JWTHandler<User> jwtHandler)
    {
        this.jwtHandler = jwtHandler;
    }

    @POST
    @Path("/login")
    public String login(@Valid UserCredentials userCredentials)
    {
        if (USERS.containsKey(userCredentials)) {
            return jwtHandler.encode(USERS.get(userCredentials));
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
}