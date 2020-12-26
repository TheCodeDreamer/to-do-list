package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ListOfTasks implements Serializable {
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
            sb.append("\n<div>").append("\n<a href=\"/todolist/").append(name).append("?id=").append(t.getId()).append("\">edit</a>")
                    .append("\n<a href=\"/todolist/").append(name).append("?id=").append(t.getId()).append("&done=").append("true").append("\">done</a>")
                    .append(t).append("\n</div>");
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
