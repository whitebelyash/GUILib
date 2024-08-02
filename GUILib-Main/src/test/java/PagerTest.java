import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.whbex.lib.gui.util.pager.Pager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PagerTest {
    // TODO: Remove tests, they're useless here
    @Test
    public void testPager(){
        List<Integer> list = new ArrayList<>();
        final int listSize = 50;
        final int pageSize = 8;
        final int pageAmount = listSize / pageSize + 1;

        for(int i = 0; i < 50; i++){
            list.add(i);
        }
        Pager<Integer> pager = new Pager<>(list, pageSize);
        System.out.println("Checking page amount");
        assertEquals(pageAmount, pager.getPagesAmount());
        List<Integer> sub1 = pager.getPage(1);
        System.out.println("page 1 size: " + sub1.size());
        System.out.println("Contents: ");
        sub1.forEach(System.out::println);
        assertEquals(pageSize, sub1.size());
        List<Integer> sub1_actual = List.of(0, 1, 2, 3, 4, 5, 6, 7);
        compList(sub1_actual, sub1);

        List<Integer> sub2 = pager.getPage(2);
        System.out.println("page 2 size: " + sub2.size());
        assertEquals(pageSize, sub2.size());
        List<Integer> sub2_actual = List.of(8, 9, 10, 11, 12, 13, 14, 15);
        compList(sub2_actual, sub2);

    }
    private <T> void compList(List<T> expected, List<T> actual){
        for(int i = 0; i < expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
