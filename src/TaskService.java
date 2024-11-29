import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

public class TaskService {
    private final TaskRepository repository = new TaskRepository();

    public long add(String description) throws IOException {
        List<Task> tasks = find("all");
        Task task = new Task();
        task.setId(repository.loadNextTaskId());
        task.setDescription(description);
        task.setCreatedAt(LocalDateTime.now());
        tasks.add(task);
        
        if(repository.saveData(tasks)) {
            repository.updateNextTaskId();
            return task.getId();
        }
        
        return 0L;
    }
    
    public boolean update(long id, String description) throws FileNotFoundException, IOException {
        return update(id, task -> task.setDescription(description));
    }
    
    public boolean update(long id, TaskStatus status) throws FileNotFoundException, IOException {
        return update(id, task -> task.setStatus(status));
    }
    
    public boolean delete(long id) throws FileNotFoundException, IOException {
        List<Task> tasks = find("all");
        Task task = findTaskById(id, tasks);
    
        if(task != null && tasks.remove(task)) {
            return repository.saveData(tasks);
        }
    
        return false;
    }
    
    public List<Task> find(String filter) throws FileNotFoundException, IOException {
        String match = (filter
        .equals("all") ? ".*\"id\":\\d+.*" : ".*\"status\":\"%s\".*"
        .formatted(filter));
        return repository.loadData(match);
    }
    
    private Task findTaskById(long id, List<Task> tasks) {
        return tasks.stream().filter(task -> task.getId() == id).findFirst().orElse(null);
    }
    
    private boolean update(long id, Consumer<Task> updater) throws FileNotFoundException, IOException {
        List<Task> tasks = find("all");
        Task task = findTaskById(id, tasks);
        
        if(task == null) {
            return false;
        }
        
        updater.accept(task);
        task.setUpdatedAt(LocalDateTime.now());
        return repository.saveData(tasks);
    }
}