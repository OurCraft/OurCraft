package org.craft.utils.crash;

import org.craft.client.*;

public class OpenGLInfos implements CrashInfos
{

    @Override
    public String getInfos()
    {
        String header = SECTION_START + " OpenGL " + SECTION_END;
        return header + "\n\tVersion: " + OpenGLHelper.getOpenGLVersion() + "\n\tVendor: " + OpenGLHelper.getOpenGLVendor();
    }

}
