package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.edgeModel;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Integer getWeight(int sourceId, int targetId) {
		
		final String sql = "SELECT e1.object_id AS o1, e2.object_id AS o2, COUNT(*) AS peso "
				+ "FROM exhibition_objects e1, exhibition_objects e2 "
				+ "WHERE e1.exhibition_id = e2.exhibition_id AND e1.object_id = ? AND e2.object_id = ?";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, sourceId);
			st.setInt(2/*in base a quale valore ci riferiamo(secondo '?')*/, targetId);
			ResultSet rs = st.executeQuery();
			
			rs.first();
			int peso = rs.getInt("peso");
			
			rs.close();
			conn.close();
			return peso;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}
	
	public List<edgeModel> getAllWeight(Map<Integer, ArtObject> mappaOggetti) {
		
//		Mi serve sapere quante volte(peso) due oggetti(due object_id) sono capitati nella stessa esibizione
		final String sql = "SELECT e1.object_id AS o1, e2.object_id AS o2, COUNT(*) AS peso "
				+ "FROM exhibition_objects e1, exhibition_objects e2 "
				+ "WHERE e1.exhibition_id = e2.exhibition_id AND e1.object_id > e2.object_id "
				+ "GROUP BY e1.object_id, e2.object_id " 	//metto il '>' così mi prende una sola volta un arco, ossia prende l'arco 11-27 e non 27-11 (così da evitare doppioni)   
				+ "ORDER BY peso desc";
		
		List<edgeModel> allEdges = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				int idSource = rs.getInt("o1");
				int idTarget = rs.getInt("o2");
				int peso = rs.getInt("peso");
				edgeModel e = new edgeModel(mappaOggetti.get(idSource), mappaOggetti.get(idTarget),peso);
				allEdges.add(e);
			}
			
			rs.close();
			conn.close();
			return allEdges;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
