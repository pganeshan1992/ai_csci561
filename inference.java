package backward;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
class back_tree{
	String clause ; 
	String status;
	String org_clause;
    ArrayList<String> kb = new ArrayList<String>();
	int visited = 0;
	back_tree parent;
	HashMap<String,String> sub = new HashMap<String,String>();
	back_tree(String clause , String status){
		this.clause= clause;
		this.status = status;
		this.org_clause = clause;
		
	}
	String getClause(){
		return clause;
	}
	String getStatus(){
		return status;
	}
	void setTrue(){
		this.status = "TRUE";
	}
	void setClause(String clause){
		this.clause = clause;
	}
	void setOrgClause(){
		this.clause = this.org_clause;
	}
	String getOrgClause(){
		return org_clause;
	}
	void setFalse(){
		this.status = "FALSE";
	}
	void setUnknown(){
		this.status = "UNKNOWN";
	}
    void setSubstitution(HashMap<String,String> sub){
    	this.sub = sub;
    }
   HashMap<String,String> getSubstitution(){
    	return this.sub;
    }
   void setKb(String kb){
   	this.kb.add(kb);
   }
   void clearKb(){
	   this.kb = new ArrayList<String>();
	   }
   ArrayList<String> getKb(){
   	return this.kb;
   }

}


class Predicate{
	String predicate;
	ArrayList<String> facts = new ArrayList<String>();
	ArrayList<String> rules =  new ArrayList<String>(); 
	ArrayList<back_tree> true_clauses =  new ArrayList<back_tree>();
	ArrayList<back_tree> false_clauses = new ArrayList<back_tree>();
	ArrayList<String> variables = new ArrayList<String>();
	String conclusion;
	HashMap<String,String> args = new HashMap<String,String>();
	Predicate(String predicate){
		this.predicate = predicate;
		
	}
	String getPredicate(){
		return predicate;
	}
	ArrayList<String> getFacts() {
		return facts;
	}
	ArrayList<String> getRules(){
		return rules;
	}
   ArrayList<back_tree> getTrueClauseList(){
	   return true_clauses;
   }
   ArrayList<back_tree> getFalseClauseList(){
	   return false_clauses;
   }
	ArrayList<String> getVariables(){
		return variables;
	}
    HashMap<String, String> getArgs(){
		return args;
	}
	void setFacts(String fact){
		facts.add(fact);
		//back_tree true_clause = new back_tree(fact,"TRUE");
		//true_clauses.add(true_clause);
	}
	void setRule(String rule){
		rules.add(rule);
	}
	void setAddTrueClause(back_tree true_clause){
		if(exists(true_clause.getClause()).equals("UNKNOWN"))
		true_clauses.add(true_clause);
	}
	void setAddFalseClause(back_tree false_clause){
		if(exists(false_clause.getClause()).equals("UNKNOWN"))
		false_clauses.add(false_clause);
	}
	String exists(String clause){
		for(back_tree t : true_clauses){
			
		String tt = t.getClause();
		if(tt.equals(clause))
			return "TRUE";
		}
		for(back_tree f : false_clauses){
			
			String ff = f.getClause();
			if(ff.equals(clause))
				return "FALSE";
		}	
		return "UNKNOWN";
	}
	back_tree getTrueClause(String clause){
		for(back_tree t : true_clauses){
			
			String tt = t.getClause();
			if(tt.equals(clause))
				return t;
			}
		return null;
	}
	back_tree getFalseClause(String clause){
		for(back_tree f : true_clauses){
			
			String ff = f.getClause();
			if(ff.equals(clause))
				return f;
			}
		return null;
	}
	void setVariables(ArrayList<String> variables){
		this.variables = variables;
	}
	void setArgs(HashMap<String, String> args){
		this.args = args;
	}
	
}

class inference{
	static int num_of_queries = 0;
	static int num_of_sentences = 0;
	static ArrayList<String> queries = new ArrayList<String>();
	static ArrayList<String> kb_from_file = new ArrayList<String>();
	static HashMap<String, Predicate> predicates_list = new HashMap<String , Predicate>();
	static ArrayList<String>entailed_kb = new ArrayList<String>();
	static HashMap<String, String> final_sub = new HashMap<String,String>();
	static ArrayList<String> used_var = new ArrayList<String>();
	static HashMap <String, Integer> var_num = new HashMap<String , Integer>();
    static ArrayList<String> rules_used = new ArrayList<String>();
    static ArrayList<String> std_rules_used = new ArrayList<String>();
	
	static void printUnifyResults(HashMap<String,String> results){
	//	System.out.println("****************Unififctaion Results***************");
		for(String key : results.keySet()){
	//	System.out.println("The sub key is" +key +"and the value is" +results.get(key));
		}
	//	System.out.println("_________________________________________________________");
		
	}
	static HashMap<String,String> unify_var(String x , String y , HashMap<String, String> result)
	{   String regex = "[a-z]";
        Pattern pattern1 = Pattern.compile(regex);
        if(result.containsKey(x) && ((pattern1.matcher(result.get(x).charAt(0)+"").matches() == true) ) && ((pattern1.matcher(x.charAt(0)+"").matches() == true) &&  (pattern1.matcher(y.charAt(0)+"").matches() == true)))
	    {
	    	if(!result.containsKey(y)){
	    	//	System.out.println("--1");
	    		result.put(y, x);
	    	}
	    	//System.out.println("---1.5");
	    }
        else if(!result.containsKey(x)){
      //  System.out.println("---2");
		result.put(x,y);
        }
	    else if(result.get(x).equals(y)){
	   //System.out.println("---3");
	    result.put(x,y);
	    }
	    else {
	    	//System.out.println("---4");
	    
		result.put("Failure$","");
	    }
		return result;
	    
	}
	static HashMap<String,String> unify(ArrayList<String> x , ArrayList<String> y , HashMap<String, String> result)
	{   String regex = "[a-z]";
	    Pattern pattern1 = Pattern.compile(regex);
		String query = x.get(0);
	    String rule = y.get(0);
	   // System.out.println("Trying to unify aguments" + query +"-" +rule);
		if(x.equals(y)){
		//System.out.println("Here1");
	    return result;
		}

		else if(y.size() == 1 && x.size() == 1 && pattern1.matcher(query.charAt(0)+"").matches() == true){
        result = unify_var(query,rule,result);
       // System.out.println("Here2");
    	return result;
		}
		else if(y.size() == 1 && x.size() == 1  && pattern1.matcher(rule.charAt(0)+"").matches() == true){
	    result = unify_var(rule, query, result);    	
	    //System.out.println("Here2.5");
	    return result;
		}
	    else if(x.size() == y.size()  && y.size()!=1){
	    	x.remove(0);
	    	y.remove(0);
	    	ArrayList<String> x_first = new ArrayList<String>();
	    	ArrayList<String> y_first = new ArrayList<String>();
	    	x_first.add(query);
	    	y_first.add(rule);
	    
	    	if(!result.containsKey("Failure$")){	
            result = unify(x_first,y_first, result);
            if( !result.containsKey("Failure$") ){
			result = unify(x, y ,result);
            }
            else{
            result = new HashMap<String, String>();
           // System.out.println("Here65");
            result.put("Failure$","");
            return result;
            }
	    	}
            else{
            result = new HashMap<String, String>();
            //System.out.println("Here.55");
            result.put("Failure$","");
            return result;
	    	}
			if(result.containsKey("Failure$")){
			//System.out.println("Here555");
		    result = new HashMap<String, String>();
            result.put("Failure$","");	
			}
		    System.out.println("Here5");
			return result;
			
		} 
		else
		{     
			  result.put("Failure$","");
			  return result;
		}
		 
	
	}
	static HashMap<String,String> checkUnification(String query , String rule , HashMap<String, String> result){
		//System.out.println("CHECK UNIFICSATION of" +query +"and " +rule);

		String query_predicate = query.replaceAll("\\(.*\\)", "");
		String query_arguments = query.substring(query.indexOf("(")+1,query.indexOf(")"));
		String query_params[] = query_arguments.split(",");	
		String rule_parts[];
		String rule_conclusion;
		String rule_predicate;
		String rule_arguments;
		String rule_params[];	
	
		if(rule.contains("=>")){
		rule_parts = rule.split("=>");
		rule_conclusion = rule_parts[1].replaceAll("\\s+","");
		rule_predicate = rule_conclusion.replaceAll("\\(.*\\)", "");
		rule_arguments = rule_conclusion.substring(rule_conclusion.indexOf("(")+1,rule_conclusion.indexOf(")"));
		rule_params = rule_arguments.split(",");	
		}
		
		else{
		    rule_conclusion = rule.replaceAll("\\s+","");
			rule_predicate = rule_conclusion.replaceAll("\\(.*\\)", "");
			rule_arguments = rule_conclusion.substring(rule_conclusion.indexOf("(")+1,rule_conclusion.indexOf(")"));
			rule_params = rule_arguments.split(",");	
		}
		
	    ArrayList<String> query_list = new ArrayList<String>();
	    ArrayList<String> rule_list = new ArrayList<String>();
	    
	    for(String q : query_params){
	    	q.replaceAll("\\s+","");
	    	query_list.add(q);
	    	
	    }
	    for(String r : rule_params){
	    	r.replaceAll("\\s+","");
	    	rule_list.add(r);
	    }
		result = unify(query_list , rule_list , result);
	    return result;
		
				
	}

	//return the the clause after making the substitution which is then added to the kb.
	
	static String makeSubstitution(String clause , HashMap<String,String> sub_result){
	    //System.out.println("EXECUTING MAKE SUB");
		ArrayList<String> final_sub = new ArrayList<String>();
		String final_sub_string = "";
		String substitued_string = clause;
		String divide[];
		String premise ;
		String con; 
		String predicates[];
		ArrayList<String> preds = new ArrayList<String>();
		if(clause.contains("=>")){
		 divide = clause.split("=>");
		 premise = divide[0];
		 con = divide[1]; 
		predicates = premise.split("\\^");
		for(String pred : predicates ){
			preds.add(pred);
		}
		preds.add(con);
		}
		else {
			
			 premise = clause;
			 predicates = premise.split("\\^");
			 for(String pred : predicates ){
				preds.add(pred);
			}
		
		}
		HashMap<String,String> new_map = new HashMap<String,String>();
		for(String key : sub_result.keySet())
		{  
			new_map.put(key, sub_result.get(key));
		}
		
		for(String key : sub_result.keySet())
		{
			String regex = "[a-z]";
	    	Pattern pattern1 = Pattern.compile(regex);
	    	Matcher matcher1 = pattern1.matcher(key.charAt(0)+"");
	    	String value = sub_result.get(key);
	    	if(value.length()>0){
	    	Matcher matcher2 = pattern1.matcher(value.charAt(0)+"");
	    	if(matcher1.matches() == true && matcher2.matches() == false){
	    		
	        for(String new_key: new_map.keySet()){
	    	  if((new_map.get(new_key)).equals(key)){
	    		  new_map.put(new_key,value);
	    	  }
	      }
		 }
	    }
		}

		
		for( String pred : preds){ 
		String subsituted_arguments = "";
        for( String variable : new_map.keySet())  {
         String sub = new_map.get(variable);
         String arguments = pred.substring(pred.indexOf("(")+1,pred.indexOf(")"));
         String args[] = arguments.split(","); 
         for(int i = 0 ; i < args.length ; i++)
         {   
        	 if(args[i].equals(variable))
        	 args[i] = sub ;
         }
         subsituted_arguments = "";
         for(String arg : args){
        	 subsituted_arguments = subsituted_arguments +arg + ",";
         }
         subsituted_arguments = subsituted_arguments.substring(0,subsituted_arguments.length()-1);
        pred = pred.replaceAll("\\(.*\\)", "("+subsituted_arguments +")"); 
      }
        final_sub.add(pred);
	}
	int i = 1;
    for(String lit: final_sub){
    	if(i <= predicates.length){
    		final_sub_string =  final_sub_string + lit + " ^ ";
    		i++;
    	}
    }
    final_sub_string = final_sub_string.substring(0,final_sub_string.length()-3);
    if(clause.contains("=>"))
    final_sub_string = final_sub_string +"=> " + final_sub.get(final_sub.size()-1);
	return  final_sub_string;
	}
	
	static ArrayList<String> checkVariableExist(String predicate){
		ArrayList<String> args = new ArrayList<String>();
		String arguments = predicate.substring(predicate.indexOf("(")+1,predicate.indexOf(")"));
		String params[] = arguments.split(",");
	    for(String param : params){
	    	
	    	String regex = "[a-z]";
	    	Pattern pattern1 = Pattern.compile(regex);
	    	Matcher matcher1 = pattern1.matcher(param.charAt(0)+"");
	    	if(matcher1.matches())
	    	args.add(param);
	    	
	    }
	    return args;
	}

  
    static String standarized(String kb){
    	ArrayList<String> predicates = new ArrayList<String>();
    	ArrayList<String> var = new ArrayList<String>();
    	String divide[] = kb.split("=>");
	    String premise[] = divide[0].split("\\^");
	    for(String prem : premise){
	    	prem = prem.replaceAll("\\s+","");
	    	predicates.add(prem);
	    }
        if(divide.length > 1){
        	divide[1] = divide[1].replaceAll("\\s+","");
        	predicates.add(divide[1]);
        }
        for(String pred : predicates){
        	ArrayList<String> args= checkVariableExist(pred);
        	for(String arg : args){
        		if(!var.contains(arg))
        		var.add(arg);
        	}
        }
       for(String arg : var){
    	   if(arg.length()== 1){
        		int num = var_num.get(arg)+1;
        		var_num.put(arg,num);
        		HashMap<String,String> std_sub = new HashMap<String,String>();
        		std_sub.put(arg,arg+num);
        		kb = makeSubstitution(kb,std_sub);
        	}	
        	}
        
        return kb;
    }
    
    static boolean vardoesnotexist(String sentence){

		if(sentence.contains("=>")){
			String conclusion = "";
			String divide[] = sentence.split("=>");
			conclusion =  divide[1];
		    String[] prem = divide[0].split("\\^");
		    conclusion = conclusion.replaceAll("\\s+","");
		    String conclusion_arguments = conclusion.substring(conclusion.indexOf("(")+1,conclusion.indexOf(")"));
			conclusion = conclusion.replaceAll("\\(.*\\)", "");
            boolean var_exist  = checkVariableExist(divide[1]).isEmpty();
            if(var_exist){
			for(String prems : prem){
			var_exist = checkVariableExist(prems).isEmpty();
			if(!var_exist)
			break;
			}
            }
            return var_exist;
		  }
			else{
			boolean var_exist = checkVariableExist(sentence).isEmpty();	
			return var_exist;
			}
	}

    static HashMap<String,String> backward_chain_and(ArrayList<back_tree> goals , HashMap<String, String> theta){

    // this should ideally never happen
	if(theta.containsKey("Failure$"))
		return theta;

	//if there is no premises then this will happen and we return theta as is
	else if(goals.size() == 0)
		return theta;
	//if the status of the poped clause is false then mark everything in the stack as false and return
     else if(goals.get(0).getStatus().equals("FALSE")){
        back_tree first = goals.remove(0);
			for(back_tree rest : goals){
				rest.setFalse();
			}
			return theta;
	}
	//there are some premises
	else {
		back_tree first = goals.remove(0);
		if(!first.getStatus().equals("TRUE"))
		{
		first.setClause(makeSubstitution(first.getClause(),theta));
		theta = map_append(theta,backward_chain_or(first, theta)); 
		}
		if(first.getStatus().equals("FALSE")){
				for(back_tree rest : goals){
				rest.setFalse();
			}		
		}
		else if(first.getStatus().equals("UNKNOWN")){
		}
		else{		
			theta = map_append(theta,backward_chain_and(goals,theta));	

	     }
	 } 
		return theta;
	  
	

    }
    static HashMap<String,String> map_append(HashMap<String,String> x ,HashMap<String,String> y ){
    	for(String key : y.keySet()){
    		x.put(key,y.get(key));
    	}
    	return x;
    }
    static HashMap<String,String> remove_map(HashMap<String,String> x ,HashMap<String,String> y ){
    //	System.out.println(x +"&" +y);
    	String regex = "[a-z]";
        Pattern pattern1 = Pattern.compile(regex);
    	for(String key : y.keySet()){
    		if(!pattern1.matcher(y.get(key).charAt(0)+"").matches()){
    		if(x.containsKey(key) && !pattern1.matcher(x.get(key).charAt(0)+"").matches()){
            if(x.get(key) == y.get(key)){
    		x.remove(y.get(key));
            }
    		}
    		}
    	}
    	//System.out.println(x +"&" +y);
    	return x;
    }
    static HashMap<String,String> backward_chain_or(back_tree clause , HashMap<String,String> theta){
    	//System.out.println("In or");	
		String query = clause.getClause();
		String query_predicate = query.replaceAll("\\(.*\\)", "");
	    String substitution = query.substring(query.indexOf("(")+1,query.indexOf(")"));
		query_predicate = query_predicate.replaceAll("\\s+","");
		query = query.replaceAll("\\s+","");
		Predicate obj = predicates_list.get(query_predicate);
	    if(obj == null){
	    	  clause.setFalse();
			  theta.put("Failure$", "");
			  return theta;
	    }
	    ArrayList<String> rules = obj.getRules();
	    ArrayList<String> facts = obj.getFacts();
	   // System.out.println(clause.getClause());
	    ArrayList<String> known_kb = new ArrayList<String>();
	    for(String fact : facts)
		known_kb.add(fact); 
	    for(String rule : rules)
	    known_kb.add(rule); 
	    int i = 0;
	    for(String known : known_kb){
	  //  	System.out.println("kb has" +known);	
	    }
	    for(String  rule :rules_used ){
        	///System.out.println("the rules used "+rule);
        }
	      while(i < known_kb.size() && clause.getStatus().equals("UNKNOWN")){  	
            String kb = known_kb.get(i);
           // System.out.println("Rules used does not contains kb" + !rules_used.contains(kb) +"-----" +clause.getKb());
          //  System.out.println("Facts containn kb  and size of facts is " + facts.contains(kb) + facts.size());
           if((!rules_used.contains(kb) && !clause.getKb().contains(kb))){       
            String org_kb = kb;
            if(kb.contains("=>") && !vardoesnotexist(kb))
            rules_used.add(org_kb);
            //System.out.println("KB "+kb);
            HashMap<String, String>  result = new HashMap<String, String>();
            kb =  standarized(kb);
           //System.out.println("Std query :"+kb);
        	result = checkUnification(query,kb ,result);
        	printUnifyResults(result);
        	if(!result.containsKey("Failure$") && result.size()!=0){
        		for(String key : result.keySet()){
        			theta.put(key,result.get(key));
        		}
        	}
        	
        	ArrayList<back_tree> prem_goal = new ArrayList<back_tree>();
        	ArrayList<back_tree> prem_goal_cp = new ArrayList<back_tree>();
        	ArrayList<back_tree> org = new ArrayList<back_tree>();
  		    String divide[] = kb.split("=>");
  			String premise[] = divide[0].split("\\^");
  			for(String prem : premise )
  			{   
  			    prem = prem.replaceAll("\\s+","");
  			    back_tree prem_clause = new back_tree(prem,"UNKNOWN");
  			    prem_clause.setSubstitution(result);
  			    prem_goal.add(prem_clause);
  			    prem_goal_cp.add(prem_clause);
  			}
			//case 1 : result is success and no pred - then return theta and mark clause as true
			//case 2 : result is failure - then we try another rule we do nothing and we dont add the rule to the set of rules_used nor the sub to theta
			//case 3 : result has some value and there is a predicate - it is unknown - we make a stack of the prem and send the substution we got for it which is in theta
			//case 4 : result has some value and there is no predicate - same as above
			if(result.size() == 0 && !kb.contains("=>")){
				//System.out.println("Case 1:" +clause.getClause() + "kb" +kb);
				rules_used.remove(org_kb);
				std_rules_used.add(kb);
				clause.setTrue();
				theta = map_append(theta,result);
				clause.setSubstitution(theta);
				clause.setKb(kb);
				//System.out.println("Case 1:" +clause.getClause() + "kb" +clause.getKb());
				//System.out.println("GOING FROM CASE 1 ");
				return theta;
			}
			else if(result.size() != 0 && !result.containsKey("Failure$") && !kb.contains("=>")){
				String after_sub = makeSubstitution(clause.getClause(),result);
				 if(facts.contains(after_sub)){
				    	clause.setTrue();
				    	rules_used.remove(org_kb);
						std_rules_used.add(kb);
				    	clause.setClause(after_sub);
				    	theta = map_append(theta,result);
						clause.setSubstitution(theta);
						clause.setKb(clause.getClause());
						//System.out.println("Case 1.5:" +clause.getClause() + "kb" +clause.getKb());
						//System.out.println("GOING FROM CASE 2 ");
						return theta;						
				    }
			}
			else if((result.size() != 0 && !result.containsKey("Failure$") && kb.contains("=>")) || (result.size() == 0 &&  !result.containsKey("Failure$") && kb.contains("=>")) ){
			
			 while(!prem_goal.isEmpty() && !prem_goal_cp.get(0).getStatus().equals("FALSE")){
				for(back_tree prem_clause : prem_goal_cp)
				{   prem_clause.setOrgClause();
					//System.out.println("before Premise clause is" +prem_clause.getClause() +"" +prem_clause.getStatus() + "---" +prem_clause.getOrgClause() +"--------------" +prem_clause.getKb() +"--------" +prem_clause.getSubstitution());
				}
				for(back_tree prem_clause : prem_goal_cp)
				{   
					prem_clause.setClause(makeSubstitution(prem_clause.getClause(),result));
					//System.out.println("before sub Premise clause is" +prem_clause.getClause() +"" +prem_clause.getStatus() + "---" +prem_clause.getOrgClause()  +"--------" +prem_clause.getSubstitution());
				}
				for(back_tree prem_clause : prem_goal_cp)
				{   
					//System.out.println(" after sub Premise clause is" +prem_clause.getClause() +"" +prem_clause.getStatus() + "---" +prem_clause.getOrgClause() +"--------" +prem_clause.getSubstitution());
				}
				HashMap<String,String>temp_and_results = new HashMap<String,String>();
				temp_and_results = backward_chain_and(prem_goal , result);
				//System.out.println("Result after doing the and fully");
				printUnifyResults(result);  
				//System.out.println("CAME FROM CALLING AND IN OR ");
			    int check = 1; 
			    if(prem_goal_cp.get(0).getStatus().equals("FALSE")){	
			    	clause.setKb(org_kb);
			    	break;
			    }
				for(back_tree prem_clause : prem_goal_cp)
				{   
					//System.out.println("after 88888888 Premise clause is" +prem_clause.getClause() +"" +prem_clause.getStatus() + "--------------" +prem_clause.getKb() +"--------" +prem_clause.getSubstitution());
				}
			    prem_goal = new ArrayList<back_tree>();
			    Stack<back_tree> done = new Stack<back_tree>();
			    for(back_tree prem_clause : prem_goal_cp){
			    	//System.out.println("Premise clause is" +prem_clause.getClause() +"" +prem_clause.getStatus());
			    	if(!prem_clause.getStatus().equals("TRUE")){
                        prem_clause.setSubstitution(new HashMap<String,String>());
                        prem_clause.setUnknown();
                        prem_clause.clearKb();
                        prem_goal.add(prem_clause);  
			    		check = 0;
			    		rules_used.remove(org_kb);
			    	}
			    	else{
			    		prem_goal.add(prem_clause);
			    		done.add(prem_clause);
			    	}
			     }
			    for(back_tree prem_clause : done)
				{   
					//System.out.println("done ones" +prem_clause.getClause() +"" +prem_clause.getStatus() + "--------------" +prem_clause.getKb() +"--------------------" +prem_clause.getSubstitution());
				}
			    if(check == 0){
			    	//System.out.println("see this 1 ");
			    	printUnifyResults(result); 
			       // System.out.println("see this 2 ");
			    	printUnifyResults(result); 
			    	back_tree to_be_changed = new back_tree("","");
			    	to_be_changed = done.pop();
			    	result = new HashMap<String,String>();
			    	if(!done.isEmpty()){
			    	back_tree to_be_changed2 = new back_tree("","");
			    	to_be_changed2 = done.pop();
			    	//System.out.println(to_be_changed2.getSubstitution());
			    	result = map_append(result,to_be_changed2.getSubstitution());
			    	//System.out.println("We get the substitution of the two clauses back and see");
			    	printUnifyResults(result); 
			    	}
			    	else{
			    	result = map_append(result,theta);
                 	}
			    	to_be_changed.setUnknown();
			    	to_be_changed.setSubstitution(new HashMap<String,String>());
			    	printUnifyResults(result);  
			    	//System.out.println("********************************&&&&&&&&&&&&&&&&");
			    }
			    if(check == 1){
			    rules_used.remove(org_kb);
			    std_rules_used.add(kb);
			    clause.setTrue();
			    clause.setKb(org_kb);
			    printUnifyResults(theta);  
                theta = map_append(theta ,temp_and_results);
                printUnifyResults(theta);  
                //System.out.println("GOING FOR CASE 4 IN OR");
			    return theta;
			    }
			    for(back_tree prem_clause : prem_goal)
				{   
					
					//System.out.println("end Premise clause is" +prem_clause.getClause() +"" +prem_clause.getStatus() + "--------------" +prem_clause.getKb());
				}
			}	
            }
            }
			i++;
		}
//      /  System.out.println("Case 3: SETTING CLAUSE TO FALSE" +clause.getClause() +clause.getStatus());
        if(clause.getStatus().equals("UNKNOWN")){
         clause.setFalse();
         clause.clearKb();
         if(!rules_used.isEmpty())
          rules_used.remove(rules_used.size()-1);
          }
	      return theta;
    }
            
	static void arrange_knowledge_base(){
		for(String sentence : kb_from_file){
			if(sentence.contains("=>")){
				String conclusion = "";
				String divide[] = sentence.split("=>");
				conclusion =  divide[1];
			    String[] prem = divide[0].split("\\^");
			    conclusion = conclusion.replaceAll("\\s+","");
			    String conclusion_arguments = conclusion.substring(conclusion.indexOf("(")+1,conclusion.indexOf(")"));
				conclusion = conclusion.replaceAll("\\(.*\\)", "");
				if(predicates_list.containsKey(conclusion)== false){
				Predicate obj =  new Predicate (conclusion);
				predicates_list.put(conclusion , obj);
				obj.setRule(sentence);
				ArrayList<String> variables = checkVariableExist(divide[1]);
				obj.setVariables(variables);
				}
				else{
				Predicate obj =  predicates_list.get(conclusion);
				obj.setRule(sentence);
				ArrayList<String> variables = checkVariableExist(divide[1]);
				obj.setVariables(variables);
				}
				
			}
			else{
                String conclusion = sentence.replaceAll("\\s+","");
				conclusion = conclusion.replaceAll("\\(.*\\)", "");
				if(predicates_list.containsKey(conclusion)== false){
				Predicate obj =  new Predicate (conclusion);
				predicates_list.put(conclusion , obj);
				obj.setFacts(sentence);
				}
				else{
				Predicate obj =  predicates_list.get(conclusion);
				obj.setFacts(sentence);
				}
			}	 
			}
		}
		
	
	
	public static void main(String args[]){
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
	      try {
			bufferedReader = new BufferedReader(new FileReader("/home/preethi/workspace/backward_chain/src/backward/input"));
			bufferedWriter = new BufferedWriter(new FileWriter("/home/preethi/workspace/backward_chain/src/backward/output.txt"));
			// PrintStream out = new PrintStream(new FileOutputStream("/home/preethi/workspace/backward_chain/src/backward/output-final.txt"));
		    // System.setOut(out);
			num_of_queries = Integer.parseInt(bufferedReader.readLine());
			int i = 1; 
			while(i <= num_of_queries){
				String query = bufferedReader.readLine();
				queries.add(query);
				i++;
			}
			
			num_of_sentences  = Integer.parseInt(bufferedReader.readLine());
			i = 1; 
			while(i <= num_of_sentences){
				String sentence = bufferedReader.readLine();		
				kb_from_file.add(sentence);	
				i++;
			}	
			arrange_knowledge_base();
			
			for(String q : queries){
			String alphabets = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
			String[] letters = alphabets.split(",");
			for(String let : letters)
		    var_num.put(let,0);
	        rules_used = new ArrayList<String>();
	        HashMap <String, String> theta = new HashMap<String,String>();
	        back_tree clause = new back_tree(q , "UNKNOWN");
            theta = backward_chain_or(clause, theta);
            bufferedWriter.write(clause.getStatus());
            bufferedWriter.newLine();
		 } 
			bufferedWriter.close();
			
	      } catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	      catch (IOException e) {
				e.printStackTrace();
		}	  

		
	}
}