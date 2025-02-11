package dev.jlipka.swiftrestapi.mapper;

import java.io.File;
import java.util.Optional;

public interface FileReader {
    public Optional<File> read(String filename);
}
