package IRPact_modellierung.messaging;

import IRPact_modellierung.agents.SpatialAgent;
import IRPact_modellierung.events.CommunicationEvent;
import IRPact_modellierung.helper.MetricHelper;
import IRPact_modellierung.simulation.SimulationContainer;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

/**
 * The ContinuousMetricCommunicationScheme is a CommunicationScheme
 * where the delay of the event scheduling is proportional to the metric
 * distance between the sending and receiving agent.
 *
 * @author Simon Johanning
 */
public class ContinuousMetricCommunicationScheme extends CommunicationScheme{

    private double timeFactor;

    public ContinuousMetricCommunicationScheme(MessageScheme correspondingMessageScheme, double timeFactor) throws IllegalArgumentException{
        super(correspondingMessageScheme);
        if(timeFactor < 0.0) throw new IllegalArgumentException("timeFactor is negative, so events would be scheduled in the past leading to inconsistent states!\nPlease provide valid arguments!");
        this.timeFactor = timeFactor;
    }

    public ContinuousMetricCommunicationScheme(MessageScheme correspondingMessageScheme) {
        super(correspondingMessageScheme);
        this.timeFactor = 1.0;
    }

    /**
     * The CommunicationEvents based on this scheme are scheduled in the future,
     * proportionally to the metric distnace between the sender and receiver,
     * scaled by the timeFactor
     *
     * @param correspondingMessages The messages to transform into communication events
     * @param currentTime The current time of the simulation
     * @param simulationContainer The container the simulation runs in
     * @return CommunicationEvents corresponding to the messages scheduled in the future proportional to he metric distance of the sender and receiver of the messages
     */
    public Set<CommunicationEvent> createCommunicationEvents(Set<Message> correspondingMessages, double currentTime, SimulationContainer simulationContainer) {
        Set<CommunicationEvent> events = new HashSet<>(correspondingMessages.size());
        for(Message currentMessage : correspondingMessages){
            Point2D senderCoordinates = ((SpatialAgent) currentMessage.getSender()).getCoordinates();
            Point2D receiverCoordinates = ((SpatialAgent) currentMessage.getReceiver()).getCoordinates();
            double metricDistance = MetricHelper.calculateDistance(senderCoordinates, receiverCoordinates, simulationContainer.getSpatialModel().getMetric());
            events.add(new CommunicationEvent(currentMessage, currentTime+metricDistance*timeFactor, simulationContainer));
        }
        return events;
    }
}
