package learn.hypergroovy.coffeemachine.p1;

import learn.hypergroovy.testutils.AbstractMainTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CoffeeMachineTest extends AbstractMainTest {

    @Test
    void main() throws IOException {
        Result result = super.runMainWithTestOutput(CoffeeMachine::main, null,
                "p1-expected-output.txt");
        assertEquals(result.expected(), result.actual());
    }
}
