/**
 * 
 */
package com.skht777.atcoder;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * @author skht777
 *
 */
public class MainController implements Initializable {

	@FXML private BorderPane root;
	@FXML private JFXTextField userField;
	@FXML private JFXComboBox<Pair<String>> contest;
	@FXML private JFXComboBox<Pair<List<Submission>>> problem;
	@FXML private JFXComboBox<Submission> submission;
	@FXML private WebView postedCode;
	@FXML private Label languageLabel;
	@FXML private JFXButton problemButton;
	@FXML private JFXButton codeButton;
	@FXML private JFXButton saveButton;

	private APIConnect api;
	private JSObject obj;

	private Alert getAlert(Exception e, String message) {
		e.printStackTrace();
		Alert alert = new Alert(AlertType.ERROR);
		alert.getDialogPane().setHeaderText("通信エラー");
		alert.getDialogPane().setContentText(message);
		return alert;
	}

	@FXML
	private void setContest() {
		setProblem();
	}

	@FXML
	private void setProblem() {
		if (userField.getActiveValidator() != null || contest.getSelectionModel().isEmpty()) return;
		submission.getItems().clear();
		try {
			problem.getItems().addAll(api.getInfo(userField.getText(), contest.getSelectionModel().getSelectedItem().getValue()));
		}catch(Exception e) {
			getAlert(e, "投稿情報の取得に失敗しました。").show();
		}
	}

	@FXML
	private void setSubmission() {
		submission.getItems().addAll(problem.getSelectionModel().getSelectedItem().getValue());
	}

	@FXML
	private void setCode() {
		if (submission.getSelectionModel().isEmpty()) return;
		try {
			obj.call("setCode", api.getCode(submission.getSelectionModel().getSelectedItem().getId(), 
					contest.getSelectionModel().getSelectedItem().getValue()));
		}catch(Exception e) {
			getAlert(e, "提出コードの取得に失敗しました。").show();
			return;
		}
		languageLabel.setText(submission.getSelectionModel().getSelectedItem().getLanguage());
		Extension lang = Extension.of(languageLabel.getText());
		if (Stream.of(Extension.C, Extension.CPP).anyMatch(e->e.equals(lang))) obj.call("setMode", "c_cpp");
		else obj.call("setMode", lang.toString().toLowerCase());
		saveButton.setDisable(false);
	}

	@FXML
	private void saveCode() {
		FileChooser fc = new FileChooser();
		fc.setTitle("保存先を指定");
		fc.setInitialFileName(contest.getSelectionModel().getSelectedItem().getValue());
		fc.getExtensionFilters().addAll(Extension.getFilterList());
		fc.setSelectedExtensionFilter(Extension.of(languageLabel.getText()).getFilter());
		Optional.ofNullable(fc.showSaveDialog(root.getScene().getWindow())).ifPresent(f->{
			try {
				Files.write(f.toPath(), ((String) obj.call("getCode")).getBytes());
			}catch(IOException e) {}
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		api = new APIConnect();
		ValidatorBase require = new RequiredFieldValidator();
		require.setMessage("ユーザIDは必須");
		ValidatorBase validator = new UserValidator(api);
		validator.setMessage("無効なユーザID");
		userField.getValidators().addAll(require, validator);
		userField.focusedProperty().addListener((o, oldVal, newVal)->{
			if(!newVal) userField.validate();
			setProblem();
		});
		postedCode.getEngine().load(getClass().getResource("/resources/html/ace.html").toExternalForm());
		obj = (JSObject) postedCode.getEngine().executeScript("window");
		try {
			contest.getItems().addAll(api.getContests());
		}catch(Exception e) {
			getAlert(e, "コンテスト情報の取得に失敗しました。").show();
		}
	}
}
