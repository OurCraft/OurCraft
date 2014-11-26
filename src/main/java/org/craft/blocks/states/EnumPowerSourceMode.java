package org.craft.blocks.states;

import com.google.common.base.*;

public enum EnumPowerSourceMode implements IBlockStateValue
{
    CONSTANT("constant", new ConstantFunction()), SIN("sin", new SinFunction()), COS("cos", new CosFunction()),
    TAN("tan", new TanFunction()), INVERSE("inverse", new InverseFunction()), SQRT("sqrt", new SqrtFunction());

    private static final class ConstantFunction implements Function<Long, EnumPowerStates>
    {
        @Override
        public EnumPowerStates apply(Long tick)
        {
            return EnumPowerStates.POWER_15;
        }
    }

    private static final class SinFunction implements Function<Long, EnumPowerStates>
    {
        @Override
        public EnumPowerStates apply(Long tick)
        {
            double valued = Math.sin(Math.toRadians((float) tick * 1f / 60f * 360f)) / 2f + 0.5f;
            int value = (int) (valued * 16f);
            return EnumPowerStates.fromPowerValue(value);
        }
    }

    private static final class CosFunction implements Function<Long, EnumPowerStates>
    {
        @Override
        public EnumPowerStates apply(Long tick)
        {
            double valued = Math.cos(Math.toRadians((float) tick * 1f / 60f * 360f)) / 2f + 0.5f;
            int value = (int) (valued * 16f);
            return EnumPowerStates.fromPowerValue(value);
        }
    }

    private static final class TanFunction implements Function<Long, EnumPowerStates>
    {
        @Override
        public EnumPowerStates apply(Long tick)
        {
            double valued = Math.tan(Math.toRadians((float) tick * 1f / 60f * 360f)) / 2f + 0.5f;
            int value = (int) (valued * 16f);
            return EnumPowerStates.fromPowerValue(value);
        }
    }

    private static final class SqrtFunction implements Function<Long, EnumPowerStates>
    {
        @Override
        public EnumPowerStates apply(Long tick)
        {
            double valued = Math.sqrt(tick * 2f % 60);
            int value = (int) (valued * 2f);
            return EnumPowerStates.fromPowerValue(value);
        }
    }

    private static final class InverseFunction implements Function<Long, EnumPowerStates>
    {
        @Override
        public EnumPowerStates apply(Long tick)
        {
            float t = 1f / (tick * 2f % 60);
            //            Log.message("T: " + t * 100);
            return EnumPowerStates.fromPowerValue((int) Math.floor(t * 100));
        }
    }

    private String                          id;
    private Function<Long, EnumPowerStates> function;

    private EnumPowerSourceMode(String id, Function<Long, EnumPowerStates> function)
    {
        this.id = id;
        this.function = function;
    }

    public Function<Long, EnumPowerStates> function()
    {
        return function;
    }

    @Override
    public String toString()
    {
        return id;
    }
}
