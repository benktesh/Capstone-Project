package benktesh.smartstock.Utils;

import java.util.List;

public class LibraryHelper {

    /*
    * Method trims a list to new size
    */
    public static void Trim(List list, int newSize) {
        int currentSize = list == null ? 0 : list.size();
        if(currentSize < newSize) {
            return;
        }
        for (int i = newSize; i < currentSize; i++) {
            list.remove(list.size() - 1);
        }
    }
}
