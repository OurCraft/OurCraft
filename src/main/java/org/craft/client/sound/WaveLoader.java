package org.craft.client.sound;

import java.io.*;

import javax.sound.sampled.*;

import org.lwjgl.openal.*;

public class WaveLoader extends SoundLoader
{

    @Override
    public SoundResource loadResource(byte[] data) throws IOException
    {
        try
        {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            AudioInputStream ain = AudioSystem.getAudioInputStream(in);
            int channels = ain.getFormat().getChannels();
            int format = 0;
            int sampleSize = ain.getFormat().getSampleSizeInBits();
            if(sampleSize == 8)
            {
                if(channels == 1)
                {
                    format = AL10.AL_FORMAT_MONO8;
                }
                else if(channels == 2)
                {
                    format = AL10.AL_FORMAT_STEREO8;
                }
                else
                    throw new RuntimeException("Unknown format: " + channels + " channels and sample size of " + sampleSize + " bits");
            }
            else if(sampleSize == 16)
            {
                if(channels == 1)
                {
                    format = AL10.AL_FORMAT_MONO16;
                }
                else if(channels == 2)
                {
                    format = AL10.AL_FORMAT_STEREO16;
                }
                else
                    throw new RuntimeException("Unknown format: " + channels + " channels and sample size of " + sampleSize + " bits");
            }
            else
                throw new RuntimeException("Unknown format: " + channels + " channels and sample size of " + sampleSize + " bits");
            SoundResource result = new SoundResource(data, format, (int) ain.getFormat().getSampleRate());
            ain.close();
            in.close();
            return result;
        }
        catch(Exception e)
        {
            throw new RuntimeException("Error while loading wave audio data", e);
        }
    }

}
