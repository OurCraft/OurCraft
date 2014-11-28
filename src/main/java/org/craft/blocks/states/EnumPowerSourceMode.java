package org.craft.blocks.states;

import com.google.common.base.*;

import org.craft.maths.*;

public enum EnumPowerSourceMode implements IBlockStateValue
{
    CONSTANT("constant", new ConstantFunction()), SIN("sin", new SinFunction(), true), COS("cos", new CosFunction(), true),
    TAN("tan", new TanFunction()), INVERSE("inverse", new InverseFunction()), SQRT("sqrt", new SqrtFunction()),
    PULSES("pulses", new PulsesFunction()), RANDOM("rand", new RandomFunction()), NOISE("noise", new NoiseFunction(), true),
    SQUARE("square", new SquareFunction(), true);

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
            return EnumPowerStates.fromPowerValue((int) Math.floor(t * 100));
        }
    }

    private static final class PulsesFunction implements Function<Long, EnumPowerStates>
    {
        @Override
        public EnumPowerStates apply(Long tick)
        {
            float x = (tick - 20 % 60);
            float a = 1;
            float p = 60 / 2;
            float y = (float) (-(2 * a) / Math.PI * Math.atan(1.0 / Math.tan((x * Math.PI) / p)));
            return EnumPowerStates.fromPowerValue((int) Math.floor(y * 15f));
        }
    }

    private static final class RandomFunction implements Function<Long, EnumPowerStates>
    {
        @Override
        public EnumPowerStates apply(Long tick)
        {
            float x = (float) (Math.random() * 15f);
            float t = x;
            return EnumPowerStates.fromPowerValue((int) Math.floor(t));
        }
    }

    private static final class NoiseFunction implements Function<Long, EnumPowerStates>
    {
        @Override
        public EnumPowerStates apply(Long tick)
        {
            float x = (tick * 2f % 60) / 60f;

            float val = (MathHelper.perlinNoise(x, tick, 0) / 2f + 0.5f) * 1.25f;
            return EnumPowerStates.fromPowerValue((int) (Math.floor(val * 15f)));
        }
    }

    private static final class SquareFunction implements Function<Long, EnumPowerStates>
    {
        @Override
        public EnumPowerStates apply(Long tick)
        {
            float x = (tick % 60) / 60f;
            float val = x <= 0.5f ? 0 : 1f;
            return EnumPowerStates.fromPowerValue((int) (Math.floor(val * 15f)));
        }
    }

    private String                          id;
    private Function<Long, EnumPowerStates> function;
    private boolean                         usesBezier;

    private EnumPowerSourceMode(String id, Function<Long, EnumPowerStates> function)
    {
        this(id, function, false);
    }

    private EnumPowerSourceMode(String id, Function<Long, EnumPowerStates> function, boolean isBezierCurveNotTooBad)
    {
        this.usesBezier = isBezierCurveNotTooBad;
        this.id = id;
        this.function = function;
    }

    public boolean usesBezier()
    {
        return usesBezier;
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
