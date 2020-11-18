package com.nxtlife.efkon.enforcementconfigurator.view;

import com.nxtlife.efkon.enforcementconfigurator.util.LongObfuscator;

public interface Response {

    default Long mask(final Long number) {
        return number != null ? LongObfuscator.INSTANCE.obfuscate(number) : null;
    }
}
