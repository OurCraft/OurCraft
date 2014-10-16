package org.craft.utils;

import java.util.*;

public final class SessionManager
{

    private static SessionManager  instance;
    private HashMap<UUID, Session> sessions;

    private SessionManager()
    {
        sessions = new HashMap<UUID, Session>();
    }

    public Session registerPlayer(UUID uuid, String id, String displayName)
    {
        Session session = new Session(uuid, id, displayName);
        sessions.put(uuid, session);
        return session;
    }

    public void registerSession(Session session)
    {
        sessions.put(session.getUUID(), session);
    }

    public static SessionManager getInstance()
    {
        if(instance == null)
            instance = new SessionManager();
        return instance;
    }

    public String getName(UUID uuid)
    {
        if(!sessions.containsKey(uuid))
            return "Unknown";
        return sessions.get(uuid).getId();
    }

    public String getDisplayName(UUID uuid)
    {
        if(!sessions.containsKey(uuid))
            return "Unknown";
        return sessions.get(uuid).getDisplayName();
    }
}
