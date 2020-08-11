package org.domainspecific.dsl.trading.external;

import java.util.ArrayList;
import java.util.List;

public class ParserArgs<T> {
    public List<String> buffer = new ArrayList<>();
    String keyWord = "";
    public T value;
    public boolean completed;

    public void reset() {
        buffer.clear();
        keyWord = "";
    }

}
