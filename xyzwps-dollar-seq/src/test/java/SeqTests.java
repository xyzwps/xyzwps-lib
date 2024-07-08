import com.xyzwps.lib.dollar.seq.SeqChainFactory;
import com.xyzwps.lib.dollar.seq.SeqMapEntryChainFactory;
import com.xyzwps.lib.dollar.testcases.ChainCases;
import org.junit.jupiter.api.Test;

public class SeqTests {
    @Test
    void testSeq() {
        new ChainCases(SeqChainFactory.INSTANCE, SeqMapEntryChainFactory.INSTANCE).test();
    }

}
