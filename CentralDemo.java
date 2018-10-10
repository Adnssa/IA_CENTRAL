import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import IA.Energia.Centrales;
import IA.Energia.Centrales;
import IA.Energia.Central;
import IA.Energia.Clientes;
import IA.Energia.Cliente;
import IA.Energia.VEnergia;

public class CentralDemo {

    public static void main(String[] args){
        int [] percent  = {5, 10, 25};
        int seedCent = 1234;
        int ncli = 1000;
        double [] propc  = {0.25, 0.3, 0.45};
        double propg = 0.75;
        int seedCli = 1234;

        BoarHat TSPB = new BoarHat(percent, seedCent, ncli, propc, propg, seedCli);
        System.out.println("Beneficis = " + TSPB.getBeneficis());
        TSPHillClimbingSearch(TSPB);
        TSPSimulatedAnnealingSearch(TSPB);
    }

    private static void TSPHillClimbingSearch(BoarHat TSPB) {
        System.out.println("\nTSP HillClimbing  -->");


        try {
            Problem problem =  new Problem(TSPB, new BoarHatSuccessorFunction(), new BoarHatGoalTest(),new BoarHatHeuristicFunction());
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);
            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void TSPSimulatedAnnealingSearch(BoarHat TSPB) {
        System.out.println("\nTSP Simulated Annealing  -->");
        return;

        /*
        try {
            Problem problem =  new Problem(TSPB,new ProbTSPSuccessorFunctionSA(), new ProbTSPGoalTest(),new ProbTSPHeuristicFunction());
            SimulatedAnnealingSearch search =  new SimulatedAnnealingSearch(2000,100,5,0.001);
            //search.traceOn();
            SearchAgent agent = new SearchAgent(problem,search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }


}
