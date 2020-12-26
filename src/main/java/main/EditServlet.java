package main;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditServlet extends HttpServlet {

    private ListOfTasks list;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        printMain(out);
        int id = Integer.parseInt((String)req.getAttribute("id"));
        list = (ListOfTasks)req.getAttribute("list");
        Task task = list.getTask(id);
        printForm(out, task.getText(), task.getPriority(), id);
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

    private void printForm(PrintWriter out, String content, int priority, int id) {
        StringBuilder checkbox = new StringBuilder();
        String selected = "";
        for (int i = 1; i <= 3; i++) {
            if (i == priority) {
                selected = "checked";
            }
            checkbox.append("<input type=\"radio\"").append(selected).append(" name=\"priority\" value=\"").append(i).append("\"/><label>").append(i).append("</label>\n").append("<br/>\n");
            selected = "";
        }

        out.println("<form action=\"/EditServlet\" method=\"POST\">\n" +
                "<input type=\"text\" hidden value=\"" + list.getName() + "\" name=\"listName\">" +
                "<input type=\"text\" hidden value=\"" + id + "\" name=\"id\">" +
                "    <label>Description</label>\n" +
                "    <br/>\n" +
                "    <textarea cols=\"50\" rows=\"5\" name=\"text\">" + content + "</textarea>\n" +
                "    <br/><br/>\n" +
                "    <label>Priority</label>\n" +
                "    <br/>\n" +
                checkbox.toString() +
                "    <br/>\n" +
                "    <label>Deadline</label>\n" +
                "    <br/>\n" +
                "    <input type=\"date\" name=\"deadline\"/>\n" +
                " <input type=\"submit\" value=\"Edit\">"+
                "</form>");
    }
}
