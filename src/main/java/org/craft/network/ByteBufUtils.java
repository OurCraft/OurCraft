package org.craft.network;

import io.netty.buffer.*;

public class ByteBufUtils
{

	public static byte[] getAsArray(ByteBuf msg)
	{
		byte[] buffer = new byte[msg.readableBytes()];
		msg.readBytes(buffer);
		return buffer;
	}

	public static void writeString(ByteBuf buffer, String s)
	{
		buffer.writeInt(s.length());
		for(char c : s.toCharArray())
		{
			buffer.writeChar(c);
		}
	}

	public static String readString(ByteBuf buffer)
	{
		int l = buffer.readInt();
		String result = "";
		for(int i = 0; i < l; i++ )
		{
			result += buffer.readChar();
		}
		return result;
	}

	public static void writeBytes(ByteBuf buffer, byte[] bytes)
	{
		buffer.writeInt(bytes.length);
		buffer.writeBytes(bytes);
	}

	public static byte[] readBytes(ByteBuf buffer)
	{
		int l = buffer.readInt();
		byte[] result = new byte[l];
		buffer.readBytes(result);
		return result;
	}

}
