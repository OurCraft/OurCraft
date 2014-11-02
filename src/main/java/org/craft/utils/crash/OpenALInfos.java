package org.craft.utils.crash;

import org.lwjgl.openal.*;

public class OpenALInfos implements CrashInfos
{

    @Override
    public String getInfos()
    {
        String s = SECTION_START + " OpenAL " + SECTION_END;
        s += "\n\tVersion: " + AL10.alGetString(AL10.AL_VERSION);
        s += "\n\tVendor: " + AL10.alGetString(AL10.AL_VENDOR);
        return s;
    }

}
