import java.util.ArrayList;
import org.newdawn.slick.SlickException;

import org.newdawn.slick.SlickException;

public class GameLogic {
	protected ArrayList<Bomber> Character = new ArrayList<Bomber>();
	protected Map m;
	protected Image_Library lib;
	protected Layout_Logic map_gen;
	
	GameLogic(Image_Library l){
		map_gen = new Layout_Logic(this);
		lib=l;
	}

	public void Action(int key) throws SlickException {
		for(int i=0;i<Character.size();i++) {
			if(key==Character.get(i).Get_MoveLeft_Key() && MoveLeftPermitted(i)) { //A
				m.Remove_Element(Character.get(i));
				Character.get(i).MoveLeft();
				m.Add_Element(Character.get(i));
			}	
			
			if(key==Character.get(i).Get_MoveRight_Key() && MoveRightPermitted(i)) { //D
				m.Remove_Element(Character.get(i));
				Character.get(i).MoveRight();
				m.Add_Element(Character.get(i));
			}	
			
			if(key==Character.get(i).Get_MoveDown_Key() && MoveDownPermitted(i)) { //S
				m.Remove_Element(Character.get(i));
				Character.get(i).MoveDown();
				m.Add_Element(Character.get(i));
			}	
			
			if(key==Character.get(i).Get_MoveUp_Key() && MoveUpPermitted(i)) { //W
				m.Remove_Element(Character.get(i));
				Character.get(i).MoveUp();
				m.Add_Element(Character.get(i));
			}
			
			if(key==Character.get(i).Get_Action_Key() && Can_Place_Bomb(i)==true) {	//Space
				Bomb b = new Bomb(Character.get(i).getX(),Character.get(i).getY(),this);
				b.Start_Countdown();
				m.Add_Element(b);
				//System.out.println("created new bomb");
			}
		}
	}

	public void Place_Characters(ArrayList<Bomber> b) {
		for(int i=0;i<b.size();i++)
			if(m.Has_Solid_Element(b.get(i).getX(), b.get(i).getY())==false) {
				Character.add(b.get(i));
				m.Add_Element(b.get(i));
			}
		
	}
	
	public void Explode(Bomb b) throws SlickException{
		m.Remove_Element(b);
		Propagate_Explosion(b.getX(),b.getY(),b);
	}
	
	private void Propagate_Explosion(int x,int y,Bomb b) throws SlickException {
		Explosion Exp = new Explosion(x,y,this,0,b);
		ArrayList<Element> tmp = Exp.Propagate();
		m.Add_Element_Array(tmp);
	}
	
	private boolean Can_Place_Bomb(int i) {
		if(Character.get(i).Can_Use_Bomb()==false || m.Has_Bomb(Character.get(i).getX(),Character.get(i).getY())==true)
			return false;
		
		Character.get(i).Used_Bomb();
		
		return true;
	}
	
	public int Death_Check(){
		for(int i=0;i<Character.size();i++)
			if(Character.get(i).Death_Check()==true){
				return i;
			}
		return 0;
	}
	
	public void Create_Map(int method) throws SlickException {
		if(method==1) map_gen.Generate_Test_Map();
		if(method==2) map_gen.Generate_Standard_Map();
	}
	
	private boolean MoveLeftPermitted(int i){
		if(m.Out_Of_Bounds(Character.get(i).getX()-1,Character.get(i).getY())==true || Character.get(i).Can_Walk()==false)
			return false;
		
		return !m.Has_Solid_Element(Character.get(i).getX()-1,Character.get(i).getY());
	}
	
	private boolean MoveRightPermitted(int i){
		if(m.Out_Of_Bounds(Character.get(i).getX()+1,Character.get(i).getY())==true || Character.get(i).Can_Walk()==false)
			return false;
		
		return !m.Has_Solid_Element(Character.get(i).getX()+1,Character.get(i).getY());
	}
	
	private boolean MoveDownPermitted(int i){
		if(m.Out_Of_Bounds(Character.get(i).getX(),Character.get(i).getY()+1)==true || Character.get(i).Can_Walk()==false)
			return false;
		
		return !m.Has_Solid_Element(Character.get(i).getX(),Character.get(i).getY()+1);
	}
	
	private boolean MoveUpPermitted(int i){
		if(m.Out_Of_Bounds(Character.get(i).getX(),Character.get(i).getY()-1)==true || Character.get(i).Can_Walk()==false)
			return false;
		
		return !m.Has_Solid_Element(Character.get(i).getX(),Character.get(i).getY()-1);
	}
}