

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Task {    
    private long id;
    private String description;
    private TaskStatus status = TaskStatus.TODO;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public TaskStatus getStatus() {
        return status;
    }
    
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    
    public void setStatus(String status) {
        this.status = TaskStatus.fromValue(status);
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String toJson() {
        return """
            {"id":%d,"description":"%s","status":"%s","createdAt":"%s","updatedAt":"%s"}"""
            .formatted(id, description, status.toString(), createdAt, updatedAt);
    }
    
    public static Task fromJson(String json) {
        Task task = new Task();
        Map<String, String> maps = new HashMap<String, String>();
        String[] parts = json.replaceAll("^\t\\{\"|\"?},?$", "")
        .split("\"?,\"");
        
        for(String part : parts) {
            String[] keyValue = part.split("\":\"?");
            maps.put(keyValue[0], keyValue[1]);
        }
        
        if(maps.containsKey("id") && maps.get("id").matches("\\d+")) {
            task.setId(Long.parseLong(maps.get("id")));
        }
        
        if(maps.containsKey("description") && !maps.get("description").isBlank()) {
            task.setDescription(maps.get("description"));
        }
        
        if(maps.containsKey("status") && TaskStatus.isValid(maps.get("status"))) {
            task.setStatus(maps.get("status"));
        }
        
        if(maps.containsKey("createdAt") && maps.get("createdAt")
        .matches("\\d{4}(-\\d{2}){2}T\\d{2}(:\\d{2}){2}(\\.\\d{1,9})?")) {
            task.setCreatedAt(LocalDateTime.parse(maps.get("createdAt")));
        }
        
        if(maps.containsKey("updatedAt") && maps.get("updatedAt")
        .matches("\\d{4}(-\\d{2}){2}T\\d{2}(:\\d{2}){2}(\\.\\d{1,9})?")) {
            task.setUpdatedAt(LocalDateTime.parse(maps.get("updatedAt")));
        }
        
        return task;
    }
    
    @Override
    public String toString() {
        return """
            id: %d, description: %s, status: %s, createdAt: %s, updatedAt: %s"""
            .formatted(id, description.replaceAll("\\\\+", ""), 
            status.toString(), createdAt, updatedAt);
    }
}