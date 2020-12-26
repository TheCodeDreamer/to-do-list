package  main;

import java.io .*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.*;
import javax.servlet.http.*;

public class MainServlet extends HttpServlet {
    private HashMap<String, ListOfTasks> lists = new HashMap<>();
    private String nameOfUser;


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        printMain(out);
        if (session != null) {
            String name = (String) session.getAttribute("name");
            if(!name.equals(nameOfUser))
                nameOfUser=name;
            String text = request.getParameter("text");
            String date = request.getParameter("calendar");
            String num = request.getParameter("prior");
            String sort = request.getParameter("sort");
            String nameNewList = request.getParameter("newlist");
            String nameOfList="";
            if(lists.isEmpty()) {
                readFromFile();
            }
            if(nameNewList!= null){
                ListOfTasks list = new ListOfTasks(nameNewList);
                lists.put(nameNewList,list);
            }
            printWhenLogin(out,request,response);
            out.println("<div>");
            out.println("<form action=\"?\" method=\"get\">\n" +
                    "    new list:<input type=\"text\" name=\"newlist\">\n" +
                    "    <input type=\"submit\" value=\"create\">\n" +
                    "</form>");
            for (Map.Entry<String,ListOfTasks> it: lists.entrySet()) {
                 nameOfList = it.getKey();
                out.println("<a href=\"/todolist/"+ nameOfList  +"\">" + nameOfList + "</a>");
            }
            out.println("</div>");
            if(!lists.isEmpty()) {
                for (Map.Entry<String, ListOfTasks> it : lists.entrySet()) {
                    String uri = request.getRequestURI();
                    nameOfList = it.getKey();
                    if (uri.contains(nameOfList)) {
                        printSort(out);
                        ListOfTasks list = it.getValue();
                        if (text != null) {
                            if (date != null || !date.isEmpty())
                                list.add(new Task(list.size(), date, text, num));
                            else
                                list.add(new Task( list.size(), text, num));
                        }
                        if(sort !=null){
                            list.sort(sort);
                        }
                        out.println(list);
                        printAddTask(out);
                        break;
                    }
                }
            }
        } else {
           printWhenLogout(out);
           nameOfUser=null;
        }

    }

    @Override
    public void destroy() {
        if(nameOfUser !=null)
            saveToFile();
    }

    private void readFromFile(){
        try {
            Scanner scanner = new Scanner(new File("C:\\Users\\Дмитрий\\IdeaProjects\\todolist\\src\\main\\resources\\" + nameOfUser + ".txt"));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] s = line.split(" ");
                String text = scanner.nextLine();
                String nameOfList = s[0];
                ListOfTasks list;
                if (lists.containsKey(nameOfList)) {
                    list = lists.get(nameOfList);
                    list.add(new Task(list.size(), s[1], s[2], text, s[3]));
                } else {
                    list = new ListOfTasks(nameOfList);
                    list.add(new Task(list.size(), s[1], s[2], text, s[3]));

                }
                lists.put(nameOfList, list);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void printMain(PrintWriter out){
        out.println("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                " <meta charset=\\\"UTF-8\\\">\n" +
                "    <title>TO DO LIST</title>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"../main.css\">"+
                "    \n</head>\n" +
                "<body>\n" +
                "<h1>TO DO LIST</h1>");
    }
    private  void printWhenLogin(PrintWriter out,HttpServletRequest request,HttpServletResponse response ) throws ServletException, IOException {
        out.println("<form action=\"LogoutServlet\">\n" +
                " <button class=\"login\">Logout</button>\n" +
                "</form>");

    }
    private void printSort(PrintWriter out){
        out.println("<form method=\"get\" action=\"?\">\n" +
                "    <select  name=\"sort\">\n" +
                "        <option value=\"deadline\">deadline</option>\n" +
                "        <option value=\"priority\">priority</option>\n" +
                "    </select>\n" +
                "    <input type=\"submit\" value=\"sort\">\n" +
                "</form>");
    }
    private  void printWhenLogout(PrintWriter out){
        out.println("<form action=\"/login.html\">\n" +
                " <button class=\"login\">Login</button>\n" +
                "</form>");
        out.println("</body>\n</html>");
        if(nameOfUser !=null)
        saveToFile();
        lists.clear();
    }
    private  void printAddTask(PrintWriter out){
        out.println("<form action=\"?\" method=\"get\">\n" +
                "    Input new task:<input type=\"text\" name=\"text\">\n" +
                "    Deadline:<input type=\"date\" name=\"calendar\">\n" +
                "   Priority:<input name=\"prior\" value=\"1\" type=\"radio\" > 1\n" +
                "    <input name=\"prior\" value=\"2\" type=\"radio\" >2\n" +
                "    <input name=\"prior\"value=\"3\" type=\"radio\"> 3\n" +
                "    <input type=\"submit\" value=\"add task\">\n" +
                "</form>");
    }
    public void saveToFile() {
        FileWriter writer;
        try {
            writer = new FileWriter("C:\\Users\\Дмитрий\\IdeaProjects\\todolist\\src\\main\\resources\\" + nameOfUser + ".txt",false);
            for (Map.Entry<String, ListOfTasks> it : lists.entrySet()) {
               ListOfTasks list =it.getValue();
               writer.write(list.getString());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}