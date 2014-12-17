package org.craft.spoonge;

import java.net.*;
import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;

import org.spongepowered.api.*;
import org.spongepowered.api.entity.player.*;
import org.spongepowered.api.text.message.*;
import org.spongepowered.api.text.message.Message.Text;
import org.spongepowered.api.world.*;

public class SpoongeServer implements Server
{

    private boolean    whiteListed;
    private boolean    onlineMode;
    private SpoongeMod spoonge;

    public SpoongeServer(SpoongeMod mod)
    {
        this.spoonge = mod;
    }

    @Override
    public Collection<Player> getOnlinePlayers()
    {
        return Lists.newArrayList();
    }

    @Override
    public int getMaxPlayers()
    {
        return 0;
    }

    @Override
    public Optional<Player> getPlayer(UUID uniqueId)
    {
        return Optional.absent();
    }

    @Override
    public Optional<Player> getPlayer(String name)
    {
        return Optional.absent();
    }

    @Override
    public Collection<World> getWorlds()
    {
        return Lists.newArrayList();
    }

    @Override
    public Optional<World> getWorld(UUID uniqueId)
    {
        return Optional.absent();
    }

    @Override
    public Optional<World> getWorld(String worldName)
    {
        return Optional.absent();
    }

    @Override
    public void broadcastMessage(Message message)
    {
        spoonge.getOurCraftInstance().broadcastMessage(message.getContent().toString());
    }

    @Override
    public Optional<InetSocketAddress> getBoundAddress()
    {
        // TODO Create a Server
        return null;
    }

    @Override
    public boolean hasWhitelist()
    {
        return whiteListed;
    }

    @Override
    public void setHasWhitelist(boolean enabled)
    {
        whiteListed = enabled;
    }

    @Override
    public boolean getOnlineMode()
    {
        return onlineMode;
    }

    @Override
    public Text getMOTD()
    {
        // TODO MotD
        return null;
    }

    @Override
    public int getRunningTimeTicks()
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
