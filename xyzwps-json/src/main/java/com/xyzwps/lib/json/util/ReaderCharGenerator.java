package com.xyzwps.lib.json.util;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;

class ReaderCharGenerator implements CharGenerator {

    private final Reader reader;

    private boolean hasBeenRead = true;
    private int current = 0;

    ReaderCharGenerator(Reader reader) {
        this.reader = reader;
    }

    @Override
    public boolean hasNext() {
        try {
            if (hasBeenRead) {
                hasBeenRead = false;
                current = reader.read();
            }
            return current != -1;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public char next() {
        if (hasBeenRead) {
            throw new IllegalStateException("You should call hasNext first");
        }
        this.hasBeenRead = true;
        return (char) current;
    }

    @Override
    public char seek() {
        if (hasBeenRead) {
            throw new IllegalStateException("You should call hasNext first");
        }
        return (char) current;
    }
}
