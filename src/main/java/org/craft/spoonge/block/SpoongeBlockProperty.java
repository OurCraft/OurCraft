package org.craft.spoonge.block;

import java.util.*;

import com.google.common.base.Optional;

import org.craft.blocks.states.*;
import org.craft.blocks.states.BlockState;
import org.spongepowered.api.block.*;

public class SpoongeBlockProperty implements BlockProperty<String>
{

    private BlockState state;

    public SpoongeBlockProperty(org.craft.blocks.states.BlockState state)
    {
        this.state = state;
    }

    @Override
    public String getName()
    {
        return state.toString();
    }

    @Override
    public Collection<String> getValidValues()
    {
        ArrayList<String> validValues = new ArrayList<String>();
        Collection<IBlockStateValue> values = BlockStates.getValues(state);
        for(IBlockStateValue value : values)
        {
            validValues.add(value.toString());
        }
        return validValues;
    }

    @Override
    public String getNameForValue(String value)
    {
        IBlockStateValue val = BlockStates.getValue(state, value);
        if(val == null)
            return "null";
        return val.toString();
    }

    @Override
    public Optional<String> getValueForName(String name)
    {
        // To OurCraft, block state values and block state "values'names" are the same.
        return Optional.of(name);
    }

}
