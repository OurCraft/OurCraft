package org.craft.client.sound;

import java.io.*;

public abstract class SoundLoader
{

    public abstract SoundResource loadResource(byte[] data) throws IOException;
}
