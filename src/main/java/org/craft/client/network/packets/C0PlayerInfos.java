package org.craft.client.network.packets;

import io.netty.buffer.*;

import java.util.*;

import org.craft.network.*;
import org.craft.utils.*;

public class C0PlayerInfos extends AbstractPacket
{

    private Session session;

    public C0PlayerInfos()
    {

    }

    public C0PlayerInfos(Session session)
    {
        this.session = session;
    }

    @Override
    public void decodeFrom(ByteBuf buffer)
    {
        String id = ByteBufUtils.readString(buffer);
        String displayName = ByteBufUtils.readString(buffer);
        UUID uuid = UUID.fromString(ByteBufUtils.readString(buffer));
        this.session = new Session(uuid, id, displayName);
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        ByteBufUtils.writeString(buffer, session.getId());
        ByteBufUtils.writeString(buffer, session.getDisplayName());
        ByteBufUtils.writeString(buffer, session.getUUID().toString());
    }

    public Session getSession()
    {
        return session;
    }

}
