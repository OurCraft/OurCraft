package org.craft.spoonge.modifiers;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.*;

import org.craft.blocks.states.*;
import org.craft.modding.modifiers.*;
import org.craft.spoonge.block.*;
import org.spongepowered.api.block.*;
import org.spongepowered.api.block.BlockState;

public class SpoongeBlockState implements BlockState
{

    private BlockType         type;
    private BlockStatesObject map;

    @Overwrite
    public SpoongeBlockState()
    {
        ;
    }

    public void setType(BlockType type)
    {
        this.type = type;
    }

    public void setMap(BlockStatesObject map)
    {
        this.map = map;
    }

    @Override
    public BlockType getType()
    {
        return type;
    }

    @Override
    public ImmutableMap<BlockProperty<?>, ? extends Comparable<?>> getProperties()
    {
        HashMap<BlockProperty<?>, Comparable<?>> properties = new HashMap<BlockProperty<?>, Comparable<?>>();
        for(org.craft.blocks.states.BlockState state : map.getMap().keySet())
        {
            properties.put(new SpoongeBlockProperty(state), map.get(state).toString());
        }
        ImmutableMap<BlockProperty<?>, ? extends Comparable<?>> immutableMap = ImmutableMap.copyOf(properties);
        return immutableMap;
    }

    @Override
    public Collection<String> getPropertyNames()
    {
        ArrayList<String> names = new ArrayList<String>();
        for(org.craft.blocks.states.BlockState state : map.getMap().keySet())
        {
            names.add(state.toString());
        }
        return names;
    }

    @Override
    public Optional<BlockProperty<?>> getPropertyByName(String name)
    {
        //   return Optional.of(new SpoongeBlockProperty(org.craft.blocks.states.BlockStates.getState(name)));
        return Optional.absent();
    }

    @Override
    public Optional<? extends Comparable<?>> getPropertyValue(String name)
    {
        return Optional.of(map.get(org.craft.blocks.states.BlockStates.getState(name)).toString());
    }

    @Override
    public BlockState withProperty(BlockProperty<?> property, Comparable<?> value)
    {
        BlockStatesObject clonedMap = map.clone();
        org.craft.blocks.states.BlockState state = BlockStates.getState(property.getName());
        clonedMap.set(state, BlockStates.getValue(state, value.toString()));
        SpoongeBlockState blockState = new SpoongeBlockState();
        blockState.setMap(clonedMap);
        blockState.setType(type);
        return blockState;
    }

    @Override
    public BlockState cycleProperty(BlockProperty<?> property)
    {
        BlockStatesObject clonedMap = map.clone();
        org.craft.blocks.states.BlockState state = BlockStates.getState(property.getName());
        Collection<IBlockStateValue> values = BlockStates.getValues(state);
        ArrayList<IBlockStateValue> valuesAsList = Lists.newArrayList(values);
        int index = valuesAsList.indexOf(map.get(state));
        index += 1;
        IBlockStateValue newValue = valuesAsList.get(index % valuesAsList.size());
        clonedMap.set(state, newValue);
        valuesAsList.clear();
        SpoongeBlockState blockState = new SpoongeBlockState();
        blockState.setMap(clonedMap);
        blockState.setType(type);
        return blockState;
    }

    @Override
    public byte getDataValue()
    {
        return 0;
    }

}
