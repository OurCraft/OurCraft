package org.craft.sound;

import java.net.*;
import java.util.*;

import com.google.common.collect.*;

import org.craft.*;
import org.craft.resources.*;
import org.craft.utils.*;

public class AudioRegistry
{

    public static HashMap<String, List<AudioInfo>> audioFiles = Maps.newHashMap();

    public static void init()
    {
        register(new ResourceLocation("ourcraft", "musics/BlitZEcho-Hanging_On.mp3"));
    }

    public static List<AudioInfo> get(String id)
    {
        return audioFiles.get(id);
    }

    public static void register(String id)
    {
        register(id, new AudioInfo(id, EnumSoundFormats.UNKNOWN, null));
    }

    public static void register(ResourceLocation loc)
    {
        register(loc.getName().replace("." + loc.getExtension(), ""), loc);
    }

    public static void registerSubs(String id, ResourceLocation loc, EnumSoundFormats format)
    {
        AssetLoader loader = CommonHandler.getCurrentInstance().getAssetsLoader();
        for(int index = 1;; index++ )
        {
            ResourceLocation subSoundLoc = new ResourceLocation(loc.getFullPath() + index + "." + format.id());
            if(loader.doesResourceExists(subSoundLoc))
            {
                register(id, subSoundLoc);
            }
            else
            {
                Log.message("Audio file " + subSoundLoc.getFullPath() + " doesn't exist, stopping to search for sub sounds of " + loc.getFullPath());
                break;
            }
        }
    }

    public static void register(String id, ResourceLocation loc)
    {
        AssetLoader loader = CommonHandler.getCurrentInstance().getAssetsLoader();
        try
        {
            URL url = loader.getResource(loc).getURL();
            EnumSoundFormats format = EnumSoundFormats.fromID(loc.getExtension());
            AudioInfo info = new AudioInfo(id, format, url);
            register(id, info);
        }
        catch(Exception e)
        {
            Log.error("Error while registering sound", e);
        }
    }

    public static void register(String id, AudioInfo sound)
    {
        List<AudioInfo> infos = audioFiles.get(id);
        if(infos == null)
        {
            infos = Lists.newArrayList();
        }
        infos.add(sound);
        if(Dev.debug())
        {
            Log.message("Registred audio file " + sound.toString());
        }
        audioFiles.put(id, infos);
    }

    public static Collection<List<AudioInfo>> getAllInfos()
    {
        return audioFiles.values();
    }

    public static AudioInfo getFirst(String id)
    {
        List<AudioInfo> infos = get(id);
        if(infos != null)
            return infos.get(0);
        return null;
    }

    public static AudioInfo getRandom(String id)
    {
        List<AudioInfo> infos = get(id);
        if(infos != null)
            return infos.get((int) (infos.size() * Math.random()));
        else
            return null;
    }
}
