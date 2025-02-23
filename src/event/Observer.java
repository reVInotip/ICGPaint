package event;

import event.events.Event;

public interface Observer {
    void update(Event event);
}
