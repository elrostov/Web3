package dao;

import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        List<BankClient> bankClientList = new ArrayList<>();
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client");
        ResultSet result = stmt.getResultSet();
        while (result.next()) {
            bankClientList.add(new BankClient(
                    result.getLong(1),
                    result.getString(2),
                    result.getString(3),
                    result.getLong(4)));
        }
        stmt.close();
        return bankClientList;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        return getClientByName(name).getPassword().equals(password);
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        Long money = getClientByName(name).getMoney() + transactValue;
        PreparedStatement preparedStatement = connection.prepareStatement(
                "update bank_client set money = ? where name = ?");
        preparedStatement.setLong(1, money);
        preparedStatement.setString(2, name);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public boolean deleteClientByName(String name) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate("delete from bank_client where name ='" + name + "'") == 1;
    }

    public BankClient getClientById(long id) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery("select * from bank_client where id='" + id + "'");
        return getBankClient(stmt, result);
    }

    private BankClient getBankClient(Statement stmt, ResultSet result) throws SQLException {
        if (result.next()) {
            BankClient bankClient = new BankClient(
                    result.getLong(1),
                    result.getString(2),
                    result.getString(3),
                    result.getLong(4));
            stmt.close();
            return bankClient;
        } else {
            stmt.close();
            throw new SQLException();
        }
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        return getClientByName(name).getMoney() >= expectedSum;
    }

    public long getClientIdByName(String name) throws SQLException {
        return getClientByName(name).getId();
    }

    public BankClient getClientByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery("select * from bank_client where name='" + name + "'");
        return getBankClient(stmt, result);
    }

    public void addClient(BankClient client) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into bank_client (name, password, money) values(?,?,?)");
        preparedStatement.setString(1, client.getName());
        preparedStatement.setString(2, client.getPassword());
        preparedStatement.setLong(3, client.getMoney());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, " +
                "name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }
}
