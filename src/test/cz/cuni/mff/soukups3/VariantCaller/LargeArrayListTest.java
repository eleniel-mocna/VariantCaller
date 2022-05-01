package cz.cuni.mff.soukups3.VariantCaller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LargeArrayListTest {
    LargeArrayList<Character> arr;
    @BeforeEach
     void SetUp(){
        arr = new LargeArrayList<>();
    }
    @Test
    void size() {
        LargeArrayList<Integer> arrayList= new LargeArrayList<>(50L);
        assertEquals(0, arrayList.size(), "Empty list");
        for (int i = 0; i < 10; i++) {
            arrayList.add(i);
        }
        assertEquals(10, arrayList.size(), "Adding via members");
        arrayList.add(new Integer[]{0,1,2,3,4,5});
        assertEquals(16, arrayList.size(), "Adding via array");
        for (int i = 0; i < 50; i++) {
            arrayList.add(i);
        }
        assertEquals(66, arrayList.size(), "Adding over capacity");
    }
    @Test @Disabled
     void sizeBig(){
        for (int j=0; j<2; j++){
            for (long i = 0; i < Integer.MAX_VALUE-1000; i++) {
                arr.add('a');
            }
        }
        assertEquals(2*((long)Integer.MAX_VALUE), arr.size(), "Big Size");
    }
    @Test
    void addGet() {
        for (int i = 48; i < 48+20; i++) {
            arr.add((char)i);
        }
        for (int i = 0; i < 20; i++) {
            assertEquals((char) (48+i), arr.get(i), "Accessing added characters");
        }
        assertThrows(IndexOutOfBoundsException.class,()->arr.get(20), "Accessing out of range");
    }

}