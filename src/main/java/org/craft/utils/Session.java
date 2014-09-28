package org.craft.utils;

import java.util.*;

public class Session
{

    private UUID   uuid;
    private String id;
    private String displayName;

    public Session(UUID uuid, String id, String displayName)
    {
        this.uuid = uuid;
        this.id = id;
        this.displayName = displayName;
    }

    public String getId()
    {
        return id;
    }

    public UUID getUUID()
    {
        return uuid;
    }

    public String getDisplayName()
    {
        return displayName;
    }
}
