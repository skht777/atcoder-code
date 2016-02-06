/**
 * 
 */
package com.skht777.atcoder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author skht777
 *
 */
public class BoxController implements Initializable {

	@FXML private TextField userField;
	@FXML private ComboBox<String> contest;
	@FXML private ComboBox<String> problem;
	@FXML private ComboBox<String> submission;
	@FXML private Button problemButton;
	@FXML private Button codeButton;
	@FXML private TextArea postedCode;
	@FXML private Label languageLabel;
	@FXML private Button saveButton;
	
	private APIConnect api;
	private List<Submission> selected;
	private String selectedContest;
	private Stage stage;
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	private <T> T getByCollection(Collection<T> c, int index) {
		return c.stream().collect(Collectors.toList()).get(index);
	}
	
	private ObservableList<String> toObservableList(Collection<String> c) {
		ObservableList<String> obl = FXCollections.observableArrayList();
		obl.addAll(c);
		return obl;
	}
	
	@FXML
	private void setProblem() {
		try {
			selectedContest = getByCollection(api.getContests().keySet(), contest.getSelectionModel().getSelectedIndex());
			api.getInfo(userField.getText(), selectedContest);
			problem.setItems(toObservableList(api.getProblems().values()));
		}catch(IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.getDialogPane().setHeaderText("通信エラー");
			alert.getDialogPane().setContentText("投稿情報の取得に失敗しました。");
			alert.show();
			return;
		}
	}
	
	@FXML
	private void setSubmission() {
		selected = getByCollection(api.getSubmissions().values(), problem.getSelectionModel().getSelectedIndex());
		submission.setItems(toObservableList(selected.stream().map(s->s.toString()).collect(Collectors.toList())));
	}
	
	@FXML
	private void setCode() {
		Submission s = selected.get(submission.getSelectionModel().getSelectedIndex());
		languageLabel.setText(s.getLanguage());
		try {
			postedCode.setText(api.getCode(s.getId(), selectedContest));
		}catch(IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.getDialogPane().setHeaderText("通信エラー");
			alert.getDialogPane().setContentText("提出コードの取得に失敗しました。");
			alert.show();
			return;
		}
	}
	
	@FXML
	private void saveCode() {
		FileChooser fc = new FileChooser();
		fc.setTitle("保存先を指定");
		//fc.setSelectedExtensionFilter(Extention.toExtensionFilter(languageLabel.getText()));
		System.out.println();
		fc.getExtensionFilters().add(Extention.toExtensionFilter(languageLabel.getText()));
		File f = fc.showSaveDialog(stage);
		if (f != null) try (FileWriter fw = new FileWriter(f)) {
			fw.write(postedCode.getText());
		}catch(IOException e) {}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			api = new APIConnect();
			contest.setItems(toObservableList(api.getContests().values()));
		}catch(IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.getDialogPane().setHeaderText("通信エラー");
			alert.getDialogPane().setContentText("コンテスト情報の取得に失敗しました。");
			alert.show();
			return;
		}
	}
}
