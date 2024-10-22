package task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIds;

    public Epic(String name, String description) {
        super(name, description);
        subtasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void addToSubtasksIds(Integer id) {
        if (!getId().equals(id)) {
            subtasksIds.add(id);
        }
    }

    public void removeFromSubtasksIds(Integer id) {
        subtasksIds.remove(id);
    }
}
