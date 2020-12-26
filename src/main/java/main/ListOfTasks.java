package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ListOfTasks implements Serializable {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListOfTasks list = (ListOfTasks) o;
        return Objects.equals(name, list.name) && Objects.equals(tasks, list.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tasks);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private ArrayList<Task> tasks;

    public ListOfTasks(String name){
        this.name=name;
        tasks=new ArrayList<>();
    }
    public void add (Task task){
        tasks.add(task);
    }
    public void remove (Task task) {
        tasks.remove(task);
    }
    public int size(){
        return tasks.size();
    }

    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder();
        for (Task t :tasks) {
            if(!t.isDone())
            sb.append("\n<tr>").append(t).append("\n<td><a  href=\"/todolist/").append(name).append("?id=").append(t.getId()).append("\" class=\"edit\" >edit</a>")
                    .append("\n<a href=\"/todolist/").append(name).append("?id=").append(t.getId()).append("&done=").append("true").append("\" class=\"edit\" >done</a>").append("</td>\n</tr>\n");
            else
                sb.append("<tr>").append(t).append("\n</tr>\n");
        }
        return  sb.toString();
    }
    public String getString() {
        StringBuilder sb = new StringBuilder();
        for (Task t :tasks) {
            sb.append(name).append(" ").append(t.getString());
        }
        return  sb.toString();
    }

    public Task getTask(int id) {
        return tasks.stream().filter(task -> task.getId() == id).findFirst().get();
    }

    public void sort(String nameOfSort){
        if (nameOfSort.equals("deadline")) {
            tasks.sort(Task.deadlineComparator);
        }
        else if(nameOfSort.equals("priority"))
            tasks.sort(Task.priorComparator);
    }
}
