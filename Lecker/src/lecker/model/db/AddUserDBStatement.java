package lecker.model.db;



import java.sql.PreparedStatement;
import java.sql.SQLException;

import lecker.presenter.Handler;



public class AddUserDBStatement implements DBStatement<Boolean> {
	private PreparedStatement statement;
	
	
	
	public AddUserDBStatement(String name, String password) {
		try {
			statement = Handler.getInstance().getDBManager().prepareStatement("INSERT INTO " + DBManager.TITLE_USER + " (" + DBManager.TITLE_USER_NAME + ", " + DBManager.TITLE_USER_PW + ") " +
					"VALUES ('" + name + "','" + password + "');");
		} catch (SQLException e) {
			Handler.getInstance().getExceptionHandler().handle(e);
		}
	}
	
	
	
	@Override
	public Boolean postQuery() {
		try {
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			Handler.getInstance().getExceptionHandler().handle(e);
		}
		return false;
	}

}
