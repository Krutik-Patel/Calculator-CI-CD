import com.calculator.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;
import java.lang.Math;

public class CalculatorTest {
    private Calculator calculator;

    @Before
    public void setUp() {
    }

    @Test
    public void testSqRoot() {
        int num = 4;
        double expected = 2.0;
        Assert.assertEquals(expected, Calculator.sqRoot(num), 0.0001);
    }

    @Test
    public void testFactorial() {
        int num = 5;
        double expected = 120.0;
        Assert.assertEquals(expected, Calculator.factorial(num), 0.0001);
    }

    @Test
    public void testNatLog() {
        int num = 5;
        Assert.assertEquals(Math.log(num), Calculator.natLog(num), 0.0001);
    }

    @Test
    public void testPower() {
        int base = 3;
        int exp = 2;
        double expected = 9.0;
        Assert.assertEquals(expected, Calculator.power(base, exp), 0.0001); 
    }
}
