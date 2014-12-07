package org.craft.client.sound;

import java.nio.*;

import org.craft.utils.*;
import org.craft.utils.io.IOUtils;
import org.lwjgl.openal.*;

public class SoundResource implements IDisposable
{

    private int referenceCounter;
    private int bufferPointer;

    public SoundResource(byte[] bufferData, int format, int freq)
    {
        this(IOUtils.createFlippedByteBuffer(bufferData), format, freq);
    }

    public SoundResource(ByteBuffer bufferData, int format, int freq)
    {
        bufferPointer = AL10.alGenBuffers();
        if(AL10.alGetError() != AL10.AL_NO_ERROR)
            throw new RuntimeException("Error while creating sound buffer");
        AL10.alBufferData(bufferPointer, format, bufferData, freq);
    }

    public int getBufferPointer()
    {
        return bufferPointer;
    }

    public boolean decreaseCounter()
    {
        referenceCounter-- ;
        return referenceCounter <= 0;
    }

    public void increaseCounter()
    {
        referenceCounter++ ;
    }

    @Override
    protected void finalize()
    {
        dispose();
    }

    @Override
    public void dispose()
    {

    }

    public int createSourceID()
    {
        int id = AL10.alGenSources();
        AL10.alSourcei(id, AL10.AL_BUFFER, bufferPointer);
        AL10.alSourcef(id, AL10.AL_PITCH, 1.0f);
        AL10.alSourcef(id, AL10.AL_GAIN, 1.0f);
        return id;
    }
}
