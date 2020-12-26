package authorization;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class RegistrationServlet extends HttpServlet {
    private HashMap<String,String> dataBase = new HashMap<>();
    @Override
    public void init() throws ServletException {
        try{
            Scanner scanner = new Scanner(new File("C:\\Users\\Дмитрий\\IdeaProjects\\todolist\\src\\main\\resources\\dataBase.txt"));
            while(scanner.hasNext()){
                String line = scanner.nextLine();
                String[] s = line.split(" ");
                for (int i = 1; i < s.length; i++) {
                    dataBase.put(s[0], s[i]);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        if(!password.equals(password2)){
            request.getRequestDispatcher("registration.html").include(request, response);
            out.print("<div style=\"color: #020113; font-size: 22px;\">Sorry, Passwords do not match!</div>");
            out.println("</html></body>");
        }
        else if(dataBase.containsKey(name)){
            request.getRequestDispatcher("registration.html").include(request, response);
            out.print("<div style=\"color: #020113; font-size: 22px;\">Sorry, This name is already in use!</div>");
            out.println("</html></body>");
        }
        else {
            FileWriter writer = new FileWriter("C:\\Users\\Дмитрий\\IdeaProjects\\todolist\\src\\main\\resources\\dataBase.txt", true);
            writer.write("\n" + name + " " + password);
            writer.close();
            File newFile  = new File("C:\\Users\\Дмитрий\\IdeaProjects\\todolist\\src\\main\\resources\\" + name+ ".txt");
            newFile.createNewFile();
            HttpSession session = request.getSession();
            session.setAttribute("name", name);
            dataBase.put(name,password);
            response.sendRedirect("/todolist/");
        }
        out.close();

    }
}
