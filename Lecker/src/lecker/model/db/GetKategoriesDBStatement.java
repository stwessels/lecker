package lecker.model.db;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import lecker.model.data.Kategorie;
import lecker.presenter.Handler;



public class GetKategoriesDBStatement implements DBStatement<Kategorie[]>{
	private PreparedStatement statement;
	
	
	
	public GetKategoriesDBStatement() {
		try {
			statement = Handler.getInstance().getDBManager().prepareStatement("SELECT " + DBManager.TITLE_KATEGORIE_NAME + " FROM " + DBManager.TITLE_KATEGORIE + ";");
		} catch (SQLException e) {
			Handler.getInstance().getExceptionHandler().handle(e);
		}
	}
	
	
	
	@Override
	public Kategorie[] postQuery() {
		try {
			ResultSet set = statement.executeQuery();
			ArrayList<Kategorie> kategories = new ArrayList<Kategorie>();
			
			while (set.next()) {
				kategories.add(new Kategorie(set.getString(DBManager.TITLE_KATEGORIE_NAME)));
			}
			
			return kategories.toArray(new Kategorie[0]);
		} catch (SQLException e) {
			Handler.getInstance().getExceptionHandler().handle(e);
		}
		return null;
	}
}
