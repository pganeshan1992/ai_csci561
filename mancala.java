package mancala;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;


class Gameboard{
	int ev;
	int old_ev;
	int start_pit;
	String parent_node; 
	int level;
	int can_progress;
	String player;
	int turn;
	String position;
	String next_position;
	int alpha ;
	int beta;
	String game_board = "";
	ArrayList<Gameboard> children = new ArrayList<Gameboard>();
	Gameboard parent_obj;
	Gameboard(int start_pit, int ev ,  String game_board, int turn , int can_progress ){
		this.ev = ev;
		this.start_pit = start_pit;
		this.game_board = game_board;
		this.parent_node = "none";
		this.level = 0;
		this.turn = turn;
		if(turn == 1)
			this.position = "B"+(start_pit+1);
		if(turn == 2)
			this.position = "A"+ (start_pit+1);
		this.can_progress = can_progress;
		this.parent_obj = null;
		this.next_position = "none" ;
	}
	void setOldEv(int old_ev){
		this.old_ev = old_ev;
	}
	int getOldEv(){
		return old_ev;
	}
	void setAlpha(int alpha){
		this.alpha = alpha;
	}
	void setBeta(int beta){
		this.beta = beta;
	}
	int getAlpha(){
		return alpha;
	}
	int getBeta(){
		return beta;
	}
	String getAlphaVal(){
		String answer;
		if(alpha == -9999999 ){
			answer = "-Infinity";
		}
		else if(alpha == 9999999){
			answer=  "Infinity";
		}
		else
			answer = alpha+"";
		return answer;
	}
	String getBetaVal(){
		String answer = "";
		if(beta == -9999999 ){
			if(can_progress == 1)
				answer = "-Infinity";
		}
		else if(beta == 9999999){
			answer=  "Infinity";
		}
		else
			answer = beta+"";
		return answer;
	}
	String getPosition(){
		if(level == 0)
			this.position = "root";
		return position;
	}
	String getNextPosition(){
		return next_position;
	}
	void setPosition(String pos){
		position = pos;
	}
	void setNextPosition(String next_pos){
		next_position = next_pos;
	}
	void setEval(int ev)
	{
		this.ev = ev;
	}
	void setParentObj(Gameboard parent_obj){
		this.parent_obj = parent_obj;
	}
	Gameboard getParentObj(){
		return parent_obj;
	}
	void setProgress(int progress){
		this.can_progress = progress;
	}
	int  getProgress(){
		return can_progress;
	}
	int getEval(){
		return ev;
	}
	String getEvalString(){
		String answer;
		if(ev == -9999999 ){
			answer = "-Infinity";
		}
		else if(ev == 9999999){
			answer=  "Infinity";
		}
		else
			answer = ev+"";
		return answer;
	}
	void setChildren( ArrayList<Gameboard> children){
		this.children= children;
	}
	ArrayList<Gameboard> getChildren(){
		return children;
	}

	String getParent(){
		return parent_node;
	}
	int getLevel(){
		return level;
	}
	int getTurn(){
		return turn;
	}
	String getNode(){
		if(level == 0)
			this.position = "root";
		return position;
	}
	void setParent(String parent){
		this.parent_node = parent;
	}
	void setLevel(int level){
		this.level = level;
	}
	String getGameboard(){
		return game_board;
	}
}

class pits{
	int value;
	int gb_index;
	pits(int value , int gb_index){
		this.value = value;
		this.gb_index = gb_index;	
	}
	void updateValue(int value)
	{
		this.value =  value;
	}
	void updateIndex(int index)
	{
		this.gb_index = index;
	}
	int getValue()
	{
		return value;
	}
	int getIndex()
	{
		return gb_index;
	}
}
public class mancala {
	static  ArrayList<pits>  game_board = new ArrayList<pits>();
	static  String game_board_values = "";
	static ArrayList<Gameboard> possible_game_states = new ArrayList<Gameboard>();
	static int mancala_1;
	static int mancala_2;
	static int turn ;
	static int level;
	static int method_to_be_used;
	static ArrayList<Integer> player_1 = new ArrayList<Integer>();
	static  ArrayList<Integer>player_2= new ArrayList<Integer>();
	static  Map<Integer , Integer> opposite_pits = new  HashMap<Integer , Integer>();
	static  Map<Integer, pits> player_1_pits = new HashMap<Integer , pits>();
	static  Map<Integer, pits> player_2_pits = new HashMap<Integer , pits>();
	static  Map<Integer, pits> mancalas = new HashMap<Integer , pits>();
	static  ArrayList<Integer> player_1_index = new ArrayList<Integer>();
	static  ArrayList<Integer> player_2_index = new ArrayList<Integer>();
	static  ArrayList<String> logger_file = new ArrayList<String>();
	static int[] org_game_board_values ;
	static int num_of_pits;
	static int n;
	static int player;
	static int opponent;
	static String print;

	static void initialize_gameboard(){
		int i = 0;
		int counter = 1;
		for(int value : player_1){
			pits pit_obj = new pits(value, i);
			game_board.add(pit_obj);
			player_1_pits.put(counter,pit_obj);
			player_1_index.add(i);
			counter++;
			i++;	        
		}
		i=n;
		pits pit = new pits(mancala_1, i);
		game_board.add(pit);
		mancalas.put(1,pit);
		i=num_of_pits-2;
		for(int j = num_of_pits-2 ;  j > n ; j--){
			pits new_pit = new pits(-1,j);
			game_board.add(new_pit);
		}
		for(counter = 1; counter <=n ; counter++){
			pits pit_obj = game_board.get(i);
			pit_obj.updateValue(player_2.get(counter-1));
			pit_obj.updateIndex(i);
			game_board.set(i,pit_obj);
			player_2_pits.put(counter,pit_obj);
			player_2_index.add(i);
			i= i-1;
		}
		i = num_of_pits-1;
		pit = new pits(mancala_2, i);
		mancalas.put(2,pit);
		game_board.add(pit);

		//set the opposite pit hash map
		for(int j = 0 ; j < n ; j++){	
			pits opposite_pit = player_2_pits.get(j+1);
			opposite_pits.put(j,opposite_pit.getIndex());
		}
		Map<Integer,Integer> temp = new HashMap<Integer,Integer>();
		for(int key : opposite_pits.keySet()){
			int value = opposite_pits.get(key);
			temp.put(value,key);
		}
		for(int key : temp.keySet()){
			opposite_pits.put(key, temp.get(key));

		}
		for(int key : opposite_pits.keySet()){
			//System.out.println("The pit is " + key +"and the  opposite pit is " + opposite_pits.get(key));
		}
		game_board_values = "";
		for(int k = 0 ; k < num_of_pits; k++){ 
			game_board_values += game_board.get(k).getValue()+",";
		}
		game_board_values = game_board_values.substring(0,game_board_values.length()-1);
		Gameboard gb = new Gameboard(-1,-9999999,game_board_values,-1,1);       
		gb.setLevel(0);

	}

	static void get_next_best_move(Gameboard state){
		ArrayList<Gameboard> children = state.getChildren();
		Gameboard child = null ;
		if(children.isEmpty()== true ){
			if(state.getProgress() == 1){
				for(int i = 1 ; i <=n ; i++){
					//System.out.println("For child number "+ i +"of min node");
					Gameboard next_state = construct_graph(state, i , level);
					if(next_state != null){
						String g_board_values = "";
						g_board_values = next_state.getGameboard();
						List<String> game_board_values = Arrays.asList(g_board_values.split(","));
						for(int j = 0; j< num_of_pits; j++)
						{
							game_board.get(j).updateValue(Integer.parseInt(game_board_values.get(j))) ;
						}
					}
			    }
		//	System.out.println("The game board is in the end state");
		}
		}
		else{

			for(int k = 0 ; k < children.size() ; k++){
				child = children.get(k);
				if(child.getEval() == state.getEval()){
					break;
				}
			}
			if(child.getProgress() == 0){
				String g_board_values = "";
				g_board_values = child.getGameboard();
				List<String> game_board_values = Arrays.asList(g_board_values.split(","));
				for(int j = 0; j< num_of_pits; j++)
				{
					game_board.get(j).updateValue(Integer.parseInt(game_board_values.get(j))) ;
				}
				return;
			}
			else {
				get_next_best_move(child);
			}
		}

	}
	static void display_value(){
		System.out.println("Player 1 : ");
		for(int value : player_1){
			System.out.print(value);
		}
		System.out.println("");
		System.out.println("Player 2 : ");
		for(int value : player_2){
			System.out.print(value);
		}
		System.out.println("");
		System.out.println("Game board");
		for(int i = 0; i < num_of_pits ; i++)
		{
			System.out.println(game_board.get(i).getValue() +"and index is" +game_board.get(i).getIndex()  );
		}
		System.out.println("");
		System.out.println("Player 1 pit info  :");
		for(int value : player_1_pits.keySet()){
			System.out.println("For pit : " + value + " the coins left are " + player_1_pits.get(value).getValue()+
					" and the index on gb is " + player_1_pits.get(value).getIndex());
		}
		System.out.println("");
		System.out.println("Player 2 pit info  :");
		for(int value : player_2_pits.keySet()){
			System.out.println("For pit : " + value + " the coins left are " + player_2_pits.get(value).getValue()+
					" and the index on gb is " + player_2_pits.get(value).getIndex());
		}
		System.out.println("");
		System.out.println("Mancalas info  :");
		for(int value :mancalas.keySet()){
			System.out.println("For player " + value + "'s mancala there are  " + mancalas.get(value).getValue()+
					" coins and the index on gb is " + mancalas.get(value).getIndex());
		}

	}
	/* To display game board */	
	static void display_gameboard(){
		String edges_between = "";
		String player_2="";
		String mancala="";
		String player_1="";
		int i =0;
		while(i < n)
		{
			player_1 = player_1 +"|   | " + game_board.get(i).getValue() + " ";
			edges_between = edges_between + "|   |   ";
			i=i+1;
		}
		i=n+1;
		while( i < num_of_pits-1)
		{
			player_2 = player_2+"|   | " + game_board.get(i).getValue() + " ";		
			i++;
		}
		i = n;
		mancala = "| "+game_board.get(num_of_pits-1).getValue() +" " + edges_between +game_board.get(n).getValue() +" |" ;
		System.out.println("------------------------------------------------------------------------------------------------------------------");
		System.out.println("|   | " +new StringBuilder(player_2).reverse().toString() );
		System.out.println(mancala);
		System.out.println(player_1 + "|     | ");
		System.out.println("-----------------------------------------------------------------------------------------------------------------");
	}
	static int game_over(){
		int flag_1 = 1;
		int flag_2 = 1;
		for(int i :player_1_pits.keySet()){
			if(player_1_pits.get(i).getValue() != 0)
				flag_1 = 0;
		}
		for(int i :player_2_pits.keySet()){
			if(player_2_pits.get(i).getValue() != 0)
				flag_2 = 0;
		}
		if(flag_1 == 1 || flag_2 == 1)
			return 1;
		else
			return 0;
	}
	static void end_game_pass(){
		for(int i :player_1_pits.keySet()){
			if(player_1_pits.get(i).getValue() != 0)
			{ 
				int mancala_1_current_value = mancalas.get(1).getValue();
				int player_1_pit_value = player_1_pits.get(i).getValue();
				player_1_pits.get(i).updateValue(0);
				mancalas.get(1).updateValue(mancala_1_current_value  + player_1_pit_value );				
			}
		}
		for(int i :player_2_pits.keySet()){
			if(player_2_pits.get(i).getValue() != 0){
				int mancala_2_current_value = mancalas.get(2).getValue();
				int player_2_pit_value = player_2_pits.get(i).getValue();
				player_2_pits.get(i).updateValue(0);
				mancalas.get(2).updateValue(mancala_2_current_value  + player_2_pit_value );			
			}

		}
	}

	//alpha beta 
	static Gameboard alpha_beta_decision(Gameboard start){
		start.setAlpha(-9999999);
		start.setBeta(9999999);			
		maximum_alpha_beta(start);            
		return start;
	}

	static int max( int x , int y){
		if(x>=y)
			return x;
		else return y;
	}
	static int min( int x , int y){
		if( x <= y)
			return x;
		else return y;
	}


	static void minimum_alpha_beta(Gameboard state){
		ArrayList  <Gameboard> children = state.getChildren();
		print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString()+","+state.getAlphaVal()+","+state.getBetaVal();	
		logger_file.add(print);
		for(int i = 1 ; i <=n ; i++){
		//	System.out.println("Min");
		//	System.out.println("For child number "+ i +"of min node");
			Gameboard child = construct_graph(state, i , level);
			if(child != null){
				//for a child belonging to diff player
				if(child.getProgress() == 0){
					child.setEval(-9999999);
					child.setAlpha(state.getAlpha());
					child.setBeta(state.getBeta());
					maximum_alpha_beta(child);
					children.add(child);
					int next_move_eval  = child.getEval();
					if(next_move_eval < state.getEval()){
						state.setEval(next_move_eval);
						state.setNextPosition(child.getNode());			
					}	
					if(state.getEval() <= state.getAlpha()){
						// System.out.println("Pruning");
						print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString()+","+state.getAlphaVal()+","+state.getBetaVal();	
						logger_file.add(print);
						break;
					}
					if(state.getBeta()> min(child.getEval(),child.getBeta())){
						state.setBeta(child.getEval());
					}
				}
				//for a child beloging to same player
				else{
					child.setEval(9999999);
					child.setAlpha(state.getAlpha());
					child.setBeta(state.getBeta());
					minimum_alpha_beta(child);
					children.add(child);
					int next_move_eval  = child.getEval();
					if(next_move_eval < state.getEval()){
						state.setEval(next_move_eval);
						state.setNextPosition(child.getNode());			
					}	
					if(state.getEval() <= state.getAlpha()){
						//System.out.println("Pruning");
						print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString()+","+state.getAlphaVal()+","+state.getBetaVal();	
						logger_file.add(print);
						break;
					}
					if(state.getBeta()>min(child.getEval(),child.getBeta())){
						state.setBeta(child.getEval());
					}
				}
				print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString()+","+state.getAlphaVal()+","+state.getBetaVal();	
				logger_file.add(print);
			}
		}	 
		state.setChildren(children);
		if(children.isEmpty() == true){		
			String g_board_values = "";
			g_board_values = state.getGameboard();
			List<String> game_board_values = Arrays.asList(g_board_values.split(","));
			for(int j = 0; j< num_of_pits; j++)
			{
				game_board.get(j).updateValue(Integer.parseInt(game_board_values.get(j))) ;
			}
			if(state.getLevel() == level){
				logger_file.remove(logger_file.size()-1);	
			}
			int eval = mancalas.get(player).getValue()- mancalas.get(opponent).getValue();
			state.setEval(eval);
			print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString()+","+state.getAlphaVal()+","+state.getBetaVal();
			logger_file.add(print);
			return;			
		}
	}	

	static void maximum_alpha_beta(Gameboard state){
		ArrayList<Gameboard> children = state.getChildren();
		print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString()+","+state.getAlphaVal()+","+state.getBetaVal();	
		logger_file.add(print);
		for(int i = 1 ; i <=n ; i++){
		//	System.out.println("For child number "+ i +"of max node");
		//	System.out.println("Max");
			Gameboard child = construct_graph(state, i , level);
			if(child != null){
				if(child.getProgress() == 0)
				{ 
					child.setEval(9999999);
					child.setAlpha(state.getAlpha());
					child.setBeta(state.getBeta());
					minimum_alpha_beta(child);
					children.add(child);
					int next_move_eval  = child.getEval();
					if(next_move_eval > state.getEval()){
						state.setEval(next_move_eval);
						state.setNextPosition(child.getNode());	
					}
					if(state.getEval() >= state.getBeta()){
						// System.out.println("Pruning");
						print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString()+","+state.getAlphaVal()+","+state.getBetaVal();	
						logger_file.add(print);
						break;
					}
					if(state.getAlpha()<max(child.getEval(),child.getAlpha())){
						state.setAlpha(child.getEval());
					}
				}
				else{
					child.setEval(-9999999);
					child.setAlpha(state.getAlpha());
					child.setBeta(state.getBeta());
					maximum_alpha_beta(child);
					children.add(child);
					int next_move_eval  = child.getEval();
					if(next_move_eval > state.getEval()){
						state.setEval(next_move_eval);
						state.setNextPosition(child.getNode());	
					}

					if(state.getEval() >= state.getBeta()){
						//System.out.println("Pruning");
						print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString()+","+state.getAlphaVal()+","+state.getBetaVal();	
						logger_file.add(print);	
						break;
					}
					if(state.getAlpha()<max(child.getEval(),child.getAlpha())){
						state.setAlpha(child.getEval());
					}
				}
				print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString()+","+state.getAlphaVal()+","+state.getBetaVal();	
				logger_file.add(print);
			}
		}
		state.setChildren(children);
		if(children.isEmpty()){			
			String g_board_values = "";
			g_board_values = state.getGameboard();
			List<String> game_board_values = Arrays.asList(g_board_values.split(","));
			for(int j = 0; j< num_of_pits; j++)
			{
				game_board.get(j).updateValue(Integer.parseInt(game_board_values.get(j))) ;
			}
			if(state.getLevel() == level){
				logger_file.remove(logger_file.size()-1);	
			}
			int eval = mancalas.get(player).getValue()- mancalas.get(opponent).getValue();	
			state.setEval(eval);
			print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString()+","+state.getAlphaVal()+","+state.getBetaVal();	
			logger_file.add(print);
			return;	
		}
	}


	//Minmax code starts from here 
	static Gameboard min_max_decision(Gameboard start){
		maximum(start);			
		return start;
	}

	static void minimum(Gameboard state){
		//System.out.println("Min");
		ArrayList<Gameboard> children = new ArrayList<Gameboard>();
		print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString();
		logger_file.add(print);		
		for(int i = 1 ; i <=n ; i++){
			//System.out.println("Min");
			//System.out.println("For child number "+ i +"of min node");
			Gameboard child = construct_graph(state, i , level);
			if(child != null){
				if(child.getProgress() == 0){
					child.setEval(-9999999);
					maximum(child);
					children.add(child);
				}
				else{
					child.setEval(9999999);
					minimum(child);	
					children.add(child);
				}

				int next_move_eval  = child.getEval();
				if(next_move_eval < state.getEval()){
					state.setEval(next_move_eval);
					state.setNextPosition(child.getNode());			
				}	
				print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString();
				logger_file.add(print);
			}
		}  
		state.setChildren(children);

		if(state.getChildren().isEmpty() == true){
			String g_board_values = "";
			g_board_values = state.getGameboard();
			List<String> game_board_values = Arrays.asList(g_board_values.split(","));
			for(int j = 0; j< num_of_pits; j++)
			{
				game_board.get(j).updateValue(Integer.parseInt(game_board_values.get(j))) ;
			}
			if(state.getLevel() == level && state.getProgress() == 0){
				logger_file.remove(logger_file.size()-1);	
			}
			int eval = mancalas.get(player).getValue()- mancalas.get(opponent).getValue();
			state.setEval(eval);
			print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString();
			logger_file.add(print);	
			return;			
		}
	}		

	static void maximum(Gameboard state){
	//	System.out.println("Max");
		ArrayList<Gameboard> children = new ArrayList<Gameboard>();
		print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString() ;
		logger_file.add(print);
		for(int i = 1 ; i <=n ; i++){
		//	System.out.println("For child number "+ i +"of max node");
		//	System.out.println("Max");
			Gameboard child = construct_graph(state, i , level);
			if(child != null){
				if(child.getProgress() == 0)
				{ 
					child.setEval(9999999);
					minimum(child);
					children.add(child);
				}
				else{
					child.setEval(-9999999);
					maximum(child);	 
					children.add(child);
				}	    
				int next_move_eval  = child.getEval();
				if(next_move_eval > state.getEval()){
					state.setEval(next_move_eval);
					state.setNextPosition(child.getNode());	
				}
				print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString();
				logger_file.add(print);
			}
		}
		state.setChildren(children);

		if(state.getChildren().isEmpty() == true){			
			String g_board_values = "";
			g_board_values = state.getGameboard();
			List<String> game_board_values = Arrays.asList(g_board_values.split(","));
			for(int j = 0; j< num_of_pits; j++)
			{
				game_board.get(j).updateValue(Integer.parseInt(game_board_values.get(j))) ;
			}
			if(state.getLevel() == level && state.getProgress() == 0){
				logger_file.remove(logger_file.size() -1 );
			}
			int eval = mancalas.get(player).getValue()- mancalas.get(opponent).getValue();	
			state.setEval(eval);
			print = state.getNode()+"," +state.getLevel()+ ","+ state.getEvalString();
			logger_file.add(print);
			return;	
		}
	}
	
	static Gameboard construct_graph(Gameboard start , int i, int level){
		Gameboard board = start;
		int parent_level = board.getLevel();
		int parent_turn = board.getTurn();
		String parent_game_state = board.getGameboard();
		List<String> game_board_values = Arrays.asList(parent_game_state.split(","));
		for(int j = 0; j< num_of_pits; j++)
		{
			game_board.get(j).updateValue(Integer.parseInt(game_board_values.get(j))) ;
		}
		if( (parent_level < level) || ((parent_level == level) && (board.getProgress() == 1))){

			for(int j = 0; j< num_of_pits; j++)
			{
				game_board.get(j).updateValue(Integer.parseInt(game_board_values.get(j))) ;
			}
		//	display_gameboard();
			pits pit;
			if(parent_turn == 2 && board.getProgress() == 0){
				turn = 1;
				//System.out.println("Option one");
			}
			else if(parent_turn == 2 && board.getProgress() == 1){
				turn = 2;
				//System.out.println("Option two");
			}
			else if(parent_turn == 1 && board.getProgress() == 0){
				turn = 2;
				//System.out.println("Option three");
			}
			else  if(parent_turn == 1 && board.getProgress() == 1){           
				turn = 1;
				//System.out.println("Option four");
			}
			else{
			//	System.out.println("***Something went wrong");	   
			}
			if(turn == 1){
				pit = player_1_pits.get(i);	
				if(pit.getValue()!=0){
					Gameboard move = get_move(board, i , parent_level);
					return move;
				}
				else {
				//	System.out.println("Cant do that move");
				}
			}
			else{
				pit = player_2_pits.get(i);	
				if(pit.getValue()!=0){
					Gameboard move = get_move(board, i , parent_level);
					return move;
				}	
				else {
					//System.out.println("Cant do that move");
				}
			}

		}
		return null;
	}
	static Gameboard get_move(Gameboard parent , int pit_number , int parent_level){
		pits pit;
		String game_board_array = "";
		int can_progress = 0;
		int child_turn = turn;
		int ev;
		ev = 0;
		int last_pit_number = 0;
		int last_pit_prev_value = 0;
		int child_level;
		if(turn == 1){
			pit = player_1_pits.get(pit_number);
			if(parent.getTurn() == turn)
				child_level = parent.getLevel();
			else
				child_level =  parent.getLevel()+1;
		}
		else{
			pit = player_2_pits.get(pit_number);
			if(parent.getTurn() == turn)
				child_level = parent.getLevel();
			else
				child_level =  parent.getLevel()+1;
		}	

		int num_of_coins = pit.getValue();
		int gb_index = pit.getIndex();
		pit.updateValue(0);
		game_board.set(gb_index,pit);
		int index = gb_index+1;
		for(int i = 1 ; i <= num_of_coins ; i++)
		{   last_pit_number = index%num_of_pits;
		if(turn == 1 && last_pit_number == mancalas.get(2).getIndex()){		    	
			index ++;
		}
		else if(turn == 2 && last_pit_number ==  mancalas.get(1).getIndex()){
			index++;
		}
		last_pit_number = index%num_of_pits;
		pit = game_board.get(index%num_of_pits);
		last_pit_prev_value = pit.getValue();
		pit.updateValue(pit.getValue()+1);
		game_board.set(index%num_of_pits,pit);
		index++;
		}		

		if(last_pit_number == mancalas.get(1).getIndex() && turn == 1){
			can_progress = 1;
			//System.out.println("The last pit is a MANCALA");
			int check_again = game_over();
			if(check_again == 1)
			{ //  System.out.println("end_game");
				end_game_pass();
			}
		}
		else if(last_pit_number == mancalas.get(2).getIndex() && turn == 2){
			can_progress = 1;
			//System.out.println("The last pit is a MANCALA");
			int check_again = game_over();
			if(check_again == 1)
			{    //System.out.println("end_game");
				end_game_pass();
			}
		}
		else if(last_pit_prev_value == 0 && turn == 1 && last_pit_number != mancalas.get(1).getIndex()){
			can_progress = 0;
			pit = game_board.get(last_pit_number);
			if(player_1_index.contains(pit.getIndex())){
				int opposite_pit_number = opposite_pits.get(last_pit_number);
				pits opposite_pit = game_board.get(opposite_pit_number);;
				int opp_pit_gb_index = opposite_pit.getIndex();
				int coins_obtained = opposite_pit.getValue()+pit.getValue() +mancalas.get(1).getValue();
				pit.updateValue(0);
				opposite_pit.updateValue(0);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
				game_board.set(last_pit_number,pit);
				game_board.set(opp_pit_gb_index,opposite_pit);	
				mancalas.get(1).updateValue(coins_obtained);
				game_board.set(mancalas.get(1).getIndex(),mancalas.get(1));
				//System.out.println("The last pit is EMPTY");
			}
			int check_again = game_over();
			if(check_again == 1){
				 System.out.println("end_game");
				end_game_pass();	
			}
		}
		else if(last_pit_prev_value == 0 && turn == 2 &&  last_pit_number != mancalas.get(2).getIndex()){
			can_progress = 0;
			pit = game_board.get(last_pit_number);
			if(player_2_index.contains(pit.getIndex())){
				int opposite_pit_number = opposite_pits.get(last_pit_number);
				pits opposite_pit = game_board.get(opposite_pit_number);;
				int opp_pit_gb_index = opposite_pit.getIndex();
				int coins_obtained = opposite_pit.getValue()+pit.getValue() +mancalas.get(2).getValue();
				pit.updateValue(0);
				opposite_pit.updateValue(0);
				game_board.set(last_pit_number,pit);
				game_board.set(opp_pit_gb_index,opposite_pit);	
				//System.out.println("The opposite_pit has number" + opposite_pit.getIndex() +" and has value" +opposite_pit.getValue());
				//System.out.println("The pit has number" + pit.getIndex() +" and has value" +pit.getValue());
				mancalas.get(2).updateValue(coins_obtained);
				game_board.set(mancalas.get(2).getIndex(),mancalas.get(2));
			}
			int check_again = game_over();
			if(check_again == 1){
				 System.out.println("end_game");
				end_game_pass();	
				
			}
		}
		else {
			can_progress = 0;
			int check_again = game_over();
			if(check_again == 1){
				 System.out.println("end_game");
				end_game_pass();	
		}
		}
		//display_gameboard();
		for(int j = 0; j< num_of_pits; j++)
		{
			game_board_array += game_board.get(j).getValue() + "," ;
		}
		game_board_array = game_board_array.substring(0 , game_board_array.length() - 1 );
		Gameboard gameboard = new Gameboard(pit_number, ev ,  game_board_array, child_turn , can_progress);
		gameboard.setLevel(child_level);
		gameboard.setParentObj(parent);
		gameboard.setParent(parent.getNode());
		gameboard.setAlpha(-9999999);
		gameboard.setBeta(9999999);
		return gameboard ;
	}


	public static void main(String[] args){
		BufferedReader bufferedReader = null;
		BufferedWriter next_state = null;
		BufferedWriter logger = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(args[0]));
			next_state = new BufferedWriter(new FileWriter("/home/preethi/workspace/mancala/src/mancala/next_state.txt"));
			logger = new BufferedWriter(new FileWriter("/home/preethi/workspace/mancala/src/mancala/traverse_log.txt"));
		    //PrintStream out = new PrintStream(new FileOutputStream("/home/preethi/workspace/mancala/src/mancala/output-final.txt"));
		    //System.setOut(out);
			method_to_be_used = Integer.parseInt(bufferedReader.readLine());
			player = Integer.parseInt(bufferedReader.readLine());
			level = Integer.parseInt(bufferedReader.readLine());
			String player_2_value_pits =  bufferedReader.readLine();
			String player_1_value_pits =  bufferedReader.readLine();
			mancala_2 = Integer.parseInt(bufferedReader.readLine());
			mancala_1 = Integer.parseInt(bufferedReader.readLine());
			String[] info = player_1_value_pits.split(" ");
			int i = 0;
			n = info.length;
			while(i < n)
			{
				player_1.add(Integer.parseInt(info[i]));
				i++;
			}
			info = player_2_value_pits.split(" ");
			i = 0;
			while(i < n)
			{
				player_2.add(Integer.parseInt(info[i]));
				i++;
			}
			num_of_pits = n*2 + 2;
			org_game_board_values= new int[num_of_pits];
			if(player == 1){
				opponent = 2;
			}
			else {
				opponent = 1;
			}
			if(player == 1)
				turn = 2 ;
			else
				turn = 1;

			initialize_gameboard();
			//if method one is used
			if(method_to_be_used == 1){
				level = 1;
				Gameboard gb = new Gameboard(-1,-9999999,game_board_values,turn,0); 
				//construct_graph(gb,level);      
				Gameboard result = min_max_decision(gb);   
				get_next_best_move(result);
				logger.write("Node,Depth,Value");
				logger.newLine();
				for(String log : logger_file)
				{ 
					logger.write(log);
					logger.newLine();
				}
				String insert_line ="";
				for(int k: player_2_pits.keySet())
				{
					insert_line = insert_line + player_2_pits.get(k).getValue() + " ";
				}
				next_state.write(insert_line);
				next_state.newLine();
				insert_line ="";
				for(int k: player_1_pits.keySet())
				{
					insert_line = insert_line + player_1_pits.get(k).getValue() + " ";
				}
				next_state.write(insert_line);
				next_state.newLine();
				next_state.write(game_board.get(mancalas.get(2).getIndex()).getValue()+"");
				next_state.newLine();
				next_state.write(game_board.get(mancalas.get(1).getIndex()).getValue()+"");
				next_state.newLine();
			//display_gameboard();
			}

			//if method 2 is used
			if(method_to_be_used == 2){
				Gameboard gb = new Gameboard(-1,-9999999,game_board_values,turn,0); 
				// construct_graph(gb,level);      
				Gameboard result =  min_max_decision(gb);  
				get_next_best_move(result);
				logger.write("Node,Depth,Value");
				logger.newLine();
				for(String log : logger_file)
				{ 
					logger.write(log);
					logger.newLine();
				}
				String insert_line ="";
				for(int k: player_2_pits.keySet())
				{
					insert_line = insert_line + game_board.get(player_2_pits.get(k).getIndex()).getValue() + " ";
				}
				next_state.write(insert_line);
				next_state.newLine();
				insert_line ="";
				for(int k: player_1_pits.keySet())
				{
					insert_line = insert_line + game_board.get(player_1_pits.get(k).getIndex()).getValue() + " ";
				}
				next_state.write(insert_line);
				next_state.newLine();
				next_state.write(game_board.get(mancalas.get(2).getIndex()).getValue()+"");
				next_state.newLine();
				next_state.write(game_board.get(mancalas.get(1).getIndex()).getValue()+"");
				next_state.newLine();
				//  display_gameboard();
			}

			//method 3 is used
			if(method_to_be_used == 3){
				Gameboard gb = new Gameboard(-1,-9999999,game_board_values,turn,0); 
				//construct_graph(gb,level);      
				Gameboard result = alpha_beta_decision(gb);  
				get_next_best_move(result);
				logger.write("Node,Depth,Value,Alpha,Beta");
				logger.newLine();
				for(String log : logger_file)
				{ 
					logger.write(log);
					logger.newLine();
				}
				String insert_line ="";
				for(int k: player_2_pits.keySet())
				{
					insert_line = insert_line + game_board.get(player_2_pits.get(k).getIndex()).getValue() + " ";
				}
				next_state.write(insert_line);
				next_state.newLine();
				insert_line ="";
				for(int k: player_1_pits.keySet())
				{
					insert_line = insert_line + game_board.get(player_1_pits.get(k).getIndex()).getValue() + " ";
				}
				next_state.write(insert_line);
				next_state.newLine();
				next_state.write(game_board.get(mancalas.get(2).getIndex()).getValue()+"");
				next_state.newLine();
				next_state.write(game_board.get(mancalas.get(1).getIndex()).getValue()+"");
				next_state.newLine();
				//   display_gameboard();
			}


			logger.close();
			next_state.close();
			bufferedReader.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}