/**
 * 
 */
package com.skht777.atcoder;

import java.util.Optional;

import com.jfoenix.validation.base.ValidatorBase;

import javafx.scene.control.TextInputControl;

/**
 * @author skht777
 *
 */
public class UserValidator extends ValidatorBase {

	private APIConnect api;
	
	public UserValidator(APIConnect api) {
		super();
		this.api = api;
	}
	
	@Override
	protected void eval() {
		if(srcControl.get() instanceof TextInputControl) evalTextInputField();
	}
	
	private void evalTextInputField(){
		hasErrors.set(!Optional.ofNullable(((TextInputControl) srcControl.get()).getText()).filter(s->api.isUserValid(s)).isPresent());
	}

}
