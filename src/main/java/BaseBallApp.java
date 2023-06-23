import dao.StadiumDAO;
import db.DBConnection;
import service.StadiumService;

public class BaseBallApp {

    private static final StadiumService stadiumService;

    static {
        stadiumService = new StadiumService(new StadiumDAO(DBConnection.getInstance()));
    }

    public static void main(String[] args) {

    }
}
