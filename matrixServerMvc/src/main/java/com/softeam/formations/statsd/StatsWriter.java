package com.softeam.formations.statsd;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.management.UnixOperatingSystemMXBean;
import com.timgroup.statsd.StatsDClient;

@Component
public class StatsWriter {

    @Autowired
    private StatsDClient statsDClient;

    private static int nbUsers = 0;

    public void increment() {
        nbUsers++;
    }

    public void decrement() {
        nbUsers--;
    }

    public void write() {
        statsDClient.gauge("userCount", nbUsers);

        statsDClient.gauge("threadCount", Thread.activeCount());

        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();

        if (operatingSystemMXBean instanceof UnixOperatingSystemMXBean) {
            UnixOperatingSystemMXBean unixOperatingSystem = (UnixOperatingSystemMXBean) operatingSystemMXBean;

            statsDClient.gauge("committedVirtualMemorySize", unixOperatingSystem.getCommittedVirtualMemorySize());
            statsDClient.gauge("freePhysicalMemorySize", unixOperatingSystem.getFreePhysicalMemorySize());
            statsDClient.gauge("freeSwapSpaceSize", unixOperatingSystem.getFreeSwapSpaceSize());
            statsDClient.gauge("maxFileDescriptorCount", unixOperatingSystem.getMaxFileDescriptorCount());
            statsDClient.gauge("openFileDescriptorCount", unixOperatingSystem.getOpenFileDescriptorCount());
            statsDClient.gauge("processCpuTime", unixOperatingSystem.getProcessCpuTime());
            statsDClient.gauge("totalPhysicalMemorySize", unixOperatingSystem.getTotalPhysicalMemorySize());
            statsDClient.gauge("totalSwapSpaceSize", unixOperatingSystem.getTotalSwapSpaceSize());
            statsDClient.gauge("availableProcessors", unixOperatingSystem.getAvailableProcessors());
            statsDClient.gauge("processCpuLoad", (long) (100 * unixOperatingSystem.getProcessCpuLoad()));
            statsDClient.gauge("systemCpuLoad", (long) (100 * unixOperatingSystem.getSystemCpuLoad()));
        }
    }

}
