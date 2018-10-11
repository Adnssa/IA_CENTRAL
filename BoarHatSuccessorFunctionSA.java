import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BoarHatSuccessorFunctionSA implements SuccessorFunction {
    @SuppressWarnings("unchecked")
    public List getSuccessors(Object aState) {
        ArrayList                retVal = new ArrayList();
        BoarHat             board  = (BoarHat) aState;
        BoarHatHeuristicFunction TSPHF  = new BoarHatHeuristicFunction();
        Random myRandom=new Random();
        int i,j;

        // Nos ahorramos generar todos los sucesores escogiendo un par de ciudades al azar

       i=myRandom.nextInt(board.getNClients());

       do{
              j=myRandom.nextInt(board.getNCentrals());
       } while (i==j);


      BoarHat newBoard = new BoarHat(board.getClients(), board.getProdLeft());

      boolean moure = newBoard.move(i,j);
      /*while(!moure){
        do{
               j=myRandom.nextInt(board.getNCentrals());
        } while (i==j);
        
        moure = newBoard.move(i,j);
      }*/
   //    double   v = TSPHF.getHeuristicValue(newBoard);
   //    String S = new String( i +"|" + j );

       if(moure)retVal.add(new Successor("", newBoard));

      return retVal;
    }
}
