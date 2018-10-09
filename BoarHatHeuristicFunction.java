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
       double aux = log(board.alturaOcupada(i)/board.alturaTotal(i));
       sum+= aux*board.alturaOcupada(i)/board.alturaTotal(i);
   }


   return sum;
  }

}
