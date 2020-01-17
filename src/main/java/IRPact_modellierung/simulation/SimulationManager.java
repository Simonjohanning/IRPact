package IRPact_modellierung.simulation;

import IRPact_modellierung.io.ConfigLoader;
import IRPact_modellierung.io.output.*;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;


public class SimulationManager {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger("debugConsoleLogger");
    private static final String CONFIGPATHPREFIX = "./target/configuration/";
    private static final String CONFIGPATH = "Schwarz";

    public static void main(String[] args){
        try {
            String configPath;
            if(args.length == 0) configPath = CONFIGPATHPREFIX + CONFIGPATH + "/";
            else if(args[0].equals("")) configPath = CONFIGPATHPREFIX + CONFIGPATH + "/";
            else configPath = CONFIGPATHPREFIX + args[0] + "/";
            Configuration simulationConfiguration = ConfigLoader.loadConfiguration(configPath);
            LOG.info("Configuration loaded sucessfully");
            SimulationContainer simulationContainer = SimulationFactory.createSimulation(simulationConfiguration);
            LOG.info("Simulation created");
            simulationContainer.getTimeModel().setOutputScheme(new SN1OutputScheme());
            simulationContainer.getTimeModel().startSimulation();
            LOG.info("Thats it folks, the show is over");
        } catch (IOException e) {
            e.printStackTrace();
        }



        /*for(int index=0;index<events.size();index++){
            LOG.info("{}th event in simulation is {}, scheduled for time {}", index, events.get(index), events.get(index).getScheduledForTime());
        }
        Set<ProductGroup> pgs = simulationConfiguration.getProductConfiguration().getProductGroups();
        Set<ProductGroupAttribute> pgas = new HashSet<ProductGroupAttribute>();
        for(ProductGroup pg : pgs){
            pgas.addAll(pg.getProductGroupAttributes());
        }
        LOG.info("pags: {} ({} in total",pgas, pgas.size());
        Set<Message> messages = new HashSet<Message>();
        for(ConsumerAgent consumer : simulationContainer.getConsumerAgents()) {
            messages.addAll(simulationContainer.getConsumerAgentMessageScheme().createMessages(simulationContainer, consumer));
        }
        LOG.info("created {} messages ",messages.size());
        for(Message message : messages){
            message.processMessage(0.0);
        }*/
        System.out.println("Program finished successfully");
    }

   /* public static void toBecomeMain(){
        Configuration simulationConfiguration = ConfigLoader.loadConfiguration(CONFIGPATH);
        SimulationContainer simulationContainer = SimulationFactory.createSimulation(simulationConfiguration);


    }*/
}