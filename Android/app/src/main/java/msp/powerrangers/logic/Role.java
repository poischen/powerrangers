package msp.powerrangers.logic;

import java.util.List;

public abstract class Role {

    public abstract String getId();

    public abstract void setId(String newid);

    public abstract String getName();

    public abstract void setName(String newName);

    public abstract List<Role> getRoles();

    public abstract void setRole(Role specificRole);

    public abstract boolean hasRole(Role specificRole);
}
