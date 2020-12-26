package main;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class Task implements Serializable {
    private int id;

    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && priority == task.priority && isDone == task.isDone && Objects.equals(text, task.text) && Objects.equals(deadline, task.deadline) && Objects.equals(dateOfCreation, task.dateOfCreation) && Objects.equals(formatter, task.formatter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, priority, deadline, dateOfCreation, isDone, formatter);
    }

    private int priority;
    private Date deadline;
    private Date dateOfCreation;
    private boolean isDone;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public Task(int id, String deadline, String text, String priority) {
        this.id = id;
        this.text = text;
        this.isDone = false;
        if(priority!=null)
            this.priority = Integer.parseInt(priority);
        else
            this.priority = 2;
        this.dateOfCreation = new Date();
        try {
            if(!deadline.isEmpty())
            this.deadline =formatter.parse(deadline);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Task(int id, String dateOfCreation, String deadline, boolean isDone, String text,  String priority) {
        this.id = id;
        this.text = text;
        this.isDone = isDone;
        this.priority = Integer.parseInt(priority);
        try {
            if(!deadline.equals("none"))
            this.deadline =formatter.parse(deadline);
            this.dateOfCreation = formatter.parse(dateOfCreation);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Task(int id, String priority, String text) {
        this.text=text;
        this.isDone = false;
        if(priority!=null)
            this.priority = Integer.parseInt(priority);
        else
            this.priority = 2;
        this.id =id;
    }
    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        if(deadline!=null)
        return "\n<th scope=\"row\">"+ text+"</th>\n<td>" + formatter.format(dateOfCreation) + "</td>\n<td>" +formatter.format(deadline) + "</td>\n<td>" + priority+"</td>\n";
        else
            return "\n<th scope=\"row\">"+ text+"</th>\n<td>" + formatter.format(dateOfCreation) + "</td>\n<td>None</td>\n<td>" + priority+"</td>\n";
    }

    public String getString(){
        if(deadline!=null)
            return formatter.format(dateOfCreation)+ " " + formatter.format(deadline)+ " " + isDone + " " + priority +  "\n" + text + "\n";
        else
            return formatter.format(dateOfCreation)+ " " + "none " + isDone + " " +priority+  "\n"+ text+"\n";
    }

    public int getPriority() {
        return priority;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getDeadline() {
        return deadline;
    }

    public static Comparator<Task> deadlineComparator = new Comparator<Task>() {
        public int compare(Task t1, Task t2) {
            Date d1 = t1.deadline;
            Date d2 = t2.deadline;
            if(d1==null && d2==null)
                return 0;
            if(d1 == null)
                return 1;
            if(d2 == null)
                return -1;
            return d1.compareTo(d2);
        }};
    public static Comparator<Task> priorComparator = new Comparator<Task>() {
        public int compare(Task t1, Task t2) {
            Integer d1 = t1.priority;
            Integer  d2 = t2.priority;
            return d2.compareTo(d1);
        }};

}
