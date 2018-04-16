package de.hpi.cache.dto;

import java.io.Serializable;
public class Property<T> implements Serializable {
    private static final long serialVersionUID = -475706876964362699L;
    private T value;
    public Property() {
    }
    public Property(T value) {
        this.value = value;
    }
    public T getValue() {
        return this.value;
    }
    public void setValue(T value) {
        this.value = value;
    }
    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.value == null ? 0 : this.value.hashCode());
        return result;
    }
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Property<?> other = (Property)obj;
            if (this.value == null) {
                if (other.value != null) {
                    return false;
                }
            } else if (!this.value.equals(other.value)) {
                return false;
            }
            return true;
        }
    }
    public String toString() {
        StringBuilder builder = new StringBuilder(17);
        builder.append("Property [value=");
        builder.append(this.value);
        builder.append(']');
        return builder.toString();
    }
}