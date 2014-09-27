package org.craft.utils.crash;

public class OSInfos implements CrashInfos
{

	@Override
	public String getInfos()
	{
		String osName = System.getProperty("os.name");
		String osVersion = System.getProperty("os.version");
		return SECTION_START + " Operating System " + SECTION_END + "\n\tName: " + osName + "\n\tVersion: " + osVersion;
	}

}
