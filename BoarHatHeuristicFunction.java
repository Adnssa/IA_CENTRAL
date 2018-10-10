import aima.search.framework.HeuristicFunction;

public class BoarHatHeuristicFunction implements HeuristicFunction  {

  public boolean equals(Object obj) {
      boolean retValue;
      retValue = super.equals(obj);
      return retValue;
  }

  public double getHeuristicValue(Object state) {
   BoarHat board=(BoarHat)state;

   double sum = 0;
   for(int i = 0; i < board.getNCentrals(); i++){
       //double aux = Math.log(board.prodOcupada(i)/board.prodTotal(i));
       //sum+= aux*board.prodOcupada(i)/board.prodTotal(i);
       sum+=Math.pow(board.prodTotal(i) - board.prodOcupada(i),2);
   }


   return sum;
  }

}
