package org.craft.spoonge.util;

import com.flowpowered.math.vector.*;

import org.craft.utils.*;
import org.spongepowered.api.util.*;

public final class DirectionUtils
{

    public static Direction fromSide(EnumSide side)
    {
        return Direction.getClosest(new Vector3d(side.getTranslationX(), side.getTranslationY(), side.getTranslationZ()));
    }

    public static EnumSide fromDirection(Direction direct)
    {
        Vector3d directVec = direct.toVector3d();
        double currentDistance = Double.POSITIVE_INFINITY;
        EnumSide found = EnumSide.UNDEFINED;
        for(EnumSide side : EnumSide.values())
        {
            Vector3d sideVec = new Vector3d(side.getTranslationX(), side.getTranslationY(), side.getTranslationZ());
            double d = sideVec.distance(directVec);
            if(d < currentDistance)
            {
                currentDistance = d;
                found = side;
            }
        }
        return found;
    }
}
