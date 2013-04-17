package com.ccdrive.moviestore.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class JsonUtilJava {
	
	/**
	 * JsonFromTable
	 * @param Connection
	 * @param String
	 * @param String
	 * @return String
	 * */
	public static String getJsonFromTable(Connection con,String sql,String name){
		StringBuffer result = new StringBuffer("{'"+name+"':[");
		
		boolean flag=true;
		try {
			/*Class.forName(driver);
			con = DriverManager.getConnection(url, user, pass);*/
			try {
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				ResultSetMetaData md = rs.getMetaData();
				int col = md.getColumnCount();
				String[] names = new String[col];
				String[] types = new String[col];
				for (int i = 1; i <= col; i++) {
					String col_name = md.getColumnName(i);
					String typename = md.getColumnTypeName(i);
					String type = md.getColumnClassName(i);
					names[i - 1] = col_name;
					types[i - 1] = type;
				}
				while (rs.next()) {
					if(flag!=true){
						result.append(",\n");
					}
					result.append("{");
					for (int i = 0; i < names.length; i++) {
						if(i!=0){
							result.append(",");
						}
						if(i==names.length){
							result.append("}");
						}
						result.append("'"+names[i]+"':");
						if (types[i].equalsIgnoreCase("java.lang.String")) {
							result.append("'"+rs.getString(names[i])+"'");
						} else if (types[i]
								.equalsIgnoreCase("java.math.BigDecimal")) {
							result.append("'"+rs.getInt(names[i])+"'");
						} else if (types[i]
								.equalsIgnoreCase("java.sql.Timestamp")) {
							result.append("'"+rs.getDate(names[i])+"'");
						}
						flag=false;
					}
					result.append("}");
				}
				rs.close();
				st.close();
			} catch (SQLException s) {
				System.out.println("SQL statement is not executed!");
				s.printStackTrace();
			}
			//con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.append("]}");
		System.out.println(result);
		System.out.println("**************************************");
		return new String(result);
	}
	/**
	 * objToJson
	 * @param Object
	 * @return String
	 * */
//	public static String objToJson(Object obj){
//        JSONObject json=JSONObject.fromObject(obj);
//        return json.toString();
//    }
	/**
	 * jsonToObj
	 * @param String 
	 * @param class
	 * @return Object
	 * */
//	public static Object jsonToObj(String jsontext,Class clazz){
//    	JSONObject jsonObject =JSONObject.fromObject(jsontext);
//    	return JSONObject.toBean(jsonObject, clazz);
//    }
	
}
