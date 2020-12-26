package  main;

import java.io .*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.*;
import javax.servlet.http.*;

public class MainServlet extends HttpServlet {
    private final HashMap<String, ListOfTasks> lists = new HashMap<>();
    private String nameOfUser;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        String name = null;
        if (session != null) {
            name = (String) session.getAttribute("name");
        }
        printMain(out);
        if (name != null) {
            if(!name.equals(nameOfUser))
                nameOfUser=name;

            ListOfTasks fromEdit = (ListOfTasks) request.getAttribute("list");
            if (fromEdit != null) {
                lists.put(fromEdit.getName(), fromEdit);
            }
            String text = request.getParameter("text");
            String id = request.getParameter("id");
            String date = request.getParameter("calendar");
            String num = request.getParameter("prior");
            String sort = request.getParameter("sort");
            String nameNewList = request.getParameter("newlist");
            String done = request.getParameter("done");
            String nameOfList;

            if(lists.isEmpty()) {
                readFromFile();
            }
            if(nameNewList!= null){
                ListOfTasks list = new ListOfTasks(nameNewList);
                lists.put(nameNewList,list);
            }
            printWhenLogin(out);
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
                        if (id != null && done == null) {
                            request.setAttribute("list", list);
                            request.setAttribute("id", id);
                            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/EditServlet");
                            requestDispatcher.forward(request, response);
                        }
                        if (id != null && done != null) {
                            Task task = list.getTask(Integer.parseInt(id));
                            task.setDone(true);
                            lists.get("archive").add(task);
                            list.remove(task);
                        }
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ListOfTasks list = lists.get(req.getParameter("listName"));
        Task task = list.getTask(Integer.parseInt(req.getParameter("id")));

        String deadline = req.getParameter("deadline");
        String priority = req.getParameter("priority");
        String text = req.getParameter("text");

        if (deadline != null) {
            try {
                task.setDeadline(formatter.parse(deadline));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        task.setPriority(Integer.parseInt(priority));
        task.setText(text);

        this.doGet(req, resp);

    }

    @Override
    public void destroy() {
        if(nameOfUser !=null)
            saveToFile();
    }

    private void readFromFile(){
        try {
            Scanner scanner = new Scanner(new File("/home/kirill/IdeaProjects/to-do-list/src/main/resources/" + nameOfUser + ".txt"));
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

    private void printMain(PrintWriter out){
        out.println("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                " <meta charset=\\\"UTF-8\\\">\n" +
                "    <title>TO DO LIST</title>\n" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"/main.css\">"+
                "    </head>\n" +
                "<body>\n" +
                "<h1>TO DO LIST</h1>");
    }
    private  void printWhenLogin(PrintWriter out) {
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