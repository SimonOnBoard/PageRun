package starter;

import java.util.Objects;

public class Pair<T, T1> {
    public T key;
    public T1 value;

    public Pair(T key, T1 value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) &&
                Objects.equals(value, pair.value)
                ||
                Objects.equals(key, pair.value) &&
                        Objects.equals(value, pair.key);
    }
}
