package org.craft.utils;

import java.nio.FloatBuffer;

public interface IBufferWritable
{

	public void write(FloatBuffer buffer);

	public int getSize();
}
