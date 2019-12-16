package servlet;

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

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.getWriter().println(
                PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
        resp.setContentType("text/html;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        Long count = Long.parseLong(req.getParameter("count"));
        String nameTo = req.getParameter("nameTo");
        Map<String, Object> map = new HashMap<>();

        if (senderName == null || senderPass == null || nameTo == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            if (bankClientService.sendMoneyToClient(new BankClient(senderName, senderPass), nameTo, count)) {
                map.put("message", "The transaction was successful");
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                map.put("message", "transaction rejected");
            }
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", map));
        }
    }
}
