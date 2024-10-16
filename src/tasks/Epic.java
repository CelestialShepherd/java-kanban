package tasks;

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

    public void removeFromSubtasksIds(Integer id) {
        subtasksIds.remove(id);
    }
}
