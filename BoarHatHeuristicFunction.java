import aima.search.framework.HeuristicFunction;

public class BoarHatHeuristicFunction implements HeuristicFunction  {

  public boolean equals(Object obj) {
      boolean retValue;
      retValue = super.equals(obj);
      return retValue;
  }

  public double getHeuristicValue(Object state) {
   BoarHat board=(BoarHat)state;
   //System.out.println("BENEFICIS: "+(-board.beneficis()));
   //System.out.println("Value: "+(board.getValue()));

   //double sum = -board.beneficis();
   //double sum = -board.beneficis()+board.getValue();
   //double sum = -(board.beneficis()+board.getHeurIndex());
   double sum = -(board.getHeurIndex());
   //double sum = -board.beneficis();

   //System.out.println("Fin: "+sum);
   return sum;
  }

}
