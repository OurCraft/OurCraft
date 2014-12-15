package org.craft.world;

import java.io.*;
import java.util.*;

import com.google.common.collect.*;
import com.mojang.nbt.*;

import org.craft.*;
import org.craft.blocks.*;
import org.craft.blocks.states.*;
import org.craft.entity.*;
import org.craft.maths.*;
import org.craft.modding.events.block.*;
import org.craft.sound.*;
import org.craft.utils.*;
import org.craft.utils.CollisionInfos.CollisionType;

public class World implements IParticleHandler, IAudioHandler
{

    public class BlockUpdateScheduler
    {
        private int  x;
        private int  y;
        private int  z;
        private long interval;
        private long cooldown;

        public BlockUpdateScheduler(int x, int y, int z, long interval)
        {
            this.x = x;
            this.y = y;
            this.z = z;
            this.interval = interval;
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }

        public int getZ()
        {
            return z;
        }

        public long getInterval()
        {
            return interval;
        }

        public long getCooldown()
        {
            return cooldown;
        }

        public void tick()
        {
            cooldown-- ;
            if(cooldown < 0)
            {
                cooldown = interval;
                onScheduledBlockUpdate(x, y, z, interval);
            }
        }
    }

    private LinkedList<Entity>            entities;
    private List<Entity>                  spawingQueue;
    private ChunkProvider                 chunkProvider;
    private WorldGenerator                generator;
    private String                        name;
    private WorldLoader                   worldLoader;
    public boolean                        isRemote;
    private Random                        rng;
    private long                          tick;
    private float                         gravity;
    private List<BlockUpdateScheduler>    schedulers;
    private IParticleHandler              delegateParticleHandler;
    private IAudioHandler                 delegateSoundProducer;
    private HashMap<UUID, NBTCompoundTag> playerData;

    public World(String name, ChunkProvider prov, WorldGenerator generator, WorldLoader worldLoader)
    {
        schedulers = Lists.newArrayList();
        this.rng = new Random(generator.getSeed());
        this.worldLoader = worldLoader;
        if(worldLoader != null) // Load all player data
        {
            playerData = Maps.newHashMap();
            try
            {
                NBTListTag<NBTCompoundTag> players = worldLoader.loadPlayersInfos(this);
                for(NBTCompoundTag playerTag : players)
                {
                    UUID uuid = null;
                    if(playerTag.contains("uuid"))
                    {
                        uuid = UUID.fromString(playerTag.getString("uuid"));
                    }
                    else
                    {
                        uuid = SessionManager.getInstance().getUUID(playerTag.getString("displayName"));
                    }
                    playerData.put(uuid, playerTag);
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        this.name = name;
        this.generator = generator;
        this.chunkProvider = prov;
        this.gravity = 9.81f / 360f;
        spawingQueue = Lists.newArrayList();
        entities = new LinkedList<Entity>();
    }

    public void update(double delta)
    {
        while(!spawingQueue.isEmpty())
            entities.add(spawingQueue.remove(0));
        List<Entity> deadEntities = Lists.newArrayList();
        for(Entity e : entities)
        {
            //            Chunk c = getChunk((int) e.getX(), (int) e.getY(), (int) e.getZ());
            //            if(c != null)
            //                c.update();
            e.update(delta);

            if(e.isDead())
            {
                deadEntities.add(e);
            }
        }
        entities.removeAll(deadEntities);

        for(int i = 0; i < schedulers.size(); i++ )
        {
            BlockUpdateScheduler scheduler = schedulers.get(i);
            scheduler.tick();
        }
        tick++ ;
    }

    /**
     * Returns chunk from given coords in world space
     */
    public Chunk getChunk(int x, int y, int z)
    {
        return chunkProvider.get(this, (int) Math.floor((float) x / 16f), (int) Math.floor((float) y / 16f), (int) Math.floor((float) z / 16f));
    }

    public void addChunk(Chunk c)
    {
        chunkProvider.addChunk(this, c);
    }

    /**
     * Returns block next to given coords and given side
     */
    public Block getBlockNextTo(int x, int y, int z, EnumSide side)
    {
        return getBlockAt(x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ());
    }

    /**
     * Returns block at given coords
     */
    public Block getBlockAt(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null)
            return Blocks.air;
        return c.getBlock(this, x, y, z);
    }

    public IBlockStateValue getBlockState(int x, int y, int z, BlockState state)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null)
            return null;
        return c.getBlockState(x, y, z, state);
    }

    /**
     * Sets block state at given coords
     */
    public void setBlockState(int x, int y, int z, BlockState state, IBlockStateValue value)
    {
        setBlockState(x, y, z, state, value, false);
    }

    public void setBlockState(int x, int y, int z, BlockState state, IBlockStateValue value, boolean notify)
    {
        Chunk c = getChunk(x, y, z);
        if(c != null)
        {
            c.setBlockState(x, y, z, state, value);
            if(notify)
                updateBlockNeighbors(x, y, z, false);
        }
    }

    /**
     * Sets block at given coords
     */
    public void setBlock(int x, int y, int z, Block block)
    {
        setBlock(x, y, z, block, true);
    }

    public void setBlock(int x, int y, int z, Block block, boolean notify)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null)
        {
            return;
        }
        Block oldBlock = c.getBlock(this, x, y, z);
        CommonHandler.getCurrentInstance().getEventBus().fireEvent(new ModBlockChangeEvent(CommonHandler.getCurrentInstance(), this, x, y, z, oldBlock, block));
        c.setBlock(this, x, y, z, block);
        if(notify)
        {
            c.markDirty();
            updateBlockNeighbors(x, y, z, false);
        }
    }

    /**
     * Spawns a new entity in world
     */
    public void spawn(Entity e)
    {
        this.spawingQueue.add(e);
    }

    /**
     * Performs a raycast from {@code sender} to get object in front of it. Results are saved into {@code infos}
     */
    public void performRayCast(Entity sender, CollisionInfos infos, float maxDist)
    {
        float maxReachedDist = maxDist;
        float size = 0.45f;
        Vector3 origin = Vector3.get(sender.posX, sender.posY + sender.getEyeOffset() - size / 2f, sender.posZ);
        Vector3 pos = origin;
        Vector3 ray = sender.getQuaternionRotation().getForward();

        int x = 0;
        int y = 0;
        int z = 0;
        float step = 0.005f;
        AABB rayBB = new AABB(Vector3.get(0, 0, 0), Vector3.get(size, size, size));
        Vector3 blockPos = null;
        for(float dist = 0f; dist < maxDist - step; dist += step)
        {
            x = (int) Math.round(pos.getX());
            y = (int) Math.round(pos.getY());
            z = (int) Math.round(pos.getZ());
            Block b = getBlockAt(x, y, z);
            if(b != null)
            {
                AABB blockBB = b.getSelectionBox(this, x, y, z);
                if(blockBB != null)
                {
                    AABB translatedRay = rayBB.translate(pos);
                    if(blockBB.intersectAABB(translatedRay))
                    {
                        infos.x = x;
                        infos.y = y;
                        infos.z = z;
                        infos.type = CollisionType.BLOCK;
                        infos.side = EnumSide.BOTTOM;
                        infos.value = b;
                        blockPos = Vector3.get(x + 0.5f, y + 0.5f, z + 0.5f);

                        Vector3 diff = blockPos.sub(pos.add(0.5f));
                        blockPos.dispose();

                        float absx = Math.abs(diff.getX());
                        float absy = Math.abs(diff.getY());
                        float absz = Math.abs(diff.getZ());

                        if(absx > absy && absx > absz)
                        {
                            if(diff.getX() > 0)
                                infos.side = EnumSide.WEST;
                            else
                                infos.side = EnumSide.EAST;
                        }
                        if(absy > absx && absy > absz)
                        {
                            if(diff.getY() > 0)
                                infos.side = EnumSide.BOTTOM;
                            else
                                infos.side = EnumSide.TOP;
                        }
                        if(absz > absy && absz > absx)
                        {
                            if(diff.getZ() > 0)
                                infos.side = EnumSide.NORTH;
                            else
                                infos.side = EnumSide.SOUTH;
                        }
                        diff.dispose();
                    }
                    translatedRay.dispose();
                    blockBB.dispose();
                }
            }
            if(pos != origin)
                pos.dispose();
            Vector3 multipliedRay = ray.mul((maxDist + step) - dist);
            pos = origin.add(multipliedRay);
            multipliedRay.dispose();
            for(Entity e : entities)
            {
                if(e.getBoundingBox().intersectAABB(rayBB))
                {
                    infos.type = CollisionType.ENTITY;
                    infos.value = e;
                    infos.x = e.posX;
                    infos.y = e.posY;
                    infos.z = e.posZ;
                    infos.distance = maxReachedDist;
                }
            }
        }
        if(blockPos != null)
            blockPos.dispose();
        origin.dispose();
        pos.dispose();
        ray.dispose();
        rayBB.dispose();
    }

    public WorldGenerator getGenerator()
    {
        return generator;
    }

    public ChunkProvider getChunkProvider()
    {
        return chunkProvider;
    }

    /**
     * Returns true if given block can see the sky
     */
    public boolean canBlockSeeSky(int x, int y, int z)
    {
        for(int y1 = y + 1; y1 < 256; y1++ )
        {
            if(!getBlockAt(x, y1, z).letLightGoThrough())
            {
                return false;
            }
        }
        return true;
    }

    public List<Entity> getEntitiesList()
    {
        return entities;
    }

    public void clearStates(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c != null)
        {
            c.clearStates(x, y, z);
        }
    }

    /**
     * Returns whether a chunk exists at chunk coordinates x, y, z
     */
    public boolean doesChunkExists(int x, int y, int z)
    {
        return this.chunkProvider.doesChunkExists(this, x, y, z);
    }

    public void createChunk(final int x, final int y, final int z)
    {
        //Log.message("Generating chunk on " + x + ", " + y + ", " + z);
        if(doesChunkExists(x, y, z) && Dev.debug())
            Log.error("Cannot generate a chunk on a chunk on " + x + ", " + y + ", " + z);
        else
        {
            chunkProvider.create(this, x, y, z);
        }
    }

    public BlockStatesObject getBlockStates(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c != null)
        {
            return c.getBlockStates(x, y, z);
        }
        return null;
    }

    public boolean updateBlock(int x, int y, int z, boolean force, List<Vector3> visited, Block from)
    {
        boolean disposeList = false;
        if(visited == null)
        {
            disposeList = true;
            visited = Lists.newArrayList();
        }
        Block b = getBlockAt(x, y, z);
        if(b != null)
        {
            Vector3 posVec = Vector3.get(x, y, z);
            if(!visited.contains(posVec) || force)
            {
                visited.add(posVec);
                if(!CommonHandler.getCurrentInstance().getEventBus().fireEvent(new ModBlockUpdateEvent(CommonHandler.getCurrentInstance(), this, x, y, z, b, from)))
                    b.onBlockUpdate(this, x, y, z, visited);
            }
            return true;
        }
        if(disposeList)
        {
            for(Vector3 v : visited)
                v.dispose();
            visited.clear();
        }
        return false;
    }

    public void cleanDirtiness(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c != null)
            c.cleanDirtiness(x, y, z);
    }

    public boolean isDirty(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c != null)
            return c.isDirty(x, y, z);
        return false;
    }

    protected boolean updateBlockFromNeighbor(int x, int y, int z, boolean force, List<Vector3> visited, Block from)
    {
        Block b = getBlockAt(x, y, z);
        if(b != null)
        {
            Vector3 posVec = Vector3.get(x, y, z);
            if(!visited.contains(posVec) || force)
            {
                visited.add(posVec);
                if(!CommonHandler.getCurrentInstance().getEventBus().fireEvent(new ModBlockUpdateEvent(CommonHandler.getCurrentInstance(), this, x, y, z, b, from)))
                    b.onBlockUpdateFromNeighbor(this, x, y, z, visited);
            }
            return true;
        }
        return false;
    }

    public void updateBlockAndNeighbors(int x, int y, int z, boolean force, List<Vector3> visited)
    {
        boolean disposeList = false;
        if(visited == null)
        {
            disposeList = true;
            visited = Lists.newArrayList();
        }
        updateBlock(x, y, z, force, visited, getBlockAt(x, y, z));
        updateBlockNeighbors(x, y, z, force, visited);
        if(disposeList)
        {
            for(Vector3 v : visited)
                v.dispose();
            visited.clear();
        }
    }

    public void updateBlockNeighbors(int x, int y, int z, boolean force)
    {
        updateBlockNeighbors(x, y, z, force, null);
    }

    public void updateBlockNeighbors(int x, int y, int z, boolean force, List<Vector3> visited)
    {
        Block from = getBlockAt(x, y, z);
        boolean disposeList = false;
        if(visited == null)
        {
            visited = Lists.newArrayList();
            disposeList = true;
        }
        updateBlockFromNeighbor(x, y, z + 1, force, visited, from);
        updateBlockFromNeighbor(x, y, z - 1, force, visited, from);
        updateBlockFromNeighbor(x, y + 1, z, force, visited, from);
        updateBlockFromNeighbor(x, y - 1, z, force, visited, from);
        updateBlockFromNeighbor(x + 1, y, z, force, visited, from);
        updateBlockFromNeighbor(x - 1, y, z, force, visited, from);
        if(disposeList)
        {
            for(Vector3 v : visited)
                v.dispose();
            visited.clear();
        }

    }

    public int getDirectElectricPowerAt(int x, int y, int z)
    {
        Chunk c = getChunk(x, y, z);
        if(c == null)
            return 0;
        int maxPower = 0;
        for(EnumSide side : EnumSide.values())
        {
            if(side == EnumSide.UNDEFINED)
                continue;
            IBlockStateValue value = getBlockState(x + side.getTranslationX(), y + side.getTranslationY(), z + side.getTranslationZ(), BlockStates.electricPower);
            if(value != null && value instanceof EnumPowerStates)
            {
                EnumPowerStates power = (EnumPowerStates) value;
                if(power.powerValue() == 15)
                    return 15;
                else if(power.powerValue() > maxPower)
                    maxPower = power.powerValue();
            }
        }
        return maxPower;
    }

    public String getName()
    {
        return name;
    }

    public void setSeed(long seed)
    {
        this.getGenerator().setSeed(seed);
    }

    public long getSeed()
    {
        return getGenerator().getSeed();
    }

    public WorldLoader getLoader()
    {
        return worldLoader;
    }

    public Random getRNG()
    {
        return rng;
    }

    public long getTick()
    {
        return tick;
    }

    public float getGravity()
    {
        return gravity;
    }

    public void onScheduledBlockUpdate(int x, int y, int z, long interval)
    {
        Block b = getBlockAt(x, y, z);
        if(b != null)
        {
            b.onScheduledUpdate(this, x, y, z, interval, tick);
        }
    }

    /**
     * Schedules block updates for every <code>interval</code> tick.<br/>
     * Throws {@link IllegalArgumentException} if <code>interval <= 0</code>
     * @param x The x coord of the block to update in the world
     * @param y The y coord of the block to update in the world
     * @param z The z coord of the block to update in the world
     * @param interval A number from 1 to {@link Long#MAX_VALUE} which corresponds the rate at which the block needs to be updated (in world ticks)
     * @throws IllegalArgumentException if <code>interval</code> <= 0
     */
    public void scheduleBlockUpdates(int x, int y, int z, long interval)
    {
        if(interval <= 0)
        {
            throw new IllegalArgumentException("Block update tick interval can't be null or negative!");
        }
        schedulers.add(new BlockUpdateScheduler(x, y, z, interval));
    }

    public boolean removeScheduledUpdater(int x, int y, int z)
    {
        BlockUpdateScheduler toRemove = null;
        for(BlockUpdateScheduler s : schedulers)
        {
            if(s.getX() == x && s.getY() == y && s.getZ() == z)
            {
                toRemove = s;
            }
        }
        schedulers.remove(toRemove);
        return toRemove != null;
    }

    public void setDelegateParticleHandler(IParticleHandler delegate)
    {
        this.delegateParticleHandler = delegate;
    }

    public IParticleHandler getDelegateParticleHandler()
    {
        return delegateParticleHandler;
    }

    public void setDelegateSoundProducer(IAudioHandler delegate)
    {
        this.delegateSoundProducer = delegate;
    }

    public IAudioHandler getDelegateSoundProducer()
    {
        return delegateSoundProducer;
    }

    public void spawnParticle(String string, ILocatable loc)
    {
        spawnParticle(string, loc.getWorld(), loc.getPosX(), loc.getPosY(), loc.getPosZ());
    }

    public void spawnParticle(String string, World w, float x, float y, float z)
    {
        delegateParticleHandler.spawnParticle(string, w, x, y, z);
    }

    public void spawnParticle(Particle particle)
    {
        delegateParticleHandler.spawnParticle(particle);
    }

    public void playSound(String id, float x, float y, float z)
    {
        delegateSoundProducer.playSound(id, this, x, y, z);
    }

    @Override
    public void playSound(String id, World w, float x, float y, float z)
    {
        playSound(id, x, y, z);
    }

    @Override
    public void playSound(Sound sound)
    {
        delegateSoundProducer.playSound(sound);
    }

    public void performExplosion(Explosion explosion)
    {
        new Thread(explosion).start();
    }

    public List<Entity> getEntitiesInRadius(float x, float y, float z, float f)
    {
        List<Entity> result = Lists.newArrayList();
        Vector3 center = Vector3.get(x, y, z);
        for(Entity e : entities)
        {
            Vector3 entPos = Vector3.get(e.posX, e.posY, e.posZ);
            if(entPos.sub(center).length() < f)
                result.add(e);
            entPos.dispose();
        }
        center.dispose();
        return result;
    }

    @Override
    public void updateAllParticles()
    {
        delegateParticleHandler.updateAllParticles();
    }

    @Override
    public void playMusic(String id, float volume)
    {
        delegateSoundProducer.playMusic(id, volume);
    }

    @Override
    public void playMusic(Music music)
    {
        delegateSoundProducer.playMusic(music);
    }

    @Override
    public void playSound(String id, ILocatable location)
    {
        playSound(id, location.getWorld(), location.getPosX(), location.getPosY(), location.getPosZ());
    }

    /**
     * Creates a new EntityPlayer based on given UUID. This method will search for this player's spawnpoint and will get the
     * global one if none is found.
     * @param uuid The UUID of the player
     * @return A newly created Entity with the data saved in the world's data. This entity has to be spawned manually into the world.
     */
    public EntityPlayer createPlayerEntity(UUID uuid)
    {
        EntityPlayer player = new EntityPlayer(this, uuid);
        if(playerData.containsKey(player.getUUID()))
        {
            player.readFromNBT(playerData.get(player.getUUID()));
            Log.message(">>FOUND for player: " + uuid + " => " + SessionManager.getInstance().getName(uuid));
        }
        else
        {
            player.setLocation(0, 160 + 17, 0);
            Log.error("No infos for player: " + uuid + " => " + SessionManager.getInstance().getName(uuid));
        }
        return player;
    }
}
