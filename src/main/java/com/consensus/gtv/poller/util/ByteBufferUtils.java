package com.consensus.gtv.poller.util;

import lombok.experimental.UtilityClass;

import java.nio.ByteBuffer;

import static java.nio.charset.StandardCharsets.UTF_8;

@UtilityClass
public class ByteBufferUtils {

    public static ByteBuffer toByteBuffer(String msg){
        return ByteBuffer.wrap(msg.getBytes(UTF_8));
    }

    public static String toString(ByteBuffer buffer){
        byte[] bytes;
        if(buffer.hasArray()) {
            bytes = buffer.array();
        } else {
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
        }
        return new String(bytes, UTF_8);
    }
}
