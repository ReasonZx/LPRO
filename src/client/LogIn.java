package client;

import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class LogIn extends BasicGameState {

	private TextField Username;
	private TextField Password;
	private Font myFont;
	private String User; // For hardcoded login
	private String Pass; // For hardcoded login
	private Image Back;
	private Image Login;
	private int login_x, login_y;
	private int UserTextX, UserTextY;
	private int PassTextX, PassTextY;
	private int backX, backY;
	private boolean error_login = false;
	private GUI_setup sbg;
	private String server_response;

	public void init(GameContainer gc, StateBasedGame arg1) throws SlickException {
		sbg = (GUI_setup) arg1;
		sbg.Set_Login_State(getID());
		myFont = gc.getDefaultFont();

		UserTextX = (int) (0.35 * (float) sbg.Get_Display_width());
		UserTextY = (int) (0.35 * (float) sbg.Get_Display_height());

		Username = new TextField(gc, myFont, UserTextX, UserTextY, 400, 20);
		Username.setBackgroundColor(Color.white);
		Username.setBorderColor(Color.white);
		Username.setTextColor(Color.black);

		PassTextX = (int) (0.35 * (float) sbg.Get_Display_width());
		PassTextY = (int) (0.55 * (float) sbg.Get_Display_height());

		Password = new TextField(gc, myFont, PassTextX, PassTextY, 400, 20);
		Password.setBackgroundColor(Color.white);
		Password.setBorderColor(Color.white);
		Password.setTextColor(Color.black);

		Pass = new String();
		User = new String();

		Back = new Image("sprites/back.png");
		Back = Back.getScaledCopy(0.2f);
		Login = new Image("sprites/logIn.png");
		Login = Login.getScaledCopy(0.5f);

		backX = 50;
		backY = 50;

		login_x = (int) (0.43 * (float) sbg.Get_Display_width());
		login_y = (int) (0.7 * (float) sbg.Get_Display_height());
	}

	@Override
	public void enter(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// TODO Auto-generated method stub
		arg0.getInput().clearMousePressedRecord();
		Username.setAcceptingInput(true);
		Password.setAcceptingInput(true);
		Username.setText("");
		Password.setText("");
		Pass = "";
		User = "";
		server_response = "";
		error_login = false;
	}

	public void update(GameContainer gc, StateBasedGame arg1, int delta) throws SlickException {
		// delta = 60;
		// this.Pass = this.Password.getText();
		User = this.Username.getText();

		int posX = gc.getInput().getMouseX();
		int posY = gc.getInput().getMouseY();

		if ((posX > backX && posX < backX + Back.getWidth()) && (posY > backY && posY < backY + Back.getHeight())) {
			if (Mouse.isButtonDown(0)) {
				sbg.enterState(sbg.Get_Menu_State());
			}
		}

		if ((posX > login_x && posX < login_x + Login.getWidth())
				&& (posY > login_y && posY < login_y + Login.getHeight())) { // ver tamanhos certos dos bot�es
			if (gc.getInput().isMousePressed(gc.getInput().MOUSE_LEFT_BUTTON)) {
				try {
					String request = "login_" + this.User + "_" + this.Pass + "_" + sbg.server.ss.getLocalPort();
					server_response = sbg.server.request(request);
					if (server_response.equals("Logged in")) {
						error_login = false;
						sbg.enterState(sbg.Get_MainMenu_State());
					} else {
						error_login = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (this.Password.hasFocus() && gc.getInput().isKeyPressed(15)) {
			this.Username.setFocus(true);
		}

		if (this.Username.hasFocus() && gc.getInput().isKeyPressed(15)) {
			this.Password.setFocus(true);
		}

		if (this.Password.hasFocus() && gc.getInput().isKeyPressed(28)) {
			try {
				String request = "login_" + this.User + "_" + this.Pass + "_" + sbg.server.ss.getLocalPort();
				server_response = sbg.server.request(request);
				if (server_response.equals("Logged in")) {
					error_login = false;
					sbg.enterState(sbg.Get_MainMenu_State());
				} else {
					error_login = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String temp = new String();

		if (this.Password.hasFocus()) {
			if (this.Password.getText().length() > this.Pass.length()) {
				this.Pass = this.Pass + this.Password.getText().substring(this.Password.getText().length() - 1,
						this.Password.getText().length());
			} else if (this.Password.getText().length() < this.Pass.length()) {
				this.Pass = this.Pass.substring(0, this.Pass.length() - 1);
			} else {
				for (int i = 0; i < this.Password.getText().length(); i++)
					temp = temp + "*";
				this.Password.setText(temp);
			}
		}
	}

	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		// g.drawString(this.User, 500, 10);
		// g.drawString(this.Pass, 500, 30);
		g.drawString("Username:", UserTextX - 100, UserTextY);
		Username.render(gc, g);
		g.drawString("Password:", PassTextX - 100, PassTextY);
		Password.render(gc, g);
		Back.draw(backX, backY);
		Login.draw(login_x, login_y);

		if (error_login) {
			g.setColor(Color.red);
			g.drawString(server_response,
					(int) ((float) sbg.Get_Display_width() * 0.50) - myFont.getWidth(server_response) / 2,
					(int) ((float) sbg.Get_Display_height() * 0.15));
			g.setColor(Color.white);
		}
	}

	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		arg0.getInput().clearMousePressedRecord();
		Username.setAcceptingInput(false);
		Password.setAcceptingInput(false);
		server_response = "";
		error_login = false;
	}

	public int getID() {
		return 2;
	}
}