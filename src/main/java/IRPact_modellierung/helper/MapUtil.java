//taken from http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java

package IRPact_modellierung.helper;

import org.apache.logging.log4j.LogManager;

import java.util.*;

public class MapUtil {

    private static final org.apache.logging.log4j.Logger fooLog = LogManager.getLogger("debugConsoleLogger");

    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {

            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
            fooLog.debug("putting pair {}/{}", entry.getKey().toString(), entry.getValue());
        }
        return result;
    }

    public static <K, V extends Comparable<? super V>> LinkedHashMap<K, V>
    sortKeysbyValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {

            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        LinkedHashMap<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
            fooLog.debug("putting pair {}/{}", entry.getKey().toString(), entry.getValue());
        }
        return result;
    }
}