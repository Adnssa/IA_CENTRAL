import aima.search.framework.HeuristicFunction;

public class BoarHatHeuristicFunction implements HeuristicFunction  {

  public boolean equals(Object obj) {
      boolean retValue;
      retValue = super.equals(obj);
      return retValue;
  }

  public double getHeuristicValue(Object state) {
   BoarHat board=(BoarHat)state;

   double sum = board.getValue();
   return sum;
  }

}
