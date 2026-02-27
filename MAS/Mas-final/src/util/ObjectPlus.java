package util;

import java.io.*;
import java.util.*;

public class ObjectPlus implements Serializable {
    private static Map<Class, List> map = new HashMap<>();
    public static final String file = "file.txt";

    public ObjectPlus() {
        register();
    }

    public static void loadExtent() throws IOException, ClassNotFoundException {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            map = (Map<Class, List>) ois.readObject();
        }
    }

    public static <T> List<T> getExtentFromClass(Class<T> c) {
        map.computeIfAbsent(c, _ -> new ArrayList<>());
        List<T> list = (List<T>) map.get(c);
        return Collections.unmodifiableList(list);
    }

    public static void saveExtent() throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            oos.writeObject(map);
        }
    }


    public void register() {
        List list = map.computeIfAbsent(this.getClass(), _ -> new ArrayList<>());
        list.add(this);
    }

    public void removeFromExtent() {
        List list = map.get(this.getClass());
        if(list != null) {
            list.remove(this);
        }
    }



}

