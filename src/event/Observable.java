package event;

import event.events.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    private final List<Observer> observers = new ArrayList<>();

    public void add(Observer observer) {
        observers.add(observer);
    }

    public void update(Event event) {
        if (event == null) {
            return;
        }

        for (Observer ob: observers) {
            ob.update(event);
        }
    }
}