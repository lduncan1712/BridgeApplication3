package com.lduncan1712.bridgeApplication.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import com.lduncan1712.bridgeApplication.database.FromDB;
import com.lduncan1712.bridgeApplication.structures.Edge;
import com.lduncan1712.bridgeApplication.structures.EdgeTier;
import com.lduncan1712.bridgeApplication.structures.Node;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class InnerSceneController implements Initializable{
	
private static InnerSceneController instance;
	
	public InnerSceneController() {
		instance = this;
		
	}
	
	public static InnerSceneController getInstance() {
		return instance;
	}

	
	
	
	
	
	
	@FXML ListView<EdgeTier> tier_lv;
	@FXML ListView<Edge> conn_lv;
	@FXML ProgressBar tier_pb;
	@FXML TextField manual_search_tf;
	@FXML ListView<Node> search_lv;
	
	@FXML TextField conn_tf;
	@FXML TextField search_tf;
	@FXML CheckBox automatic_cb;
	@FXML Button match_null_b;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//TIER (LISTVIEW)
		tier_lv.setItems(ConfigController.getPhase2Surveyee().sublist);
		//WHEN SELECTED TIER CHANGES
		tier_lv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<EdgeTier>() {
			@Override
			public void changed(ObservableValue<? extends EdgeTier> observable, EdgeTier oldValue, EdgeTier newValue) {
				if(!Objects.isNull(newValue)) {
					conn_lv.setItems(newValue.sublist);
					updateCurrentTierPB(newValue.returnDecimalCompleted());
				}
				search_tf.setText("");
				conn_tf.setText("");
				search_lv.setItems(null);
			}
		});
		
		//EDGE (LISTVIEW)
		conn_lv.setPlaceholder(new Label("Please Select A Tier"));
		//WHEN SELECTED EDGE CHANGES
		conn_lv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Edge>() {
			@Override
			public void changed(ObservableValue<? extends Edge> observable, Edge oldValue, Edge newValue) {
				if(!Objects.isNull(newValue)) {
					search_lv.setItems(newValue.sublist);
					conn_tf.setText(newValue.recipient);
					if(!Objects.isNull(newValue.confirmedMatch)) {
						search_tf.setText(newValue.confirmedMatch.toString());
					}else
						search_tf.setText("");
				}
			}
		});
		
		//NODE (LISTVIEW)
		search_lv.setPlaceholder(new Label("Please Select An Edge"));
		//WHEN SEARCH ITEM IS SELECTED
		search_lv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Node>() {
			@Override
			public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue){
				if(!Objects.isNull(newValue)) {
					Edge item = conn_lv.getSelectionModel().getSelectedItem();
					doProgressIfUnmatched(item);
					item.setConfirmedMatch(newValue);
					search_tf.setText(newValue.toString());
				}
			}
		});
		
		automatic_cb.selectedProperty().set(true);
		automatic_cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!conn_lv.getSelectionModel().isEmpty()) {
					if(newValue) {
						search_lv.setItems(conn_lv.getSelectionModel().getSelectedItem().sublist);
					}else {
						search_lv.setItems(ConfigController.localManualSearchCache);
					}
				}	
			}
		});
	}
	
	
	public boolean doProgressIfUnmatched(Edge e) {
		if(Objects.isNull(e.getConfirmedMatch())) {
			e.incrementTotalProgress(1);
			updateCurrentTierPB(e.getSuperObject().returnDecimalCompleted());
			return true;
		}
		return false;
	}
	
	
	
	public void matchToNull(ActionEvent e) {
		Edge edge = conn_lv.getSelectionModel().getSelectedItem();
		doProgressIfUnmatched(edge);
		edge.setConfirmedMatch(ConfigController.null_match);
		search_tf.setText(ConfigController.null_match.toString());
	}
	
	
	
	public void moveWithinList(boolean reverse) {
		if(!Objects.isNull(ConfigController.getPhase2Surveyee())) {
			//IF NO TIER SELECTED
			if(tier_lv.getSelectionModel().isEmpty()) {
				tier_lv.getSelectionModel().select(0);
				conn_lv.getSelectionModel().select(0);	
			//OTHERWISE IS TIER IS SELECTED	
			}else {
				//IF NO EDGE IS SELECTED
				if(conn_lv.getSelectionModel().isEmpty()) {
					
					if(reverse) {
						conn_lv.getSelectionModel().select(tier_lv.getSelectionModel().getSelectedItem().sublist.size() - 1);
					}else {
						conn_lv.getSelectionModel().select(0);
					}
				}else {
				//OTHERWISE IF AN EDGE IS SELECTED	
					int tier_index = tier_lv.getSelectionModel().getSelectedIndex();
					int edge_index = conn_lv.getSelectionModel().getSelectedIndex();
					EdgeTier et = tier_lv.getSelectionModel().getSelectedItem();
					//IF REVERSING
					if(reverse) {
						//IF AT START OF EDGE
						if(edge_index == 0) {
							//IF NOT IN FIRST TIER
							if(tier_index != 0) {
								//go up a tier, and set to last
								tier_lv.getSelectionModel().select(--tier_index);
								conn_lv.getSelectionModel().selectLast();
							}
							//OTHERWISE STAY SAME
						//IF NOT AT START OF EDGE
						}else {
							conn_lv.getSelectionModel().selectPrevious();
						}
					//IF GOING FORWARD	
					}else {
						//IF AT THE END OF EDGE
						if(edge_index == et.sublist.size() - 1) {
							//IF NOT IN LAST ROW
							if(tier_index != et.superObject.sublist.size() - 1) {
								//go down one row, and select the first index
								tier_lv.getSelectionModel().selectNext();
								conn_lv.getSelectionModel().selectFirst();
							}
							//IF SO DONT CHANGE
						//IF NOT AT END OF EDGE
						}else {
							conn_lv.getSelectionModel().selectNext();
						}
					}
				}
			}
		}
	}
	
	
	
	
	
	public void updateCurrentTierPB(double value) {
		tier_pb.setProgress(value);
	}
	
	public void search() throws ClassNotFoundException, SQLException {
		String str = manual_search_tf.getText();
		if(str.length() > 0) {
			ConfigController.localManualSearchCache.clear();
			FromDB.setManualSearchResults(str);
			//should set list
			automatic_cb.setSelected(false);
		}
		
	}

}
