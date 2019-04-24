package com.diwayou.acm.net.pcap;

import com.sun.jna.Platform;
import org.pcap4j.core.*;
import org.pcap4j.util.NifSelector;

import java.io.IOException;

public class Loop {
    private static final String COUNT_KEY = Loop.class.getName() + ".count";
    private static final int COUNT = Integer.getInteger(COUNT_KEY, -1);

    private static final String READ_TIMEOUT_KEY = Loop.class.getName() + ".readTimeout";
    private static final int READ_TIMEOUT = Integer.getInteger(READ_TIMEOUT_KEY, 10); // [ms]

    private static final String SNAPLEN_KEY = Loop.class.getName() + ".snaplen";
    private static final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]

    private Loop() {}

    public static void main(String[] args) throws PcapNativeException, NotOpenException {
        String filter = "tcp port 443 and host 127.0.0.1";

        System.out.println(COUNT_KEY + ": " + COUNT);
        System.out.println(READ_TIMEOUT_KEY + ": " + READ_TIMEOUT);
        System.out.println(SNAPLEN_KEY + ": " + SNAPLEN);
        System.out.println("\n");

        PcapNetworkInterface nif;
        try {
            nif = new NifSelector().selectNetworkInterface();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (nif == null) {
            return;
        }

        System.out.println(nif.getName() + "(" + nif.getDescription() + ")");

        final PcapHandle handle = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);

        if (filter.length() != 0) {
            handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
        }

        PacketListener listener =
                packet -> {
                    System.out.println(handle.getTimestamp());
                    System.out.println(packet);
                };

        try {
            handle.loop(COUNT, listener);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PcapStat ps = handle.getStats();
        System.out.println("ps_recv: " + ps.getNumPacketsReceived());
        System.out.println("ps_drop: " + ps.getNumPacketsDropped());
        System.out.println("ps_ifdrop: " + ps.getNumPacketsDroppedByIf());
        if (Platform.isWindows()) {
            System.out.println("bs_capt: " + ps.getNumPacketsCaptured());
        }

        handle.close();
    }
}
