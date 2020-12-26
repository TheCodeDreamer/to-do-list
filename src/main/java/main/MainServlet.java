package  main;

import java.io .*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.*;
import javax.servlet.http.*;

public class MainServlet extends HttpServlet {
    private final HashMap<String, ListOfTasks> lists = new HashMap<>();
    private String nameOfUser;
    private HashMap<String, String> colors = new HashMap<>();

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
            if(!name.equals(nameOfUser)){
                nameOfUser=name;
                lists.clear();
            }

            ListOfTasks fromEdit = (ListOfTasks) request.getAttribute("list");
            if (fromEdit != null) {
                lists.put(fromEdit.getName(), fromEdit);
            }
            String text = request.getParameter("text");
            String id = request.getParameter("id");
            String date = request.getParameter("calendar");
            String color1 = request.getParameter("color");
            String idList = request.getParameter("namelist");
            if(color1!=null && idList!=null){
                colors.put(idList,color1);
            }
            String num = request.getParameter("prior");
            String sort = request.getParameter("sort");
            String nameNewList = request.getParameter("newlist");
            String done = request.getParameter("done");
            String uri = request.getRequestURI();
            String nameOfList;

            if(lists.isEmpty()) {
                readFromFile();
            }
            if(nameNewList!= null){
                ListOfTasks list = new ListOfTasks(nameNewList);
                lists.put(nameNewList,list);
                colors.put(nameNewList,"49b1ca");
            }
            for (Map.Entry<String,ListOfTasks> it: lists.entrySet()) {
                 nameOfList = it.getKey();
                out.println("<div class=\"row\">\n<div class=\"col\">\n<a href=\"/todolist/"+ nameOfList  +"\">" + nameOfList + "</a></div></div>\n");
            }
            out.println("<form action=\"?\" method=\"get\">\n" +
                    "    <input type=\"text\" name=\"newlist\" placeholder=\"Input new list\">\n" +
                    "    <input type=\"submit\" value=\"create\">\n" +
                    "</form>");
            printWhenLogin(out);
            out.println("</div>");

            if(!lists.isEmpty()) {
                for (Map.Entry<String, ListOfTasks> it : lists.entrySet()) {
                    nameOfList = it.getKey();
                    if (uri.contains(nameOfList)) {
                        out.println("<div class=\"col right text-center\" style=\"background-color: #" +colors.get(nameOfList) +";\" id=\""+nameOfList+"\">\n");
                        out.println("<div class=\"row text-center\"> <div class=\"row\">\n<h2>"+ nameOfList  +"</h2></div>\n");
                        out.println("<div class=\"row\">\n<div class=\"col\">\n<input type=\"color\"id=\"color\">\n</div></div>\n");
                        out.println("<div class=\"row text-center\">\n");
                        out.println("<div class=\"col text-center\">\n");
                        printSort(out);
                        out.println("<table class=\"table\">\n");
                        out.println("<thead>\n" +
                                "    <tr>\n" +
                                "      <th scope=\"col\">Description</th>\n" +
                                "      <th scope=\"col\">Date of Creation</th>\n" +
                                "      <th scope=\"col\">Deadline</th>\n" +
                                "      <th scope=\"col\">Priority</th>\n" +
                                "      <th scope=\"col\"></th>\n" +
                                "    </tr>\n" +
                                "  </thead>");
                        out.println("<tbody>");
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

                        out.println("</tbody>");
                        out.println("</table>");
                        if(!nameOfList.equals("archive"))
                        printAddTask(out,nameOfList);
                        out.println("</div>\n");
                        out.println("</div>\n");
                        out.println("</div>\n");
                        break;
                    }
                }
            }
            out.println("</div>");
        } else {
           //printWhenLogout(out);
            //out.println("</body>\n</html>");
//            if(nameOfUser !=null)
//                saveToFile();
//            lists.clear();
//           nameOfUser=null;
        }
        out.println("</div>");
        out.println("</div>");
        out.println("<script src=\"../main.js\"></script>");
        out.println("</body>");
        out.println("</html>");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ListOfTasks list = lists.get(req.getParameter("listName"));
        Task task = list.getTask(Integer.parseInt(req.getParameter("id")));

        String deadline = req.getParameter("deadline");
        String priority = req.getParameter("priority");
        String text = req.getParameter("content");
        String uri = req.getRequestURI();
        if(uri.contains("color/set")){

        }
        if (!deadline.equals("")) {
            try {
                task.setDeadline(formatter.parse(deadline));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        task.setPriority(Integer.parseInt(priority));
        task.setText(text);


        PrintWriter out = resp.getWriter();
        doGet(req,resp);
    }

    @Override
    public void destroy() {
        if(nameOfUser !=null)
            saveToFile();
    }

    private void readFromFile(){
        try {
            Scanner scanner = new Scanner(new File("C:\\Users\\Дмитрий\\IdeaProjects\\todolist\\src\\main\\resources\\" + nameOfUser + ".txt"));
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
                colors.put(nameOfList,"49b1ca");
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
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"../bootstrap.min.css\">\n"+
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"../main.css\">\n"+
                "    </head>\n" +
                "<body>\n" +
                "<div class=\"p-5 container-fluid\">\n"+
                "<div class=\"row\">\n"+
                "<div class=\"col left text-center\">\n"+
                "<h1>TO DO LIST</h1>");
    }
    private  void printWhenLogin(PrintWriter out) {
        out.println("<form action=\"/LogoutServlet\">\n" +
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

    private  void printAddTask(PrintWriter out, String name){
        out.println("<form action=\"?\" method=\"get\">\n" +
                "    <input type=\"text\" placeholder=\"Input new task\" name=\"text\">\n" +
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
        private  void printWhenLogout(PrintWriter out){
        out.println("<form action=\"/login.html\">\n" +
                " <button class=\"login\">Login</button>\n" +
                "</form>");
        out.println("</body>\n</html>");
        if(nameOfUser !=null)
        saveToFile();
        lists.clear();
    }
}