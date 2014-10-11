package org.craft.blocks.states;

import java.util.*;

import com.google.common.collect.*;

public class BlockStatesObject implements Iterable<IBlockStateValue>
{

    private HashMap<BlockState, IBlockStateValue> map;

    public BlockStatesObject()
    {
        map = Maps.newHashMap();
    }

    public BlockStatesObject(BlockState[] states, IBlockStateValue[] values)
    {
        this();
        for(int i = 0; i < states.length; i++ )
        {
            map.put(states[i], values[i]);
        }
    }

    public void set(BlockState state, IBlockStateValue value)
    {
        map.put(state, value);
    }

    public IBlockStateValue get(BlockState state)
    {
        return map.get(state);
    }

    public boolean has(BlockState state)
    {
        return map.containsKey(state);
    }

    public void clear()
    {
        map.clear();
    }

    public HashMap<BlockState, IBlockStateValue> getMap()
    {
        return map;
    }

    public int size()
    {
        return map.size();
    }

    @Override
    public Iterator<IBlockStateValue> iterator()
    {
        return map.values().iterator();
    }
}
