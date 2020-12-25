package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Task {
    private int id;
    private String text;
    private int priority;
    private Date deadline;
    private Date dateOfCreation;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public Task(int id, String deadline, String text, String priority) {
        this.id = id;
        this.text = text;
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
    public Task(int id, String dateOfCreation, String deadline,String text,  String priority) {
        this.id = id;
        this.text = text;
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
        if(priority!=null)
            this.priority = Integer.parseInt(priority);
        else
            this.priority = 2;
        this.id =id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        if(deadline!=null)
        return "\n"+ text+"<br>\nDate of creation: " + formatter.format(dateOfCreation) + "<br>\n Deadline: " +formatter.format(deadline) + "<br>\npriority: " + priority;
        else
            return "\n"+ text+"\t" + formatter.format(dateOfCreation) + "\t" +"none" + "\t" + priority;
    }

    public String getString(){
        if(deadline!=null)
            return formatter.format(dateOfCreation)+ " " + formatter.format(deadline)+ " "+priority+  "\n"+ text+"\n";
        else
            return formatter.format(dateOfCreation)+ " " + "none "+priority+  "\n"+ text+"\n";
    }

    public int getPriority() {
        return priority;
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
