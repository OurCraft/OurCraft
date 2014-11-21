package org.craft.utils.io;

import java.nio.FloatBuffer;

public interface IBufferWritable
{

	public void write(FloatBuffer buffer);

	public int getSize();
}
