package rise.myapplication.Util;

/**
 * Created by newcomputer on 17/11/15.
 */
import java.util.ArrayList;
import java.util.List;

public class Pool<T> {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public interface ObjectFactory<T> {
        T createObject();
    }
    private final ObjectFactory<T> mFactory;
    private final List<T> mPool;
    private final int mMaxPoolSize;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public Pool(ObjectFactory<T> factory, int maxPoolSize) {
        mFactory = factory;
        mMaxPoolSize = maxPoolSize;
        mPool = new ArrayList<>(mMaxPoolSize);
    }

    public T get() {
        T object = null;

        if (mPool.isEmpty())
            object = mFactory.createObject();
        else
            object = mPool.remove(mPool.size() - 1);

        return object;
    }

    public void add(T object) {
        if (mPool.size() < mMaxPoolSize)
            mPool.add(object);
    }
}
