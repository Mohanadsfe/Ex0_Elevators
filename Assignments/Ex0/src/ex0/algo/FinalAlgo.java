package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.Elevator;

import java.util.*;

public class FinalAlgo implements ElevatorAlgo{

    public static final int UP=1, DOWN=-1;
    private int _direction;
    private Building _building;
    private boolean[] _firstTime;

    public FinalAlgo(Building b) {
        _building = b;
        _direction = UP;
        _firstTime = new boolean[_building.numberOfElevetors()];
        for(int i=0;i<_firstTime.length;i++) {_firstTime[i] = true;}
    }

    @Override
    public Building getBuilding() {

        return this._building;
    }

    public int getDirection() {return this._direction;}



    @Override
    public String algoName() {

            return "Ex0_OOP_Basic_Final_Algo";
    }


    private double dist(int src, int elev,int dest) {
        double ans = -1;
        Elevator thisElev = this._building.getElevetor(elev);
        int pos = thisElev.getPos();
        double speed = thisElev.getSpeed();
        int min = this._building.minFloor(), max = this._building.maxFloor();
        double up2down = (max-min)*speed;
        double floorTime = speed+thisElev.getStopTime()+thisElev.getStartTime()+thisElev.getTimeForOpen()+thisElev.getTimeForClose();



        if(elev%2==1) { // up

            if(pos<=src) {ans = (src-pos)*floorTime;

            } // How much time take to get to the src LeveL
            else {
                ans = ((max-pos) + (pos-min))*floorTime + up2down; // All the sum of the state well be when the elev is work
            }

            if (src<dest){ // UP
                ans+=(dest-src)*floorTime;
            }
            else{ // DOWN
                ans+=(src-dest)*floorTime;

            }

        }
        else { // elev is in even number
            if(pos>=src) {ans = (pos-src)*floorTime+(src-dest)*floorTime;} // ***** Perhaps there a wrong , needs to check it
            else {
                ans += ((max-pos) + (pos-min))*floorTime + up2down; //*****
            }

            if (src<dest){ // UP
                ans+=(dest-src)*floorTime;
            }
            else{ // DOWN
                ans+=(src-dest)*floorTime;

            }

        }
        return ans;
    }




    public double TheBestBydist(Building building, CallForElevator c){

        double min=dist(c.getSrc(),0,0); // min=dist(c.getSrc(),0,c.getSrc());

        if (c.getSrc() < c.getDest()) { // UP
        for (int i= 3; i <this._building.numberOfElevetors() ; i+=2) {

            if (min > dist(c.getSrc(), i,c.getDest()))
                min = dist(c.getSrc(), i,0); // should c.getDest()
        }

        }
        else{// DOWN

            for (int i= 2; i <this._building.numberOfElevetors() ; i+=2) {

                if (min > dist(c.getSrc(), i,c.getDest()))
                    min = dist(c.getSrc(), i,c.getDest());
            }


        }
        return min;

    }

// Function to check if their more than one elevator that have the same distance (min)
    public boolean MoreThanOne(Building building, int elev,double min,CallForElevator c){
       // int indexMin=IndexOFTheBestBydist(building,src);

        int counter=0;
        for (int i= 1; i <this._building.numberOfElevetors()&&i!=elev; i++) {

            if (min==dist(elev,i,c.getDest())) //****** if (min==dist(c.getSrc,i,c.getDest()))
                counter++;
        }

        return counter > 0;
    }

    public int TheBestElevator(Building building,int elev,CallForElevator c) {


    double MinDist=TheBestBydist(building,c);
        int NElevIndex=elev;
        int number = this._building.numberOfElevetors();

        if (MoreThanOne(building, elev, TheBestBydist(building, c),c)) { // there more than one elevator that have a good dist


            for (int i = 0; i <number ; i++) {

                if (dist(elev,i,c.getDest())==MinDist){ //***** dist(c.getDest(),i,c.getDest())

// After we check that their more than one elevator that have the same distance ,
// then to get the best elevator , compare all of them by the speed
                    if (this.getBuilding().getElevetor(elev).getSpeed()<this.getBuilding().getElevetor(i).getSpeed()){
                        NElevIndex=i;
                    }
                }

            }


        }

        return NElevIndex;
    }


    public int TheBestBydist2(Building building, CallForElevator c){

        int indexMin=-1;
        double min=dist(c.getSrc(),0,0); // min=dist(c.getSrc(),0,c.getSrc());

        if (c.getSrc() < c.getDest()) { // UP
            for (int i= 3; i <this._building.numberOfElevetors() ; i+=2) {

                if (min > dist(c.getSrc(), i,c.getDest())) {
                    min = dist(c.getSrc(), i, 0); // should c.getDest()
                    indexMin=i;
                }
            }

        }
        else{// DOWN

            for (int i= 2; i <this._building.numberOfElevetors() ; i+=2) {

                if (min > dist(c.getSrc(), i,c.getDest())) {
                    min = dist(c.getSrc(), i, c.getDest());
                    indexMin = i;

                }


                }


        }
        return indexMin;

    }

//////New*************************************************

    //Here the (int elev0 that have the number(index)of the elevator that have good dist.
    public int TheBestElevatorDifferentDistanceAndSpeed(Building building,int elev,CallForElevator c,int min) {

        int minDistance =min;



        ArrayList<Elevator> array2 = new ArrayList<Elevator>();
        int counter=0;
        for (int i = 0; i < building.numberOfElevetors(); i++) {

            if (dist(c.getSrc(),i,c.getDest())>minDistance){
                array2.add(building.getElevetor(i));
                counter++;
            }
        }


        double timeOfTheMinDistance = minDistance/(building.getElevetor(elev).getSpeed());
        int indexTheBestElevator = -1;
        for (int i = 0; i <counter; i++) {
            if (timeOfTheMinDistance>dist(c.getSrc(),i,c.getDest())/(array2.get(i).getSpeed())){
                timeOfTheMinDistance=dist(c.getSrc(),i,c.getDest())/(array2.get(i).getSpeed());
                indexTheBestElevator=i;
            }
        }


        return indexTheBestElevator;

    }



        @Override
    public int allocateAnElevator(CallForElevator c) {

        int ans = 0, elevNum = _building.numberOfElevetors();
int ans2=0;
        if (elevNum > 1) {

            if (c.getSrc() < c.getDest()) { // UP
                ans = 1;
                for (int i = 3; i < elevNum; i += 2) {

                    int src = c.getSrc(); // save the number of the floor we have get call from it

                    int GoTo=c.getDest();
                    if (dist(src, i,GoTo) < dist(src, ans,GoTo)) { //******* !!!!!!!!!
                        ans = i;
                    }  // Check which elev is better than the other depend on time work(dest)
                }

            } else {   // down   if (c.getSrc() >= c.getDest())
                for (int i = 2; i < elevNum; i += 2) {
                    int src = c.getSrc();
                    if (dist(src, i,c.getDest()) < dist(src, ans,c.getDest())) {
                        ans = i;
                    }   // The same
                }
            }

            ans2 = TheBestElevator(this.getBuilding(), ans, c);

        }
      //  }  // בעליה רק המעליות האי זוגים עובדים ובירידה ההפך
        return ans2;

    }












    @Override
    public void cmdElevator(int elev) {


        if(!_firstTime[elev]) {  // if the elev is not work
            if (this._building.numberOfElevetors() == 1) {
                f0();
            } else {
                if (elev % 2 == 0) {
                    f2(elev);
                } else {
                    f1(elev);
                }
            }
        }

        else {  // Here , we have a new algorithm of ShabatElev2Algo
            _firstTime[elev] = false;
            int min = this._building.minFloor(), max = this._building.maxFloor();
            for(int i=0;i<this._building.numberOfElevetors();i++) {
                Elevator curr = this.getBuilding().getElevetor(elev);
                int floor = rand(min,max);
                curr.goTo(floor);
            }
        }

    }
    private void f1(int elev) { // odd --> UP  אי זוגי
        Elevator curr = this.getBuilding().getElevetor(elev);
        if(curr.getState() == Elevator.LEVEL) {
            int dir = this.getDirection();
            int pos = curr.getPos();
            if(pos<curr.getMaxFloor()) { curr.goTo(pos+1); }
            else {
                int min = this._building.minFloor();
                curr.goTo(min);
            }
        }
    }
    private void f2(int elev) { // even --> Down
        Elevator curr = this.getBuilding().getElevetor(elev);
        if(curr.getState() == Elevator.LEVEL) {
            int dir = this.getDirection();
            int pos = curr.getPos();
            if(pos>curr.getMinFloor()) { curr.goTo(pos-1); }
            else {
                int max = this._building.maxFloor();
                curr.goTo(max);
            }
        }
    }
    private void f0() {
        Elevator curr = this.getBuilding().getElevetor(0);
        if(curr.getState() == Elevator.LEVEL) {
            int dir = this.getDirection();
            int pos = curr.getPos();
            boolean up2down = false;
            if(dir == UP) {
                if(pos<curr.getMaxFloor()) {
                    curr.goTo(pos+1);
                }
                else {
                    _direction = DOWN;
                    curr.goTo(pos-1);
                    up2down = true;
                }
            }
            if(dir == DOWN && !up2down) {
                if(pos>curr.getMinFloor()) {
                    curr.goTo(pos-1);
                }
                else {
                    _direction = UP;
                    curr.goTo(pos+1);
                }
            }
        }
    }


    private static int rand(int min, int max) {
        if(max<min) {throw new RuntimeException("ERR: wrong values for range max should be >= min");}
        int ans = min;
        double dx = max-min;
        double r = Math.random()*dx;
        ans = ans + (int)(r);
        return ans;
    }





}
