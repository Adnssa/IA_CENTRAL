import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BoarHatSuccessorFunctionSA implements SuccessorFunction {
    @SuppressWarnings("unchecked")
    public List getSuccessors(Object aState){
        ArrayList                retVal = new ArrayList();
        BoarHat             board  = (BoarHat) aState;
        BoarHatHeuristicFunction TSPHF  = new BoarHatHeuristicFunction();
        Random myRandom=new Random();
        int i,j;

        // Nos ahorramos generar todos los sucesores escogiendo un par de ciudades al azar

       i=myRandom.nextInt(board.getNClients());

       do{
              j=myRandom.nextInt(board.getNCentrals());
       } while (board.CliACent(i)==j);


      BoarHat newBoard = new BoarHat(board.getClients(), board.getProdLeft(), board.beneficis(), board.getNoAssig());

      boolean moure = newBoard.move(i,j);
      double y = newBoard.clientsNoAssignats();
      double z = newBoard.centralsNoObertes();
      double   v = TSPHF.getHeuristicValue(newBoard);
      double x = newBoard.beneficis();
      String  S = "Beneficis = "+ x + " "+y+" "+ z+BoarHat.INTERCAMBIO + " " + i + " " + j + " Coste(" + v + ") ---> " + newBoard.toString();
      System.out.println(S);
       if(moure)retVal.add(new Successor(S, newBoard));
       i=myRandom.nextInt(board.getNClients());

       do{
              j=myRandom.nextInt(board.getNClients());
       } while (i==j);
       BoarHat newBoard2 = new BoarHat(board.getClients(), board.getProdLeft(), board.beneficis(), board.getNoAssig());
       moure = newBoard.swap(i,j);
      if(moure)retVal.add(new Successor(S, newBoard));
      return retVal;
    }
}
