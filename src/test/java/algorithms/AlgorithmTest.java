package algorithms;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlgorithmTest {

    private Algorithm alg;
    private String testAlgName;

    @BeforeEach
    void initTest () {
        alg = new Algorithm();
        testAlgName = Huffman.name();
    }

    @Test
    void checkAlgoName() {
        Assert.assertTrue(alg.checkAlgoName(testAlgName));
        Assert.assertFalse(alg.checkAlgoName("test"));
    }

    @Test
    void allAlgorithms() {
        String[] expected = { testAlgName };
        Assert.assertArrayEquals(alg.allAlgorithms(), expected);
    }

    @Test
    void selectAlgo() {
        Assert.assertNotNull(alg.selectAlgo(testAlgName).encode("hello"));
    }
}