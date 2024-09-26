//Timothy Tracy Project 1
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

class Person{
    String[] prefs;
    String name;
    String id;
    Person fiance;
    int lastAttemptedEngagement = -1;
    public Person(String name, String id, String[] prefs){
        this.name = name;
        this.id = id;
        this.prefs = prefs;
    }
    public String[] prefs(){
        return this.prefs;
    }
    public Person fiance(){
        return this.fiance;
    }
    public String id(){
        return this.id;
    }
    public String identity(){
        return this.name+id();
    }
    public String matching(){
        return "("+this.identity()+", "+fiance().identity()+")";
    }
    public boolean isEngaged(){
        return this.fiance != null ? true:false;
    }
    public int lastAttemptedEngagmentIdx(){
        return lastAttemptedEngagement;
    }
    public int nextAttemptedEngagmentIdx(){
        return lastAttemptedEngagement+1;
    }
    public void engage(Person person){
        if(this.fiance != person){
            //System.out.println(this.identity() + " engages " + person.identity());
            this.fiance = person;
            //System.out.println(matching());
            person.engage(this);
        }
    }
    public void propose(Person person){
        //System.out.println(this.identity() + " proposes to " + person.identity());
        lastAttemptedEngagement+=1;
        if(!person.isEngaged()){
            engage(person);
        } else {
            person.considerProposal(this);
        }
    }
    public void dispose(){
        //System.out.println(this.identity() + " disposes " + this.fiance.identity());
        Person tempFiance= this.fiance;
        if(this.fiance !=null){
            this.fiance = null;
        }
        if(tempFiance.isEngaged()){
            tempFiance.dispose();
        }
    }
    public void considerProposal(Person person){
        if(!isEngaged()){
            engage(person);
        } else{
            if(getPrefOfPerson(person)<getPrefOfPerson(this.fiance)){
                dispose();
                engage(person);
            } else {
               // System.out.println(this.identity() + " rejects " + person.identity());
            }
        }
    }
    public int getPrefOfPerson(Person person){
        for(int i = 0; i< this.prefs.length; i++){
            if(person.id().equals(prefs[i])){
                return 1+i;
            }
        }
        return -1;
    }
}
class Man extends Person{
    public Man(String name, String id, String[] prefs){
        super(name, id, prefs);
    }
}
class Woman extends Person{
    public Woman(String name, String id, String[] prefs){
        super(name, id, prefs);
    }
}
class Algorithm{
    int n;
    HashMap<String, Person> men;
    HashMap<String, Person> women;
    public Algorithm(int n, HashMap<String, Person> men, HashMap<String, Person> women ){
        this.n = n;
        this.men = men;
        this.women = women;
    }
    public boolean allAreEngaged(HashMap<String, Person> people){
        for(Person person: people.values()){
            if(!person.isEngaged()){
                return false;
            }
        }
        return true;
    }
    public String matchings(HashMap<String, Person> ppl){
        String s = "";
        for(Person person: ppl.values()){
            s+=person.id()+" " + person.fiance.id()+"\n";
        }
        return s;
    }
    public String matchingsFormatted(HashMap<String, Person> ppl){
        String s = "";
        for(Person person: ppl.values()){
            s+=person.matching()+"\n";
        }
        return s;
    }
}
class StableMarriage extends Algorithm{
    public StableMarriage(int n, HashMap<String, Person> men, HashMap<String, Person> women ){
        super(n, men, women);
    }
    public void run(){
        int manNum = 1;
        while(!allAreEngaged(men)){
            if (manNum%n == 0){
                manNum = n;
            } else {
                manNum = manNum%n;
            }
            //System.out.println("checking man "+ manNum);
            Person man = men.get(String.valueOf(manNum));
            if(!man.isEngaged()){
               // System.out.println("checking man "+ manNum + " wants to engage "+ women.get(man.prefs()[man.nextAttemptedEngagmentIdx()]).identity());
                String nextWomanNum = man.prefs()[man.nextAttemptedEngagmentIdx()];
                man.propose(women.get(nextWomanNum));
            }
            manNum +=1;
        }
    }
    public String matchings(){
        return super.matchings(men);
    }
    public String matchingsFormatted(){
        return super.matchingsFormatted(men);
    }
}
class SadieHawkins extends Algorithm{
    
    public SadieHawkins(int n, HashMap<String, Person> men, HashMap<String, Person> women ){
        super(n, men, women);
    }

    public void run(){
        int womanNum = 1;
        while(!allAreEngaged(women)){
            if (womanNum%n == 0){
                womanNum = n;
            } else {
                womanNum = womanNum%n;
            }
            Person woman = women.get(String.valueOf(womanNum));
            if(!woman.isEngaged()){
                String nextManNum = woman.prefs()[woman.nextAttemptedEngagmentIdx()];
                woman.propose(men.get(nextManNum));
            }
            womanNum +=1;
        }
    }
    public String matchings(){
        return super.matchings(men);
    }
    public String matchingsFormatted(){
        return super.matchingsFormatted(men);
    }
}

class Project1{
    public static void main(String[] args){
        String filePath = "input.txt";
        BufferedReader reader = null;
        HashMap<String, Person> men = new HashMap<String, Person>();
        HashMap<String, Person> women = new HashMap<String, Person>();
        HashMap<String, Person> clonemen = new HashMap<String, Person>();
        HashMap<String, Person> clonewomen = new HashMap<String, Person>();
       
        try {
            reader = new BufferedReader(new FileReader(filePath));
            int n = Integer.parseInt(reader.readLine());
            for(int i = 1; i<=n; i++){
                String line = reader.readLine();
                men.put(String.valueOf(i), new Man("M", String.valueOf(i),line.split(" ")));
                clonemen.put(String.valueOf(i), new Man("CM", String.valueOf(i),line.split(" ")));
            }
            for(int i = 1; i<=n; i++){
                String line = reader.readLine();
                women.put(String.valueOf(i),new Woman("W", String.valueOf(i),line.split(" ")));
                clonewomen.put(String.valueOf(i),new Woman("CW", String.valueOf(i),line.split(" ")));

            }
            System.out.println("");
            System.out.println("Input: Men");
            for(int i = 1; i<=n; i++){
                System.out.println(men.get(String.valueOf(i)).identity() + Arrays.toString(men.get(String.valueOf(i)).prefs()));
            }
            System.out.println();
            System.out.println("Input: Women");
            for(int i = 1; i<=n; i++){
                System.out.println(women.get(String.valueOf(i)).identity() + Arrays.toString(women.get(String.valueOf(i)).prefs()));
            }
            System.out.println();
            System.out.println("StableMarriage Results");
            StableMarriage sm = new StableMarriage(n, men, women);
            sm.run();
            //System.out.println(sm.matchings());
            System.out.println(sm.matchingsFormatted());

            System.out.println("SadieHawkins Results");
            SadieHawkins sh = new SadieHawkins(n, clonemen, clonewomen);
            sh.run();
            //System.out.println(sh.matchings());
            System.out.println(sh.matchingsFormatted());

            int manDifferences = 0;
            for(int i = 1; i<= n; i++){
                Person man = men.get(String.valueOf(i));
                Person cloneman = clonemen.get(String.valueOf(i));
                if(!(man.fiance().id().equals(cloneman.fiance().id()))){manDifferences+=1;}
            }

            int womanDifferences = 0;
            for(int i = 1; i<= n; i++){
                Person woman = women.get(String.valueOf(i));
                Person clonewoman = clonewomen.get(String.valueOf(i));
                if(!(woman.fiance().id().equals(clonewoman.fiance().id()))){womanDifferences+=1;}
            }
            System.out.println("Output");

            String sOrU = manDifferences == womanDifferences ? "Stable": "Unstable";
            System.out.println(manDifferences+" " + womanDifferences + " " + sOrU);
           
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}