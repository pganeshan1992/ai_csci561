package waterFlow;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;


//State keeps track of the name of the node 
class State
{
	String state;
	State(String state){
		this.state = state ;
	}
	String getState(){
	return state;
	}
}

//Node keeps tack of info regarding a state
class Node
{
    State state;
    int parent;
    String parent_state;
    int path_cost ;
    int node;
    int water_entry_time;
    ArrayList <Node> children;
    
    Node(int node , State state){  	
    	this.node = node;
    	this.state = state;
    	this.parent = -1;
    	this.parent_state = "$$$";
    	this.path_cost = 0;
    	this.state = state;
    	this.water_entry_time = 0;  	
    }
    
    Node(State state){    	
    	this.node = -1;
    	this.state = state;
    	this.parent = -1;
    	this.path_cost = 0;
    	this.state = state;
    	this.parent_state = "$$$";
    	this.water_entry_time = 0;   	
    }
    
     int getNode(){
    	 return node;
     }
     
     int getWaterEntryTime(){
    	 return water_entry_time;
     }

     String getState()
     {
    	 return state.getState();
     }
     
    int getParent()
     {
    	 return parent;
     }
    
    String getParentState()
    {
    	return parent_state;
    }
    
     int getPathCost(){
    	return path_cost;
     }
     
    void setParent(int i){
    	this.parent = i;
    }
    
     void setPathCost(int path_cost){
    	 this.path_cost = path_cost;
     }
     
     void setNode(int node){
    	 this.node = node;
     }
     
     void setParentState(String parent_state){
    	 this.parent_state = parent_state;
     }
     
     void setWaterEntryTime(int water_entry_time){
    	 this.water_entry_time = water_entry_time;
     }

    void display(){
    	System.out.println("Node is " +node);
    	System.out.print(" Node's parent is :" +parent);
    	System.out.print(" Node;s state is :" +state);
    	System.out.print(" The path cost of the node is: " +path_cost);
    
   }
}


class SortNodes implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        return o1.getState().compareTo(o2.getState());
    }
}

class SortNodesDesc implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        return o2.getState().compareTo(o1.getState());
    }
}

class SortNodesBasedPath implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
       if(o1.getPathCost() < o2.getPathCost())
    	   return -1 ;
       else if (o1.getPathCost() > o2.getPathCost())
            return 1 ;
       else if (o1.getPathCost() == o2.getPathCost())
            return o1.getState().compareTo(o2.getState());
       else 
    	   return 0;      
    }
}

class Edges{
	String start_vertex;
	String end_vertex;
	int path_cost;
	int num_off_periods;
	ArrayList<String> off_periods = new ArrayList<String>();
	ArrayList<Integer> off_start_time = new ArrayList<Integer>();

	Edges(String start_vertex , String end_vertex , int path_cost , int num_off_periods , ArrayList<String> off_periods , ArrayList<Integer> start_off_time){
	this.start_vertex = start_vertex;
	this.end_vertex = end_vertex;
	this.path_cost = path_cost;
	this.num_off_periods = num_off_periods;
	this.off_periods = off_periods;	
	this.off_start_time = start_off_time;
	}
	String getStartVertex(){
		return start_vertex;
	}
	String getEndVertex(){
		return end_vertex;
	}
	int  getPathCost(){
		return path_cost;
	}
	ArrayList<String> getOffPeriods(){
		return off_periods;
	}
	ArrayList<Integer> getWaterOffStartingTimes(){
		return off_start_time;
	}
	int getNumOffPeriods(){
		return num_off_periods;
	}
   void display(){
	   System.out.println(start_vertex + end_vertex + path_cost + num_off_periods + off_periods + off_start_time);
   }
}



class TestCases{
	ArrayList <String> start = new ArrayList<String>();
    ArrayList <String> middle = new ArrayList<String>();
    ArrayList <String> end = new ArrayList<String>();
    ArrayList <String> lines = new ArrayList<String>();
    ArrayList <String> states = new ArrayList<String>();
    HashMap <String, State> states_list = new HashMap<String ,  State>();
    Map <String , Node> nodes = new HashMap <String , Node>();
    Map<String , Edges> edges = new HashMap<String , Edges>();
	String strategy ;
	String start_node;
	String end_nodes;
	String middle_nodes;
    int start_time ;	
    int counter = 1;
    
    TestCases(ArrayList<String> start ,
    ArrayList<String> middle ,
    ArrayList<String> end ,
    ArrayList<String> lines,
    ArrayList<String> states,
    HashMap <String, State> states_list ,
    Map <String , Node> nodes,
    Map<String , Edges> edges,
	String strategy,
	String start_node,
	String end_nodes,
	String middle_nodes,
    int start_time){
    	this. start = start;
        this.middle = middle;
        this.end = end;
        this.lines = lines;
        this.states = states;
        this.states_list  = states_list;
        this.nodes = nodes;
        this.edges = edges;
    	this.strategy = strategy;
    	this.start_node = start_node;
    	this.end_nodes = end_nodes;
    	this.middle_nodes = middle_nodes;
        this.start_time = start_time;
    }
   
	ArrayList<Node> expansion(Node start_node ){
		 ArrayList<Node> children = new ArrayList<Node>();
		 for (Entry<String, Edges> e : edges.entrySet()) {
			 if (e.getKey().startsWith(start_node.getState() +"-")) {				 
			     Node child = new Node(states_list.get(e.getValue().getEndVertex()));
			     child.setNode(-1);
			     child.setParent(start_node.getNode());
			     child.setParentState(start_node.getState());
		    	 child.setPathCost(start_node.getPathCost()+1);
		    	 children.add(child);
			   }
		 }
		 if(children.isEmpty()){
			Node empty = new Node(-999 , new State("$$$"));
			children.add(empty);
		 }
		 Collections.sort(children ,new SortNodes());
		 for(Node child : children ){
			 if(child.getNode() == -1){
				 counter = counter + 1;
				 child.setNode(counter);
		    	 System.out.println("The child is node -" +child.getNode() +"and has state - " +child.getState() + "and has parent : " +child.getParent() + "And has path cost : " + child.getPathCost());
			 }
		 }
		 return children;
	 }
	 
	  ArrayList<Node> expansion_ucs(Node start_node ){
		 ArrayList<Node> children = new ArrayList<Node>();
		 for (Entry<String, Edges> e : edges.entrySet()) {
			 if (e.getKey().startsWith(start_node.getState() +"-")) {
				 e.getValue().display();
				 System.out.println("The children array has a size " +children.size() );
			     Node child = new Node(states_list.get(e.getValue().getEndVertex()));
			     System.out.println("The child state " +child.getState() + " and key is " +e.getKey()) ;
			     child.setParent(start_node.getNode());
			     child.setNode(-1);
			     child.setParentState(start_node.getState());
		    	 child.setPathCost(e.getValue().getPathCost() + start_node.getPathCost());
		    	 child.setWaterEntryTime((start_node.getWaterEntryTime() + e.getValue().getPathCost())%24);
		    	 ArrayList<Integer> off_timings = e.getValue().getWaterOffStartingTimes();
		    	 System.out.println("The parent has water entry time " +start_node.getWaterEntryTime());
		    	 System.out.println("The path cost time "+ start_node.getPathCost()%24);
		    	 System.out.println("The off times are : " +off_timings);
		    	 System.out.println("The water entry times is : " +child.getWaterEntryTime());
		    	 if(!off_timings.contains(start_node.getWaterEntryTime()))
		    	 children.add(child);
		    	 else
		    	 System.out.println("The child " +child.getState() +" is not considered becasue the water entry time is " +off_timings);
		    	 System.out.println("The children array has a size " +children.size() );
			   }
		 }
		 if(children.isEmpty()){
			Node empty = new Node(-999 , new State("$$$"));
			System.out.println("********" +empty.getState());
			children.add(empty);
		 }
		 Collections.sort(children ,new SortNodesBasedPath());
		 for(Node child : children ){
			 if(child.getNode() == -1){
				 counter = counter + 1;
				 child.setNode(counter);
		    	 System.out.println("The child is node -" +child.getNode() +"and has state - " +child.getState() + "and has parent : " +child.getParent() + "And has path cost : " + child.getPathCost());
			 }
		 }
		 return children;
	 }
	  
	 ArrayList<Node> expansion_dfs(Node start_node ){
		 ArrayList<Node> children = new ArrayList<Node>();
		 for (Entry<String, Edges> e : edges.entrySet()) {
			 if (e.getKey().startsWith(start_node.getState() +"-")) {
				 
			     Node child = new Node(states_list.get(e.getValue().getEndVertex()));
			     child.setNode(-1);
			     child.setParent(start_node.getNode());
			     child.setParentState(start_node.getState());
		    	 child.setPathCost(start_node.getPathCost()+1);
		    	 children.add(child);
			   }
		 }
		 if(children.isEmpty()){
			Node empty = new Node(-999 , new State("$$$"));
			children.add(empty);
		 }
		 Collections.sort(children ,new SortNodesDesc());
		 for(Node child : children ){
			 if(child.getNode() == -1){
				 counter = counter + 1;
				 child.setNode(counter);
		    	 System.out.println("The child is node -" +child.getNode() +"and has state - " +child.getState() + "and has parent : " +child.getParent());
			 }
		 }
		 return children;
	 }
	 
	void removeNode(Queue<Node> queue_node , Node child){
		 for(Node node : queue_node){
			 if(node.getState().equals(child.getState())){
				 queue_node.remove(node) ;
				 return;
			 }
		 }
	 }
	  Node getNode(Queue<Node> queue_node , Node child){
       Node empty = new Node(-1 , new State("$$$"));
		 for(Node node : queue_node){
			 if(node.getState().equals(child.getState())){
				 return node;
			 }
		 }
		 return empty;
	 }
	  
	 Queue<Node> sortNodesBasedOnPathCost(Queue<Node> queue_unsorted){
		 Queue<Node> sort_queue = new LinkedList<Node>();
		 ArrayList<Node> state_list_unsorted = new ArrayList<Node>();
		 for(Node element : queue_unsorted)
		 {
		   state_list_unsorted.add(element);		 
		 }
		 Collections.sort(state_list_unsorted ,new SortNodesBasedPath());	
         for(Node element : state_list_unsorted){
           sort_queue.add(element);
         }
		 return sort_queue;
	 }
	 
	String ucs(Node root_node ){
			System.out.println("Starting ucs");
			String result;
			Queue<Node> open_queue = new LinkedList<Node>();
			Queue<Node> closed_queue = new LinkedList<Node>();
			open_queue.add(root_node);
			System.out.println("The start node is " +root_node.getState());
			System.out.println("It has start time  " +root_node.getWaterEntryTime());
			int time_taken = 0;
			while(open_queue.isEmpty() == false)
			{
				Node node  = open_queue.poll();
				System.out.println("Nodes is " + node.getState());
				if(end.contains(node.getState()))
				{		
						System.out.println("Found the node " +node.getState() +" has path cost " +node.getPathCost());
						closed_queue.add(node);
				    	result = node.getState() + " " +(node.getPathCost())%24 ;
					    return result;
				}
				else 
				{
				System.out.println("The path cost so far is " + node.getPathCost());
				ArrayList<Node> children = expansion_ucs(node);
				Collections.sort(children ,new SortNodes());	
			    for(Node child: children)
			    {
				if (!child.getState().equals("$$$") && getNode(open_queue , child).getState().equals("$$$") && getNode(closed_queue , child).getState().equals("$$$")){
			    	System.out.println("Adding child node to open_queue as case 1 is satisfied:" +child.getState());
		            open_queue.add(child);
			        }
			    else if(!getNode(open_queue , child).getState().equals("$$$")){   
			             Node open_queue_node = getNode(open_queue , child);
			             if(child.getPathCost() < open_queue_node.getPathCost()){
			             System.out.println("***Removing  node from open_queue with:" + open_queue_node.getState() + " with path cost"  + open_queue_node.getPathCost() +"and node number is " + open_queue_node.getNode());
			    		 removeNode(open_queue,open_queue_node);
			    		 System.out.println("****Adding  node to open_queue with:" + child.getState() + " with path cost"  + child.getPathCost() +"and node number is " + child.getNode());
			    		 open_queue.add(child) ;
			             }
			     }
				 else if(!getNode(closed_queue , child).getState().equals("$$$")){
				             Node closed_queue_node = getNode(closed_queue , child);
				             if(child.getPathCost() < closed_queue_node.getPathCost()){
				             System.out.println("****Removing  node from closed_queue with:" + closed_queue_node.getState() + " with path cost"  + closed_queue_node.getPathCost() +"and node number is " + closed_queue_node.getNode());
				    		 removeNode(closed_queue,closed_queue_node);
				    		 System.out.println("*******Adding  node to  open_queue with:" + child.getState() + " with path cost"  + child.getPathCost() +"and node number is " + child.getNode());
				    		 open_queue.add(child) ;
					     }
			     }
			     }
				}
				closed_queue.add(node);			
				open_queue = sortNodesBasedOnPathCost(open_queue);
			}
			result = "None";
			return result;
		}
	 
	  String dfs(Node root_node ){
			System.out.println("Starting dfs");
			String result;
			Stack<Node> stack_nodes = new Stack<Node>();
			ArrayList<String> visited = new ArrayList<String>();
			HashMap<String , Node > visited_nodes = new HashMap <String ,Node>();
			stack_nodes.add(root_node);
			System.out.println("The start node is " +root_node.getState());
			int time_taken = 0;
			while(stack_nodes.isEmpty() == false)
			{   
				Node node  = stack_nodes.pop();	
				visited.add(node.getState());
				visited_nodes.put(node.getState(), node);
				System.out.println("Popping Nodes is and the visted size :" + node.getState() +"," +visited.size());
				if(end.contains(node.getState()))
				{	Node found_goal = new Node( -1 , new State("$$$"));
				    found_goal = node;
				    System.out.println("Found the node " +node.getState() +" has path cost " +node.getPathCost());
					while(!node.getState().equals(root_node.getState())){
						System.out.println("The node " +node.getState() +" has parent " +node.getParentState());
						node = visited_nodes.get(node.getParentState());
						time_taken = time_taken + 1;
					}
				
				System.out.println("Found end node :" +node.getState());
				result = found_goal.getState() +  " " +(time_taken+start_time)%24 ;
				return result;
				}
				else
				{
				int found_unvisited_child = 0;
				ArrayList<Node> children = expansion_dfs(node);
				Collections.sort(children ,new SortNodesDesc());	
			    for(Node child: children)
			    {
				if (!child.getState().equals("$$$") && visited.contains(child.getState()) == false){
			    System.out.println("Adding child node :" +child.getState());
			    stack_nodes.add(child);				   
			    }
				}
			}
			}
			result= "None" ;
			return result;
		}
	

	  String bfs(Node root_node){
		System.out.println("Starting bfs");
		int time_taken = 0;
		Queue <Node>  queue_nodes = new LinkedList<Node>();
		ArrayList<String> visited = new ArrayList<String>();
		HashMap<String , Node> visited_nodes = new HashMap <String ,Node>();
		queue_nodes.add(root_node);
		visited.add(root_node.getState());
		visited_nodes.put(root_node.getState(),root_node);
		System.out.println("The start node is " +root_node.getState());
		String result ;
		while(queue_nodes.isEmpty() == false)
		{
			Node node  = queue_nodes.poll();
			System.out.println("Removing node" + node.getState());
			if(end.contains(node.getState()))
			{
			Node found_goal = new Node(-1 , new State("$$$" ));
			found_goal = node;
			System.out.println("Found the node " +node.getState() +" has path cost " +node.getPathCost());
			while(!node.getState().equals(root_node.getState())){
				System.out.println("The node " +node.getState() +" has parent " +node.getParentState());
				node = visited_nodes.get(node.getParentState());
				time_taken = time_taken + 1;
			}
			result = found_goal.getState()  + " " +(time_taken+start_time)%24 ;
			return result;
			}
			else 
			{
			ArrayList<Node> children = expansion(node);
			Collections.sort(children ,new SortNodes());	
		    for(Node child: children)
		    {
			if (!child.getState().equals("$$$") && visited.contains(child.getState()) == false){
		    System.out.println("Adding child node :" +child.getState());
		    queue_nodes.add(child);
		    visited.add(child.getState());
		    visited_nodes.put(child.getState(), child);
		    System.out.println("The visited states are :" + visited);
			}
			}     
			}
		}
		result = "None" ;
		return result;
	}
}

class waterFlow {
	static ArrayList <String> start = new ArrayList<String>();
    static ArrayList <String> middle = new ArrayList<String>();
    static ArrayList <String> end = new ArrayList<String>();
    static ArrayList <String> lines = new ArrayList<String>();
    static ArrayList <String> states = new ArrayList<String>();
    static HashMap <String, State> states_list = new HashMap<String ,  State>();
    static Map <String , Node> nodes = new HashMap <String , Node>();
    static Map<String , Edges> edges = new HashMap<String , Edges>();
	static String strategy ;
	static String start_node;
	static String end_nodes;
	static String middle_nodes;
    static int start_time ;	
    static String empty_string;
    static int num_of_test_cases;
	public static void main (String args[]){
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
		    bufferedReader = new BufferedReader(new FileReader(args[0]));
		    bufferedWriter = new BufferedWriter(new FileWriter("/home/preethi/workspace/waterFlow/src/waterFlow/output.txt"));
		    num_of_test_cases = Integer.parseInt(bufferedReader.readLine());
		    int x = 0;
		    int line_number = 1;
		    while(x < num_of_test_cases){
		    System.out.println("Starting test case number :"+x);
		    System.out.println("Line number is :"+line_number);
		    start = new ArrayList<String>();
		    middle = new ArrayList<String>();
		    end = new ArrayList<String>();
		    lines = new ArrayList<String>();
		    states =new ArrayList<String>();
		    states_list = new HashMap<String ,  State>();
		    nodes = new HashMap <String , Node>();
		    edges = new HashMap<String , Edges>();
		    strategy = null;
		    start_node = null;
		    end_nodes = null;
		    middle_nodes = null;
		    start_time = 0;
		    empty_string = null;
		    strategy = bufferedReader.readLine();
		    start_node = bufferedReader.readLine() ;
		    end_nodes =  bufferedReader.readLine();
		    middle_nodes =  bufferedReader.readLine();
		    int  num_of_edges =  Integer.parseInt(bufferedReader.readLine());
		    String[] info = start_node.split(" ");
		    start.add(info[0]);
		    states.add(info[0]);
		    info = end_nodes.split(" ");
		    for(String end_info : info){
		    end.add(end_info);
		    states.add(end_info);
		    }  
		    info = middle_nodes.split(" ");
		    for(String middle_info : info){
		    middle.add(middle_info);
		    states.add(middle_info);
		    }  
		    int i = 0 ;
		    while (i < num_of_edges)
		    { 
		       lines.add(bufferedReader.readLine());
		       i++;
	        }
		    start_time = Integer.parseInt(bufferedReader.readLine());
		    empty_string = bufferedReader.readLine();
	     
        for(String state : states){
    	State state_variable = new State(state);
    	states_list.put(state, state_variable);
        }
   
	    for (String line : lines){
		String[] info_line = line.split(" ");
		String start_vertex = info_line[0];
	    String end_vertex = info_line[1];
	    int path_cost = Integer.parseInt(info_line[2]);
	    int num_off_times = Integer.parseInt(info_line[3]);	
	    ArrayList<String> off_periods = new ArrayList<String>();
	    ArrayList<Integer> start_off_time = new ArrayList<Integer>();
	    for (int j = 1 ; j <= num_off_times ; j++)
	    {
	    	String off_time = info_line[3+j];
	    	String[] timing_info = off_time.split("-");
	    	int start_time = Integer.parseInt(timing_info[0]);
	    	int end_time = Integer.parseInt(timing_info[1]);
	    	off_periods.add(off_time);
	    	for(int k = start_time ; k <= end_time ; k ++){
	    	start_off_time.add(k);
	    	}
	    }
	    Edges value = new Edges(start_vertex , end_vertex , path_cost , num_off_times , off_periods , start_off_time);
	    String key = start_vertex + "-" + end_vertex;
	    System.out.println("The edges are " + key + " and its value is ");  value.display();
	    edges.put(key, value);
	  }
	   
	 State it_state = states_list.get((start.get(0)));
	 Node root_node = new Node(1, it_state);
	 root_node.setWaterEntryTime(start_time);
	 root_node.setPathCost(start_time);
	 TestCases test_case = new TestCases(start , middle , end , lines  , states , states_list , nodes , edges , strategy  , start_node ,end_nodes , middle_nodes,start_time	);	
	 String result;
	   if(strategy.equalsIgnoreCase("BFS")){
	   result = test_case.bfs(root_node);
	   bufferedWriter.write(result);
	   bufferedWriter.newLine();    
	   }
	   else if(strategy.equalsIgnoreCase("DFS")){
		   result = test_case.dfs(root_node);
		   bufferedWriter.write(result);
		   bufferedWriter.newLine();
	   }  
	   else if(strategy.equalsIgnoreCase("UCS")){
		result = test_case.ucs(root_node);
	    bufferedWriter.write(result);
	    bufferedWriter.newLine();
	   }
	   else {
		 
		   System.out.println("No such method");
	   }
	   x++;
	   line_number = line_number + 7 + num_of_edges ;
    } 
    bufferedWriter.close();
    }catch (FileNotFoundException e) {
    e.printStackTrace();
    } catch (IOException e) {
    e.printStackTrace();
   } 
  }    
}
	

