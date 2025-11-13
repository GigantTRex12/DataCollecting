package Utils;

/**
 * String that is case-insensitive when compared with other NoCaseStrings
 */
public class NoCaseString {

    private final String string;

    public NoCaseString(String string) {
        this.string = string;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NoCaseString that = (NoCaseString) o;
        return this.string.equalsIgnoreCase(that.string);
    }

    @Override
    public int hashCode() {
        return string.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return string;
    }
}
