package com.mallowwww.realnpc.util;

import net.minecraft.client.Minecraft;

public class WorldConsoleTextHandler {
    private static final String username = Minecraft.getInstance().getUser().getName();
    private static final String fakeIP = Integer.toHexString(username.hashCode() * 7919 * 7873 * 7727).substring(0, 8);

    private static final String[] LINES = new String[] {
            "[INFO] Attempting connection...",
            "[INFO] ONET connection received; assigned address " + username+":"+fakeIP,
            "[INFO] Connection established! Awaiting instructions...",
            """
------------[%gONET%w]------------
Welcome to ONET!
Connected as: %c\\%w
MOTD: %rchangeme
Server: %rchangeme""",
            "[INFO] Receiving instructions, please hold.",
            "",
            "",
            "",
            "[INFO] Downloaded instructions.",
            "[%yWARN%w] Possible corrupted data, filtering...",
            "",
            "[%rERROR%w] %rAttempting to resolve instruction conflict",
            "[INFO] Found world to connect to.",
            "[INFO] Loading into world..."
    };
    private int index = 0;
    public WorldConsoleTextHandler() {
    }
    public void reset() {
        index = 0;
    }
    public String[] next() {
        if (index < LINES.length)
            return LINES[index++].split("\n");
        return new String[] {};
    }

}
