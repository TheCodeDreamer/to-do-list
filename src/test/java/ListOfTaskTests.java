import main.ListOfTasks;
import main.Task;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ListOfTaskTests {

    private final static HashMap<String, ListOfTasks> lists = new HashMap<>();

    @BeforeAll
    static void readFromFile() {
        try {
            Scanner scanner = new Scanner(new File("C:\\Users\\Дмитрий\\IdeaProjects\\todolist\\src\\main\\resources\\d.txt"));
            lists.put("archive", new ListOfTasks("archive"));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] s = line.split(" ");
                String text = scanner.nextLine();
                String nameOfList = s[0];
                ListOfTasks list;
                if (lists.containsKey(nameOfList)) {
                    list = lists.get(nameOfList);
                } else {
                    list = new ListOfTasks(nameOfList);
                }
                Task task = new Task(list.size(), s[1], s[2], Boolean.parseBoolean(s[3]), text, s[4]);
                if (!task.isDone()) {
                    list.add(task);
                    lists.put(nameOfList, list);
                } else {
                    lists.get("archive").add(task);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readFromFileTest() {
        assertEquals(3, lists.get("Study").size());
    }

    @Test
    public void testSort() {
        ListOfTasks list = lists.get("Study");
        ListOfTasks list2 = new ListOfTasks("Study");
        list2.add(new Task(0,"2020-12-26", "2020-12-30", false,"lab", "3"));
        list2.add(new Task(2,"2020-12-26", "2020-12-09", false,"exam", "2"));
        list2.add(new Task(1,"2020-12-26", "none", false,"kursovaya", "1"));
        list.sort("priority");
        assertEquals(list,list2);
    }

    @Test
    public void getTaskByIdTest(){
        ListOfTasks list = lists.get("Study");
        Task actual = list.getTask(1);
        Task expected = new Task(1,"2020-12-26", "none", false,"kursovaya", "1");
        assertEquals(expected,actual);
    }

}
