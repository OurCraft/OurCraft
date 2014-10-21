package org.craft.client.sound;

public enum SoundFormats
{
	WAV(new WaveLoader());

	private SoundLoader loader;

	SoundFormats(SoundLoader loader)
	{
		this.loader = loader;
	}

	public SoundLoader getLoader()
	{
		return loader;
	}
}
