package main;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        ArrayList<Task> tasks = (ArrayList<Task>) session.getAttribute("tasks");
        System.out.println(tasks);
        String nameOfList = (String) request.getAttribute("nameOfList");
        if (session != null) {
            String text = request.getParameter("text");
            String date = request.getParameter("calendar");
            String num = request.getParameter("prior");
            if (text != null) {
                if (date != null)
                    tasks.add(new Task(tasks.size(), date, num, text));
                else
                    tasks.add(new Task(tasks.size(), num, text));
            }
            response.sendRedirect("/todolist/" + nameOfList);
        }
    }
}
