package org.craft.utils.crash;

public class SystemInfos implements CrashInfos
{

    @Override
    public String getInfos()
    {
        String osName = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");
        String jvmName = System.getProperty("java.vm.name");
        String jvmVendor = System.getProperty("java.vm.vendor");
        String javaVendor = System.getProperty("java.vendor");
        String javaVersion = System.getProperty("java.version");

        return SECTION_START + " System info " + SECTION_END +
                "\n\tOperating System: " + osName + " (" + arch + ")" + " version " + osVersion +
                "\n\tJava Version: " + javaVersion + ", " + javaVendor + "\n\tVM Version: " + jvmName + ", " + jvmVendor;
    }

}
