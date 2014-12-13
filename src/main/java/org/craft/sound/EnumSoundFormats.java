package org.craft.sound;

public enum EnumSoundFormats
{

    VORBIS("ogg"), WAV("wav"), MP3("mp3"), UNKNOWN(null);

    private String id;

    private EnumSoundFormats(String id)
    {
        this.id = id;
    }

    public String id()
    {
        return id;
    }

    public static EnumSoundFormats fromID(String id)
    {
        for(EnumSoundFormats format : values())
        {
            if(format.id().equals(id))
                return format;
        }
        return UNKNOWN;
    }
}
