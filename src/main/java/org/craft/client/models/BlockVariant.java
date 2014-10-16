package org.craft.client.models;

import java.util.*;

import com.google.common.collect.*;

import org.craft.blocks.states.*;

public class BlockVariant
{

    private BlockState            state;
    private IBlockStateValue      value;
    private ArrayList<BlockModel> models;

    public BlockVariant()
    {
        models = Lists.newArrayList();
    }

    public void setBlockStateKey(BlockState state)
    {
        this.state = state;
    }

    public void setBlockStateValue(IBlockStateValue value)
    {
        this.value = value;
    }

    public BlockState getBlockState()
    {
        return state;
    }

    public IBlockStateValue getBlockStateValue()
    {
        return value;
    }

    public void addBlockModel(BlockModel model)
    {
        this.models.add(model);
    }

    public List<BlockModel> getModels()
    {
        return models;
    }
}
