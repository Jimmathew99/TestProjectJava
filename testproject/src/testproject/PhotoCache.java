package testproject;
import java.util.HashMap;
import java.util.Map;

public class PhotoCache {
    private final Map<String, String> cache = new HashMap<>();
    private final Map<String, Long> timestamps = new HashMap<>();
    private static final long CACHE_DURATION_MS = 10 * 60 * 1000; 
    private static final long PHOTO_DURATION_MS = 24 * 60 * 60 * 1000; // 24 hours

    public String get(String hexCode) {
        Long timestamp = timestamps.get(hexCode);
        if (timestamp == null || System.currentTimeMillis() - timestamp > CACHE_DURATION_MS) {
            return null;
        }
        return cache.get(hexCode);
    }

    public void put(String hexCode, String photoUrl) {
        cache.put(hexCode, photoUrl);
        timestamps.put(hexCode, System.currentTimeMillis());
    }
}
