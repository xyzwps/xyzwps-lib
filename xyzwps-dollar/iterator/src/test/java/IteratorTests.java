import com.xyzwps.lib.dollar.iterator.IteratorChainFactory;
import com.xyzwps.lib.dollar.iterator.IteratorMapEntryChainFactory;
import com.xyzwps.lib.dollar.testcases.ChainCases;
import org.junit.jupiter.api.Test;

public class IteratorTests {

    @Test
    void testIterator() {
        new ChainCases(IteratorChainFactory.INSTANCE, IteratorMapEntryChainFactory.INSTANCE).test();
    }
}

