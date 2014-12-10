package org.craft.sound;

import java.net.*;

public class SoundInfo
{

    private String id;
    private String fileId;
    private URL    url;

    public SoundInfo(String id, EnumSoundFormats format, URL url)
    {
        this.id = id;
        this.fileId = format.id();
        this.url = url;
    }

    public URL getURL()
    {
        return url;
    }

    public String getID()
    {
        return id;
    }

    public String getFileIdentifier()
    {
        return fileId;
    }

    public String toString()
    {
        return "{" + "\"id\":" + id + ", \"fileIdentifier\":" + fileId + ", \"url\":" + url + "}";
    }
}
