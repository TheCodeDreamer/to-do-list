//package authorization;
//
//import main.ListOfTasks;
//import main.Task;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Scanner;
//
//public class abc {
//}
//
//package  main;
//
//        import java.io .*;
//        import java.util.ArrayList;
//        import java.util.HashMap;
//        import java.util.Map;
//        import java.util.Scanner;
//        import javax.servlet.*;
//        import javax.servlet.http.*;
//
//public class MainServlet extends HttpServlet {
//    private HashMap<String, ArrayList<Task>> lists = new HashMap<>();
//    private HashMap<String, ListOfTasks> lists2 = new HashMap<>();
//    private String name;
//
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html");
//        PrintWriter out = response.getWriter();
//        HttpSession session = request.getSession(false);
//        // request.getRequestDispatcher("main.html").include(request, response);
//        out.println("<!DOCTYPE html>\n" +
//                "<html>\n" +
//                "<head>\n" +
//                " <meta charset=\\\"UTF-8\\\">\n" +
//                "    <title>TO DO LIST</title>\n" +
//                "    </head>\n" +
//                "<body>\n" +
//                "<h1>TO DO LIST</h1>");
//        if (session != null) {
//            name = (String) session.getAttribute("name");
//            String text = request.getParameter("text");
//            String date = request.getParameter("calendar");
//            String num = request.getParameter("prior");
//            String nameOfList="";
//            if(lists.isEmpty()) {
//                readFromFile();
//            }
//            printWhenLogin(out,request,response);
//            for (Map.Entry<String,ArrayList<Task>> it: lists.entrySet()) {
//                nameOfList = it.getKey();
//                out.println("<a href=\"/todolist/"+ nameOfList  +"\">" + nameOfList + "</a>");
//            }
//            if(!lists.isEmpty()) {
//                for (Map.Entry<String, ArrayList<Task>> it : lists.entrySet()) {
//                    String uri = request.getRequestURI();
//                    nameOfList = it.getKey();
//                    if (uri.contains(nameOfList)) {
//                        ArrayList<Task> tasks = it.getValue();
//                        if (text != null) {
//                            if (date != null)
//                                tasks.add(new Task(nameOfList + tasks.size(), date, num, text));
//                            else
//                                tasks.add(new Task(nameOfList + tasks.size(), num, text));
//                        }
//                        for (Task t : tasks) {
//                            out.println("<div>");
//                            out.println(t);
//                            out.println("</div>");
//                        }
//                        session.setAttribute("tasks", tasks);
//                        printAddTask(out);
//                        break;
//                    }
//                }
//            }
//        } else {
//            printWhenLogout(out);
//        }
//
//    }
//
//    private void readFromFile2(){
//        try {
//            Scanner scanner = new Scanner(new File("C:\\Users\\Дмитрий\\IdeaProjects\\todolist\\src\\main\\resources\\" + name + ".txt"));
//            while (scanner.hasNext()) {
//                String line = scanner.nextLine();
//                String[] s = line.split(" ");
//                String text = scanner.nextLine();
//                String nameOfList = s[0];
//                ListOfTasks list ;
//                if (lists2.containsKey(nameOfList)) {
//                    list = lists2.get(nameOfList);
//                    list.add(new Task(nameOfList + list.size(), s[1], s[2], s[3], text));
//                } else {
//                    list = new ListOfTasks(nameOfList);
//                    list.add(new Task(nameOfList + 0, s[1], s[2], s[3], text));
//
//                }
//                lists2.put(nameOfList, list);
//            }
//            scanner.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//    private void readFromFile(){
//        try {
//            Scanner scanner = new Scanner(new File("C:\\Users\\Дмитрий\\IdeaProjects\\todolist\\src\\main\\resources\\" + name + ".txt"));
//            while (scanner.hasNext()) {
//                String line = scanner.nextLine();
//                String[] s = line.split(" ");
//                String text = scanner.nextLine();
//                String nameOfList = s[0];
//                ArrayList<Task> tasks;
//                if (lists.containsKey(nameOfList)) {
//                    tasks = lists.get(nameOfList);
//                    tasks.add(new Task(nameOfList + tasks.size(), s[1], s[2], s[3], text));
//                } else {
//                    tasks = new ArrayList<>();
//                    tasks.add(new Task(nameOfList + 0, s[1], s[2], s[3], text));
//
//                }
//                lists.put(nameOfList, tasks);
//            }
//            scanner.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//    private  void printWhenLogin(PrintWriter out,HttpServletRequest request,HttpServletResponse response ) throws ServletException, IOException {
//        out.println("<form action=\"LogoutServlet\">\n" +
//                " <button class=\"login\">Logout</button>\n" +
//                "</form>");
//
//    }
//
//    private  void printWhenLogout(PrintWriter out){
//        out.println("<form action=\"/login.html\">\n" +
//                " <button class=\"login\">Login</button>\n" +
//                "</form>");
//        out.println("</body>\n</html>");
//        if(name !=null)
//            saveToFile();
//        lists.clear();
//    }
//    private  void printAddTask(PrintWriter out){
//        out.println("<form action=\"\" method=\"get\">\n" +
//                "    Input new task:<input type=\"text\" name=\"text\">\n" +
//                "    Date:<input type=\"date\" name=\"calendar\">\n" +
//                "    <input name=\"prior\" value=\"1\" type=\"radio\" > 1\n" +
//                "    <input name=\"prior\" value=\"2\" type=\"radio\" >2\n" +
//                "    <input name=\"prior\"value=\"3\" type=\"radio\"> 3\n" +
//                "    <input type=\"submit\" value=\"add task\">\n" +
//                "</form>");
//    }
//    public void saveToFile() {
//        FileWriter writer = null;
//        try {
//            writer = new FileWriter("C:\\Users\\Дмитрий\\IdeaProjects\\todolist\\src\\main\\resources\\" + name + ".txt",false);
//            for (Map.Entry<String, ArrayList<Task>> it : lists.entrySet()) {
//                ArrayList<Task> tasks =it.getValue();
//                String nameOfList =it.getKey();
//                for (Task t :tasks) {
//                    writer.write(nameOfList+ " " + t.getString());
//                }
//            }
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}