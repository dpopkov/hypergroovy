package learn.hypergroovy.testutils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

/**
 * Base class for tests that check input-output of simple console programs.
 */
public abstract class AbstractMainTest {
    protected InputStream savedIn;
    protected PrintStream savedOut;
    protected ByteArrayOutputStream buffer;

    /**
     * Should be called as a setup method using @BeforeEach annotation
     */
    @BeforeEach
    protected void setupTest() {
        savedIn = System.in;
        savedOut = System.out;
        buffer = new ByteArrayOutputStream();
    }

    /**
     * Runs the specified main method reference using test data from the specified input and expected output filenames.
     * Test data files must be located in the test resources folder.
     */
    protected Result runMainWithTestInputOutput(Consumer<String[]> mainMethodReference,
                                                String[] mainMethodArgs,
                                                String inputDataFilename,
                                                String expectedOutputDataFilename) throws IOException {
        String expected;
        try (
                InputStream input = getResourceForFilename(inputDataFilename);
                InputStream expectedOutput = getResourceForFilename(expectedOutputDataFilename)
        ) {
            System.setIn(input);
            System.setOut(new PrintStream(buffer));

            if (mainMethodArgs == null) {
                mainMethodArgs = new String[0];
            }
            mainMethodReference.accept(mainMethodArgs);

            expected = new String(expectedOutput.readAllBytes());
        }
        String actual = buffer.toString();
        return new Result(expected, actual);
    }

    /**
     * Runs the specified main method reference using test data from the specified expected output filename.
     * Test data file must be located in the test resources folder.
     */
    protected Result runMainWithTestOutput(Consumer<String[]> mainMethodReference,
                                           String[] mainMethodArgs,
                                           String expectedOutputDataFilename) throws IOException {
        String expected;
        try (
                InputStream expectedOutput = getResourceForFilename(expectedOutputDataFilename)
        ) {
            System.setOut(new PrintStream(buffer));

            if (mainMethodArgs == null) {
                mainMethodArgs = new String[0];
            }
            mainMethodReference.accept(mainMethodArgs);

            expected = new String(expectedOutput.readAllBytes());
        }
        String actual = buffer.toString();
        return new Result(expected, actual);
    }

    private InputStream getResourceForFilename(String resourceFilename) {
        return this.getClass().getClassLoader().getResourceAsStream(resourceFilename);
    }

    /**
     * Should be called as a tearDown method using @AfterEach annotation.
     */
    @AfterEach
    protected void tearDownTest() {
        System.setIn(savedIn);
        System.setOut(savedOut);
    }

    public static class Result {
        private final String expected;
        private final String actual;

        public Result(String expected, String actual) {
            this.expected = expected;
            this.actual = actual;
        }

        public String expected() {
            return expected;
        }

        public String actual() {
            return actual;
        }
    }
}
