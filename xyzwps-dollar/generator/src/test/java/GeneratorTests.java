import com.xyzwps.lib.dollar.generator.GeneratorChainFactory;
import com.xyzwps.lib.dollar.generator.GeneratorMapEntryChainFactory;
import com.xyzwps.lib.dollar.testcases.ChainCases;
import org.junit.jupiter.api.Test;

class GeneratorTests {

    @Test
    void test() {
        new ChainCases(GeneratorChainFactory.INSTANCE, GeneratorMapEntryChainFactory.INSTANCE).test();
    }
}
