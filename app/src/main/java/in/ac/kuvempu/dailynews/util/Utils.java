package in.ac.kuvempu.dailynews.util;

/**
 * Created by raghav on 4/6/2017.
 */

public class Utils {

    public enum CATEGORIES {
        General ("General"),
        Economics ("Economics"),
        Technology ("Technology"),
        Sports ("Sports"),
        MyNews ("News Reported by Me");

        private final String name;

        private CATEGORIES(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }
}
