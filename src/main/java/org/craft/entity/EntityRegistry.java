package org.craft.entity;

import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.*;
import com.mojang.nbt.*;

import org.craft.world.*;

public class EntityRegistry
{

    public static final HashMap<String, Class<? extends Entity>>  ENTITY_REGISTRY = Maps.newHashMap();
    private static final HashMap<Class<? extends Entity>, String> types2ids       = Maps.newHashMap();

    /**
     * Registers all entities of the game
     */
    public static void init()
    {
        register("player", EntityPlayer.class);
        register("playermp", EntityPlayerMP.class);
        register("tnt", EntityPrimedTNT.class);
        register("test", EntityTest.class);
    }

    /**
     * Registers an entity type into the ENTITY_REGISTRY
     */
    public static void register(String id, Class<? extends Entity> clazz)
    {
        if(ENTITY_REGISTRY.containsKey(id))
        {
            throw new IllegalArgumentException("Id " + id + " is already used by " + ENTITY_REGISTRY.get(id) + " when trying to add " + clazz);
        }
        ENTITY_REGISTRY.put(id, clazz);
        types2ids.put(clazz, id);
    }

    /**
     * Returns the entity type in ENTITY_REGISTRY with given id
     */
    public static Class<? extends Entity> get(String string)
    {
        return ENTITY_REGISTRY.get(string);
    }

    public static Collection<Class<? extends Entity>> getAllEntityTypes()
    {
        return ENTITY_REGISTRY.values();
    }

    public static Entity createEntity(World w, NBTCompoundTag entityData) throws ReflectiveOperationException, SecurityException
    {
        String id = entityData.getString("typeID");
        if(id == null)
        {
            throw new IllegalArgumentException("Not id found while creating entity " + entityData.toJson().toString());
        }
        Class<? extends Entity> type = get(id);
        if(type == null)
        {
            throw new IllegalArgumentException("Not type found for id " + id + " while creating entity " + entityData.toJson().toString());
        }
        Constructor<? extends Entity> cons = type.getConstructor(World.class);
        Entity e = cons.newInstance(w);
        e.readFromNBT(entityData);
        return e;
    }

    public static String getType(Entity entity)
    {
        return getType(entity.getClass());
    }

    public static String getType(Class<? extends Entity> clazz)
    {
        return types2ids.get(clazz);
    }
}
