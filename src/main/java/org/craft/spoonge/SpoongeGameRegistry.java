package org.craft.spoonge;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;

import org.craft.blocks.*;
import org.craft.items.*;
import org.craft.items.Item;
import org.spongepowered.api.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.effect.*;
import org.spongepowered.api.entity.*;
import org.spongepowered.api.entity.hanging.art.*;
import org.spongepowered.api.entity.living.meta.*;
import org.spongepowered.api.entity.living.villager.*;
import org.spongepowered.api.entity.player.gamemode.*;
import org.spongepowered.api.item.*;
import org.spongepowered.api.item.inventory.*;
import org.spongepowered.api.item.merchant.*;
import org.spongepowered.api.potion.*;
import org.spongepowered.api.world.biome.*;

public class SpoongeGameRegistry implements GameRegistry
{

    public SpoongeGameRegistry()
    {
    }

    @Override
    public com.google.common.base.Optional<BlockType> getBlock(String id)
    {
        if(!id.contains(":"))
        {
            id = "ourcraft:" + id;
        }
        else if(id.startsWith("minecraft:"))
        {
            id = id.replace("minecraft:", "ourcraft:");
        }
        return com.google.common.base.Optional.of((BlockType) Blocks.get(id));
    }

    @Override
    public com.google.common.base.Optional<ItemType> getItem(String id)
    {
        if(!id.contains(":"))
        {
            id = "ourcraft:" + id;
        }
        else if(id.startsWith("minecraft:"))
        {
            id = id.replace("minecraft:", "ourcraft:");
        }
        return com.google.common.base.Optional.of((ItemType) Items.get(id));
    }

    @Override
    public List<BlockType> getBlocks()
    {
        List<BlockType> types = Lists.newArrayList();
        for(Block block : Blocks.getBlocks())
            types.add((BlockType) block);
        return types;
    }

    @Override
    public List<ItemType> getItems()
    {
        List<ItemType> types = Lists.newArrayList();
        for(Item item : Items.getItems())
            types.add((ItemType) item);
        return types;
    }

    @Override
    public Optional<BiomeType> getBiome(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<BiomeType> getBiomes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemStackBuilder getItemBuilder()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TradeOfferBuilder getTradeOfferBuilder()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Particle> getParticle(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Particle> getParticles()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<EntityType> getEntity(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<EntityType> getEntities()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Art> getArt(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Art> getArts()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<DyeColor> getDye(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<DyeColor> getDyes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<HorseColor> getHorseColor(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<HorseColor> getHorseColors()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<HorseStyle> getHorseStyle(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<HorseStyle> getHorseStyles()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<HorseVariant> getHorseVariant(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<HorseVariant> getHorseVariants()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<OcelotType> getOcelotType(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<OcelotType> getOcelotTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<RabbitType> getRabbitType(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<RabbitType> getRabbitTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<SkeletonType> getSkeletonType(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SkeletonType> getSkeletonTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Career> getCareer(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Career> getCareers()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Career> getCareers(Profession profession)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Profession> getProfession(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Profession> getProfessions()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<GameMode> getGameModes()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PotionEffectType> getPotionEffects()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Enchantment> getEnchantment(String id)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Enchantment> getEnchantments()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> getDefaultGameRules()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
