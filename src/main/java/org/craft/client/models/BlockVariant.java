package org.craft.client.models;

import java.util.*;

import com.google.common.collect.*;

import org.craft.blocks.states.*;
import org.craft.client.render.*;

public class BlockVariant
{

    private ArrayList<BlockState>       states;
    private ArrayList<IBlockStateValue> values;
    private ArrayList<Model>       models;
    private EnumRenderPass              pass;

    public BlockVariant()
    {
        pass = EnumRenderPass.NORMAL;
        models = Lists.newArrayList();
        states = Lists.newArrayList();
        values = Lists.newArrayList();
    }

    /**
     * Sets block state of this variant
     */
    public void setBlockStateKeys(ArrayList<BlockState> states)
    {
        this.states = states;
    }

    /**
     * Sets block state value of this variant
     */
    public void setBlockStateValues(ArrayList<IBlockStateValue> values)
    {
        this.values = values;
    }

    /**
     * Gets block state of this variant
     */
    public ArrayList<BlockState> getBlockStates()
    {
        return states;
    }

    /**
     * Gets block state value of this variant
     */
    public ArrayList<IBlockStateValue> getBlockStateValues()
    {
        return values;
    }

    /**
     * Add model to this block variant
     */
    public void addBlockModel(Model model)
    {
        this.models.add(model);
    }

    /**
     * Returns a mutable list of models used in this variant
     */
    public List<Model> getModels()
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
