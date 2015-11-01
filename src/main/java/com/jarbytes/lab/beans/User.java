package com.jarbytes.lab.beans;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class User
{
    private final String username;

    private final Set<Privilege> privileges;

    public User(final String username,
                final Set<Privilege> privileges)
    {
        this.username = username;
        this.privileges = Collections.unmodifiableSet(privileges);
    }

    public String getUsername()
    {
        return username;
    }

    public Set<Privilege> getPrivileges()
    {
        return privileges;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(privileges, user.privileges);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(username, privileges);
    }
}