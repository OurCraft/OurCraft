package org.craft.client.models;

import java.util.*;

import com.google.common.collect.*;

import org.craft.blocks.states.*;
import org.craft.client.render.*;

public class BlockVariant
{

    private BlockState            state;
    private IBlockStateValue      value;
    private ArrayList<BlockModel> models;
    private EnumRenderPass        pass;

    public BlockVariant()
    {
        pass = EnumRenderPass.NORMAL;
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

    public void setPass(EnumRenderPass pass)
    {
        if(pass == null)
            throw new NullPointerException("Render Pass can't be null");
        this.pass = pass;
    }

    public EnumRenderPass getPass()
    {
        return pass;
    }
}
