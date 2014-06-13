package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class dataAccess implements SQLInterface {
	private Connection con;

	public dataAccess(Connection con) {
		this.con = con;
	}

	// used for logging into the game. returns 1 if user is a teacher, 2 if user is a student
	// and 0 if user does not have an account
	public int login(String u, String p) {
		try {
			Statement select = (Statement) con.createStatement();
			ResultSet teacher = select.executeQuery("select `t-user` from teacher where password = '" + p +"';");
			Statement select2 = (Statement) con.createStatement();
			ResultSet student = select2.executeQuery("select `s-user` from student where password = '" + p +"';");

			while(teacher.next()){
				String name = (String) teacher.getObject(1).toString();
				if (name.equals(u)){
					select.close();
					teacher.close();
					return 1;
				}				
			}

			while(student.next()){
				String name = (String) student.getObject(1).toString();
				if (name.equals(u)){
					select2.close();
					student.close();
					return 2;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override // method to return students for list view on teacher home page
	public ObservableList<String> teacherHome(String u) {
		ObservableList<String> items = FXCollections.observableArrayList();
		ResultSet teacher = null;

		try{
			Statement select = (Statement) con.createStatement();

			teacher = select.executeQuery("SELECT firstname from student;");
			while (teacher.next()){
				items.add((String) teacher.getObject(1).toString());
			}
			select.close();
			teacher.close();			
		}
		catch (Exception e){
			e.printStackTrace();	
		}
		return items;
	}

	@Override // method to return teacher info for the list view on teacher accounts page
	public ObservableList<String> teacherAccounts(String u) {
		ObservableList<String> items = FXCollections.observableArrayList();
		ResultSet teacher = null;

		try{
			Statement select = (Statement) con.createStatement();

			teacher = select.executeQuery("SELECT firstname from teacher;");
			while (teacher.next()){
				items.add((String) teacher.getObject(1).toString());
			}
			select.close();
			teacher.close();			
		}
		catch (Exception e){
			e.printStackTrace();	
		}
		return items;
	}

	// gets student data for student detail information on teacher home page
	public String[] getStudent(String u){
		String[] array = new String[6];
		try{
			Statement select = (Statement) con.createStatement();
			ResultSet teacher = select.executeQuery("SELECT lastname, grade_level, email,`s-user`, password from student where firstname = '"+u+"';");

			while (teacher.next()){
				array[1]= (String) teacher.getObject(1).toString();
				array[2]= (String) teacher.getObject(2).toString();
				array[3]= (String) teacher.getObject(3).toString();
				array[4]= (String) teacher.getObject(4).toString();
				array[5]= (String) teacher.getObject(5).toString();               
			}
			select.close();
			teacher.close();			
		}

		catch (Exception e){
			e.printStackTrace();	
		}
		return array;
	}

	// gets individual teacher data
	public String[] getTeacher(String u){
		String[] array = new String[4];
		try{
			Statement select = (Statement) con.createStatement();
			ResultSet teacher = select.executeQuery("SELECT lastname, email,`t-user`, password from teacher where firstname = '"+u+"';");

			while (teacher.next()){
				array[0]= (String) teacher.getObject(1).toString();
				array[1]= (String) teacher.getObject(2).toString();
				array[2]= (String) teacher.getObject(3).toString();
				array[3]= (String) teacher.getObject(4).toString();

			}
			select.close();
			teacher.close();			
		}

		catch (Exception e){
			e.printStackTrace();	
		}
		return array;
	}

	// gets first column of a select statement
	public ArrayList<String> select(String s){
		ArrayList<String> list = new ArrayList<String>();
		try{
			Statement select = (Statement) con.createStatement();
			ResultSet names = select.executeQuery(s);

			while(names.next()){
				list.add((String) names.getObject(1).toString());
			}
			select.close();
			names.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	// executes an update statement
	public void update(String s){
		try{
			Statement select = (Statement) con.createStatement();
			select.executeUpdate(s);
			select.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	// executes an insert statement
	public void insert(String s) {
		try{
			Statement select = (Statement) con.createStatement();
			select.execute(s);
			select.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	// executes a delete statement
	public void delete(String s){
		try{
			Statement select = (Statement) con.createStatement();
			select.execute(s);
			select.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	// fetches students name and scores and returns them in a String array
	public String[] gameHome(String u){
		ResultSet name = null;
		String[] values = new String[11];

		try{
			Statement select = (Statement) con.createStatement();
			name = select.executeQuery("SELECT firstname, lastname from student where `s-user` = '"+u+"';");
			while(name.next()){
				values[0]=((String) name.getObject(1).toString());
				values[1]=((String) name.getObject(2).toString());
			}

			select.close();
			name.close();			
		}
		catch (Exception e){
			e.printStackTrace();	
		}

		try{
			Statement select = (Statement) con.createStatement();
			name = select.executeQuery("select addHighestLevel, addCurrentPercent, addTimeScore, subHighestLevel, subCurrentPercent, subTimeScore, npHighestLevel, npCurrentPercent, npTimeScore from progress where `s-user` = '"+u+"';");

			while(name.next()){
				values[2]=((String) name.getObject(1).toString());
				values[3]=((String) name.getObject(2).toString());
				values[4]=((String) name.getObject(3).toString());
				values[5]=((String) name.getObject(4).toString());
				values[6]=((String) name.getObject(5).toString());
				values[7]=((String) name.getObject(6).toString());
				values[8]=((String) name.getObject(7).toString());
				values[9]=((String) name.getObject(8).toString());
				values[10]=((String) name.getObject(9).toString());

			}

			select.close();
			name.close();			
		}
		catch (Exception e){
			e.printStackTrace();	
		}
		return values;
	}

	// A method to return the custom addition problems in the system
	public ObservableList<String> getAddProblems(){
		ObservableList<String> items = FXCollections.observableArrayList();
		ResultSet teacher = null;

		try{
			Statement select = (Statement) con.createStatement();

			teacher = select.executeQuery("SELECT * from addproblems;");
			while (teacher.next()){
				items.add((String) teacher.getObject(1).toString() + " + "+ (String) teacher.getObject(2).toString());
			}
			select.close();
			teacher.close();			
		}
		catch (Exception e){
			e.printStackTrace();	
		}
		return items;
	}

	// A method to return the custom addition problems in the system
	public ObservableList<String> getSubProblems(){
		ObservableList<String> items = FXCollections.observableArrayList();
		ResultSet teacher = null;

		try{
			Statement select = (Statement) con.createStatement();

			teacher = select.executeQuery("SELECT * from subproblems;");
			while (teacher.next()){
				items.add((String) teacher.getObject(1).toString() + " - " + (String) teacher.getObject(2).toString());
			}
			select.close();
			teacher.close();			
		}
		catch (Exception e){
			e.printStackTrace();	
		}
		return items;

	}
}
