package com.jarbytes.lab.beans;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UserCredentials
{
    @NotNull
    private String username;

    @NotNull
    private String password;

    public UserCredentials()
    {
    }

    public UserCredentials(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof UserCredentials)) return false;
        UserCredentials that = (UserCredentials) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(username, password);
    }
}