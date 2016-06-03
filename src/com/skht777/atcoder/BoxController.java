/**
 * 
 */
package com.skht777.atcoder;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

import javafx.collections.FXCollections;
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
import javafx.util.Pair;
import javafx.util.StringConverter;
import javafx.scene.layout.BorderPane;

/**
 * @author skht777
 *
 */
public class BoxController implements Initializable {

	@FXML private BorderPane root;
	@FXML private TextField userField;
	@FXML private ComboBox<Pair<String, String>> contest;
	@FXML private ComboBox<Pair<String, List<Submission>>> problem;
	@FXML private ComboBox<Submission> submission;
	@FXML private TextArea postedCode;
	@FXML private Label languageLabel;
	@FXML private Button problemButton;
	@FXML private Button codeButton;
	@FXML private Button saveButton;

	private APIConnect api;
	private String selectedContest;

	private <T> StringConverter<T> getConverter(Function<T, String> converter) {
		return new StringConverter<T>() {
			@Override
			public String toString(T object) {
				return converter.apply(object);
			}

			@Override
			public T fromString(String string) {return null;}
		};
	}

	private Alert getAlert(Exception e, String message) {
		e.printStackTrace();
		Alert alert = new Alert(AlertType.ERROR);
		alert.getDialogPane().setHeaderText("通信エラー");
		alert.getDialogPane().setContentText(message);
		return alert;
	}
	
	@FXML
	private void setContest() {
		selectedContest = contest.getSelectionModel().getSelectedItem().getValue();
	}

	@FXML
	private void setProblem() {
		try {
			problem.setItems(FXCollections.observableArrayList(api.getInfo(userField.getText(), selectedContest)));
		}catch(Exception e) {
			getAlert(e, "投稿情報の取得に失敗しました。").show();
			return;
		}
	}

	@FXML
	private void setSubmission() {
		submission.setItems(FXCollections.observableArrayList(problem.getSelectionModel().getSelectedItem().getValue()));
	}

	@FXML
	private void setCode() {
		if (submission.getSelectionModel().isEmpty()) return;
		languageLabel.setText(submission.getSelectionModel().getSelectedItem().getLanguage());
		try {
			postedCode.setText(api.getCode(submission.getSelectionModel().getSelectedItem().getId(), selectedContest));
		}catch(Exception e) {
			getAlert(e, "提出コードの取得に失敗しました。").show();
			return;
		}
	}

	@FXML
	private void saveCode() {
		FileChooser fc = new FileChooser();
		fc.setTitle("保存先を指定");
		fc.setInitialFileName(selectedContest);
		fc.getExtensionFilters().addAll(Extention.getExtensionList());
		fc.setSelectedExtensionFilter(Extention.toExtensionFilter(languageLabel.getText()));
		Optional.ofNullable(fc.showSaveDialog(root.getScene().getWindow())).ifPresent(f->{
			try {
				Files.write(f.toPath(), postedCode.getText().getBytes());
			}catch(IOException e) {}}
		);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		api = new APIConnect();
		contest.setConverter(getConverter(Pair::getKey));
		problem.setConverter(getConverter(Pair::getKey));
		try {
			contest.setItems(FXCollections.observableArrayList(api.getContests()));
		}catch(Exception e) {
			getAlert(e, "コンテスト情報の取得に失敗しました。").show();
			return;
		}
	}
}
