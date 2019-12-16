package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
        resp.setContentType("text/html;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String pass = req.getParameter("password");
        Long money;
        try {
            money = Long.parseLong(req.getParameter("money"));
        } catch (NumberFormatException e) {
            money = 0L;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("message", "Client not add");

        if (name == null || pass == null || name.equals("") || pass.equals("")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            try {
                BankClientService bankClientService = new BankClientService();
                if (bankClientService.addClient(new BankClient(name, pass, money))) {
                    map.put("message", "Add client successful");
                    resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
                }
            } catch (DBException e) {
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
                e.printStackTrace();
            }
        }
    }
}
