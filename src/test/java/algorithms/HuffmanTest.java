package algorithms;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HuffmanTest {

    private String text;
    private String endcodedText;

    @BeforeEach
    public void initTest () {
        text = "hello";
        endcodedText = "  36e       1h       1l       2o       11011000111";
    }


    @Test
    void encode() {
        Assert.assertEquals(new Huffman().encode(text), endcodedText);
    }

    @Test
    void decode() {
        Assert.assertEquals(new Huffman().decode(endcodedText), text);
    }

    @Test
    void name () {
        Assert.assertEquals(Huffman.name(), "huffman");
    }
}