package in.ac.kuvempu.dailynews.util;

import java.util.HashMap;

/**
 * Created by raghav on 4/8/2017.
 */

public class Context {

    private static HashMap<String, Object> mContext;
    private static Context instance;

    private Context(){
        mContext = new HashMap<>();
    }

    public static Context getInstance(){

        if(instance == null){
            instance = new Context();
            return instance;
        }
        return instance;
    }

    public static void add(String key, Object object){

        mContext.put(key, object);
    }

    public static Object get(String key){

        return mContext.get(key);
    }

    public static void remvoe(String key){

        mContext.remove(key);
    }

    public static void clear(){

        mContext.clear();
    }
}