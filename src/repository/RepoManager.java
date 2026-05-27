package repository;

import database.DBConnection;
import java.sql.Connection;

public class RepoManager {
    private static RepoManager instance;
    private UserRepository userRepo;
    private CarRepository carRepo;
    private CustomerRepository customerRepo;
    private TransactionRepository transactionRepo;
    private PaymentRepository paymentRepo;
    private LogRepository logRepo;

    private RepoManager() {
        Connection conn = DBConnection.getConnection();
        userRepo = new UserRepository(conn);
        carRepo = new CarRepository(conn);
        customerRepo = new CustomerRepository(conn);
        transactionRepo = new TransactionRepository(conn);
        paymentRepo = new PaymentRepository(conn);
        logRepo = new LogRepository(conn);
    }

    public static RepoManager getInstance() {
        if (instance == null) {
            instance = new RepoManager();
        }
        return instance;
    }

    public UserRepository getUserRepo() { return userRepo; }
    public CarRepository getCarRepo() { return carRepo; }
    public CustomerRepository getCustomerRepo() { return customerRepo; }
    public TransactionRepository getTransactionRepo() { return transactionRepo; }
    public PaymentRepository getPaymentRepo() { return paymentRepo; }
    public LogRepository getLogRepo() { return logRepo; }
}