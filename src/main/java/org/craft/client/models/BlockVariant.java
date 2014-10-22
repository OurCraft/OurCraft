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

    /**
     * Sets block state of this variant
     */
    public void setBlockStateKey(BlockState state)
    {
        this.state = state;
    }

    /**
     * Sets block state value of this variant
     */
    public void setBlockStateValue(IBlockStateValue value)
    {
        this.value = value;
    }

    /**
     * Gets block state of this variant
     */
    public BlockState getBlockState()
    {
        return state;
    }

    /**
     * Gets block state value of this variant
     */
    public IBlockStateValue getBlockStateValue()
    {
        return value;
    }

    /**
     * Add model to this block variant
     */
    public void addBlockModel(BlockModel model)
    {
        this.models.add(model);
    }

    /**
     * Returns a mutable list of models used in this variant
     */
    public List<BlockModel> getModels()
    {
        return models;
    }

    /**
     * Sets pass in which the variant is rendered.<br/>Throws a {@link NullPointerException} if given pass is null
     */
    public void setPass(EnumRenderPass pass)
    {
        if(pass == null)
            throw new NullPointerException("Render Pass can't be null");
        this.pass = pass;
    }

    /**
     * Gets pass in which the variant is rendered
     */
    public EnumRenderPass getPass()
    {
        return pass;
    }
}
