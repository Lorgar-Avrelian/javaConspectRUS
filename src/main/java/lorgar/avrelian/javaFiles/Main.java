package lorgar.avrelian.javaFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        try {
            filesExample();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void filesExample() throws IOException {
        Path path = Path.of("books/covers/1.jpg");
        System.out.println(path);
        boolean b = Files.exists(path);
        System.out.println(Files.probeContentType(path));
    }
}
