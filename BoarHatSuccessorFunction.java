import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;


import java.util.ArrayList;
import java.util.List;


public class BoarHatSuccessorFunction implements SuccessorFunction {
    @SuppressWarnings("unchecked")
    public List getSuccessors(Object aState) {
        ArrayList                retVal = new ArrayList();
        BoarHat             board  = (BoarHat) aState;
        BoarHatHeuristicFunction TSPHF  = new BoarHatHeuristicFunction();

        // No permitimos intercambiar la primera ciudad
        for (int i = 0; i < board.getNClients(); i++) {
            for (int j = 0; j < board.getNCentrals(); j++) {
                BoarHat newBoard = new BoarHat(board.getClients(), board.getProdLeft());

                boolean moure = newBoard.move(i, j); //Moure el client i a la central j.

                if(moure) {
                double    v = TSPHF.getHeuristicValue(newBoard);
                String S = BoarHat.INTERCAMBIO + " " + i + " " + j + " Coste(" + v + ") ---> " + newBoard.toString();
                retVal.add(new Successor(S, newBoard));
              }
            }
        }

        return retVal;
    }
}
