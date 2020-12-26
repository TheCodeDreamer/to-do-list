package authorization;

import java.io .*;
import java.util.HashMap;
import java.util.Scanner;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {

    private HashMap<String,String> dataBase = new HashMap<>();

    @Override
    public void init() throws ServletException {
        try{
            Scanner scanner = new Scanner(new File("/home/kirill/IdeaProjects/to-do-list/src/main/resources/dataBase.txt"));
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        PrintWriter out = response.getWriter();

        if( password.equals(dataBase.get(name))){
            HttpSession session = request.getSession();
            session.setAttribute("name", name);
            response.sendRedirect("/todolist/");
        }
        else{

            request.getRequestDispatcher("login.html").include(request, response);
            out.print("<div style=\"color: #b50931;\">Sorry, username or password error!</div>");
            out.println("</html></body>");

        }
        out.close();
    }

}