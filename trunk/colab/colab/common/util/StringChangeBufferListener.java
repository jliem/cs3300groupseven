package colab.common.util;

public interface StringChangeBufferListener {

    void insert(int offset, String str);

    void delete(int offset, int length);

}
