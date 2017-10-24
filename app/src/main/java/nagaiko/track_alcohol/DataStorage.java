package nagaiko.track_alcohol;


/**
 * Created by altair on 24.10.17.
 */

public class DataStorage {
    private static DataStorage _instance = null;

    public static DataStorage getInstance() {
        if (_instance == null) {
            _instance = new DataStorage();
        }
        return _instance;
    }

    public static final int COCKTAIL_FILTERED_LIST = 0;
    private static final int ARRAY_SIZE = 1;

    public Object[] data;

    public DataStorage() {
        this.data = new Object[ARRAY_SIZE];
    }

    public void setData(int position, Object data) {
        if (position >= ARRAY_SIZE) {
            throw new IndexOutOfBoundsException("position must be one of list?");
        }
        synchronized(this) {
            this.data[position] = data;
        }
    }

    public Object getData(int position) {
        if (position >= ARRAY_SIZE) {
            throw new IndexOutOfBoundsException("position must be one of list?");
        }
        synchronized (this) {
            return this.data[position];
        }
    }
}
