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
			BoarHat retfin = null;


			for (int i = 0; i < board.getNClients(); i++) {
				for (int j = 0; j < board.getNCentrals(); j++) {
					BoarHat newBoard = new BoarHat(board.getClients(), board.getProdLeft(), board.beneficis(), board.getNoAssig());
					boolean moure = newBoard.move(i, j); //Moure el client i a la central j.
					if(moure) {
						retfin = newBoard;
						double x = newBoard.beneficis();
						//double y = newBoard.clientsNoAssignats();
						//double z = newBoard.centralsNoObertes();
						double    v = TSPHF.getHeuristicValue(newBoard);
						//double consum = newBoard.consumTotal();
						//double produccio = newBoard.produccioTotal();
						//double produccioLeft = newBoard.ProduccioLeft();
						String  S = "Beneficis = "+ x +BoarHat.INTERCAMBIO + " " + i + " " + j + " Coste(" + v + ") ---> " + newBoard.toString();
						//S += "Consum = " +consum+ " Produccio = " + produccio + " Produccio Left " + produccioLeft + " -- " + String.valueOf(produccio - consum);
						retVal.add(new Successor(S, newBoard));
					}
				}
				for (int j = i; j < board.getNClients(); j += 2) {
					BoarHat newBoard = new BoarHat(board.getClients(), board.getProdLeft(), board.beneficis(), board.getNoAssig());
					boolean moure = newBoard.swap(i, j); //Moure el client i a la central j.
					if(moure) {
						double x = newBoard.beneficis();
					//	double y = newBoard.clientsNoAssignats();
					//	double z = newBoard.centralsNoObertes();
						double    v = TSPHF.getHeuristicValue(newBoard);
					//	double consum = newBoard.consumTotal();
					//	double produccio = newBoard.produccioTotal();
					//	double produccioLeft = newBoard.ProduccioLeft();
						String  S = "Beneficis = "+ x +BoarHat.INTERCAMBIO + " " + i + " " + j + " Coste(" + v + ") ---> " + newBoard.toString();
						//S += "Consum = " +consum+ " Produccio = " + produccio + " Produccio Left " + produccioLeft + " -- " + String.valueOf(produccio - consum);
						retVal.add(new Successor(S, newBoard));
					}
				}
				}

				//retfin.printState();

				return retVal;
			}
		}
