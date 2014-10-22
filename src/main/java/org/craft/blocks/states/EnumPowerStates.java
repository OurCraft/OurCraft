package org.craft.blocks.states;

public enum EnumPowerStates implements IBlockStateValue
{

    POWER_0(0),
    POWER_1(1), POWER_2(2), POWER_3(3), POWER_4(4), POWER_5(5),
    POWER_6(6), POWER_7(7), POWER_8(8), POWER_9(9), POWER_10(10),
    POWER_11(11), POWER_12(12), POWER_13(13), POWER_14(14), POWER_15(15);

    private String id;
    private int    powerValue;

    private EnumPowerStates(int powerValue)
    {
        this.powerValue = powerValue;
        this.id = "power_" + powerValue;
    }

    /**
     * Returns the int power value of this instance (0-15)
     */
    public int powerValue()
    {
        return powerValue;
    }

    @Override
    public String toString()
    {
        return id;
    }

    /**
     * Returns a power state value depending on given powerValue.<br/>
     * If powerValue <= 0 then this method returns {@link #POWER_0}<br/>
     * If powerValue >= 15 then this method returns {@link #POWER_15}<br/>
     */
    public static EnumPowerStates fromPowerValue(int powerValue)
    {
        if(powerValue <= 0)
            return POWER_0;
        if(powerValue >= 15)
            return POWER_15;
        for(EnumPowerStates power : values())
        {
            if(power.powerValue == powerValue)
                return power;
        }
        return POWER_0;
    }
}
