package com.lkc.tool;

import java.util.UUID;

public class UID {

    public static String getUid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
