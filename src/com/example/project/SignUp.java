package com.example.project;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignUp extends Activity {
	
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	}
	
	public void cancel(View v) {
		finish();
	}
	
	public void signUp(View v) {
		EditText name = (EditText) findViewById(R.id.username);
		EditText email = (EditText) findViewById(R.id.email);
		EditText reemail = (EditText) findViewById(R.id.reemail);
		EditText password = (EditText) findViewById(R.id.password);
		EditText repassword = (EditText) findViewById(R.id.rePassword);
		
		if((email.getText().toString().equals("")) 
				|| (reemail.getText().toString().equals("")) 
				|| (name.getText().toString().equals("")) 
				|| (password.getText().toString().equals(""))  
				|| (repassword.getText().toString().equals("")))  {
			Toast.makeText(getApplicationContext(), "Please, fill all fields", Toast.LENGTH_LONG).show();
		}
		else if(email.getText().toString().compareTo(reemail.getText().toString()) != 0) {
			Toast.makeText(getApplicationContext(), "E-mails don't match", Toast.LENGTH_LONG).show();
			
			email.setText("");
			reemail.setText("");
		}
		else if(password.getText().toString().compareTo(repassword.getText().toString()) != 0) {
			Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
			
			password.setText("");
			repassword.setText("");
		}
		else {
			final ParseUser user = new ParseUser();
			user.setUsername(name.getText().toString());
			user.setPassword(password.getText().toString());
			user.setEmail(email.getText().toString());
			
			pd = new ProgressDialog(this);
			pd.setTitle("New account");
			pd.setMessage("Saving..");
			pd.show();
			
			
			Bitmap mBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/noavatar.png");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] data = stream.toByteArray();
			final ParseFile imgFile = new ParseFile("noavatar.png", data);
			imgFile.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					if (e == null) {
						  user.put("photo", imgFile);

						  user.signUpInBackground(new SignUpCallback() {
							  @Override
							  public void done(ParseException e) {
								  pd.cancel();

								  if (e == null) {
									  User.getInstance().setmUser(user);
									  startActivity(new Intent(getApplicationContext(), MyMap.class));
								  }		 
								  else {
									  Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
									  System.out.println(e.getMessage());
								  }
							  }
						  });

					}
					else {
						Toast.makeText(getApplicationContext(), "Image Failed", Toast.LENGTH_SHORT).show();
						System.out.println(e.getMessage());
					}
				}
			});
		}
	}
}
