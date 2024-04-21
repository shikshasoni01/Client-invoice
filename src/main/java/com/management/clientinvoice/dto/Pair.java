package com.management.clientinvoice.dto;

import com.management.clientinvoice.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Pair<K, V> {

    private K key;
    private V value;

    public Pair(K key, V value, boolean appendQuotes) {
        setKey(key, appendQuotes);
        setValue(value, appendQuotes);
    }

    public void setKey(K key, boolean appendQuotes) {
        if (appendQuotes) {
            this.key = (K) (StringUtils.appendQuotes(key));
        } else {
            this.key = key;
        }
    }

    public void setValue(V value, boolean appendQuotes) {
        if (appendQuotes) {
            this.value = (V) (StringUtils.appendQuotes(value));
        } else {
            this.value = value;
        }
    }
}


