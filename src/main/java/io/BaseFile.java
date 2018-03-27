package io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import algorithms.IAlgorithm;

public class BaseFile {
    public static String readFile (String filePath) throws IOException
    {
        StringBuilder contentBuilder = new StringBuilder();
        Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8);
        stream.forEach(s -> contentBuilder.append(s).append("\n"));
        return contentBuilder.toString();
    }

    public static void writeToFile (String filePath, String bytes) throws IOException {
        Files.write(Paths.get(filePath), bytes.getBytes());
    }

    public static void encodeFile (String filePath, IAlgorithm alg) throws IOException {
        String data = readFile(filePath);
        String encoded = alg.encode(data);
        writeToFile(filePath + "_encoded", encoded);
    }

    public static void decodeFile (String filePath, IAlgorithm alg) throws IOException {
        String encoded = readFile(filePath);
        String decoded = alg.decode(encoded);
        writeToFile(filePath + "_decoded", decoded);
    }
}

