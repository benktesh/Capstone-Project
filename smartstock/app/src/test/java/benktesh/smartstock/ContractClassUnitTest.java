package benktesh.smartstock;



import android.provider.BaseColumns;



import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import benktesh.smartstock.Data.Contract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ContractClassUnitTest {

    @Test
    public void inner_class_exists() throws Exception {
        Class[] innerClasses = Contract.class.getDeclaredClasses();
        assertEquals("There should be 1 Inner class inside the contract class", 2, innerClasses.length);
    }

    @Test
    public void inner_class_type_correct() throws Exception {
        Class[] innerClasses = Contract.class.getDeclaredClasses();
        assertEquals("Cannot find inner class to complete unit test", 2, innerClasses.length);
        Class entryClass = innerClasses[0];
        assertTrue("Inner class should implement the BaseColumns interface", BaseColumns.class.isAssignableFrom(entryClass));
        assertTrue("Inner class should be final", Modifier.isFinal(entryClass.getModifiers()));
        assertTrue("Inner class should be static", Modifier.isStatic(entryClass.getModifiers()));
    }



}